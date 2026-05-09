package com.ruoyi.motorclub.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.motorclub.domain.McActivity;
import com.ruoyi.motorclub.domain.McActivityRegistration;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.dto.McRegistrationReviewBody;
import com.ruoyi.motorclub.domain.vo.McPortalActivityDetailVo;
import com.ruoyi.motorclub.domain.vo.McPortalActivityListVo;
import com.ruoyi.motorclub.enums.McActivityStatusEnum;
import com.ruoyi.motorclub.enums.McRegistrationStatusEnum;
import com.ruoyi.motorclub.mapper.McActivityMapper;
import com.ruoyi.motorclub.mapper.McActivityRegistrationMapper;
import com.ruoyi.motorclub.mapper.McUserMapper;
import com.ruoyi.motorclub.service.IMcActivityService;
import com.ruoyi.motorclub.util.McMediaUtils;

/**
 * 活动与报名业务服务实现
 *
 * @author AI.Coding
 */
@Service
public class McActivityServiceImpl implements IMcActivityService
{
    @Resource
    private McActivityMapper mcActivityMapper;

    @Resource
    private McActivityRegistrationMapper mcActivityRegistrationMapper;

    @Resource
    private McUserMapper mcUserMapper;

    /**
     * 查询活动列表。
     *
     * @param activity 查询条件
     * @return 活动列表
     */
    @Override
    public List<McActivity> selectMcActivityList(McActivity activity)
    {
        return mcActivityMapper.selectMcActivityList(activity);
    }

    /**
     * 查询门户活动列表。
     *
     * @return 门户活动列表
     */
    @Override
    public List<McPortalActivityListVo> selectPortalActivityList()
    {
        return mcActivityMapper.selectPortalActivityList();
    }

    /**
     * 查询门户活动详情。
     *
     * @param activityId 活动主键
     * @return 门户活动详情
     */
    @Override
    public McPortalActivityDetailVo selectPortalActivityDetail(Long activityId)
    {
        McPortalActivityDetailVo detailVo = mcActivityMapper.selectPortalActivityDetailById(activityId);
        if (detailVo == null)
        {
            throw new ServiceException("活动不存在或暂不可访问");
        }
        // 门户详情页允许查看已结束活动，但已取消活动必须在服务层显式拦截。
        if (McActivityStatusEnum.CANCELED.getCode().equals(detailVo.getStatus()))
        {
            throw new ServiceException("活动不存在或暂不可访问");
        }
        return detailVo;
    }

    /**
     * 通过主键查询活动。
     *
     * @param activityId 活动主键
     * @return 活动信息
     */
    @Override
    public McActivity selectMcActivityById(Long activityId)
    {
        return mcActivityMapper.selectMcActivityById(activityId);
    }

    /**
     * 新增活动。
     *
     * @param activity 活动信息
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int insertMcActivity(McActivity activity, String operator)
    {
        activity.setCoverImageUrl(McMediaUtils.normalizeStoredUrl(activity.getCoverImageUrl()));
        // 修复点：小程序活动创建页未提供状态选择，新增活动时需要由服务端补默认“未开始”状态，避免写入 null 触发数据库非空约束。
        activity.setStatus(StringUtils.defaultIfBlank(activity.getStatus(), McActivityStatusEnum.UPCOMING.getCode()));
        // 修复点：活动表单已经不再录入结束时间，但数据库 end_time 仍是非空字段，这里统一回填为开始时间以兼容现有表结构。
        normalizeLegacyEndTime(activity);
        activity.setCurrentParticipants(activity.getCurrentParticipants() == null ? 0 : activity.getCurrentParticipants());
        activity.setCreateBy(operator);
        activity.setCreateTime(DateUtils.getNowDate());
        activity.setUpdateBy(operator);
        activity.setUpdateTime(DateUtils.getNowDate());
        return mcActivityMapper.insertMcActivity(activity);
    }

    /**
     * 修改活动。
     *
     * @param activity 活动信息
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int updateMcActivity(McActivity activity, String operator)
    {
        McActivity oldActivity = requireActivity(activity.getActivityId());
        if (activity.getCapacity() != null && activity.getCapacity() < oldActivity.getCurrentParticipants())
        {
            throw new ServiceException("活动人数上限不能小于当前已通过报名人数");
        }
        activity.setCoverImageUrl(McMediaUtils.normalizeStoredUrl(activity.getCoverImageUrl()));
        // 修复点：编辑活动时同样不再提交结束时间，需要在服务层保持遗留 end_time 字段与开始时间一致。
        normalizeLegacyEndTime(activity);
        activity.setUpdateBy(operator);
        activity.setUpdateTime(DateUtils.getNowDate());
        return mcActivityMapper.updateMcActivity(activity);
    }

    /**
     * 兼容旧表结构中的结束时间非空约束。
     *
     * @param activity 活动信息
     */
    private void normalizeLegacyEndTime(McActivity activity)
    {
        if (StringUtils.isBlank(activity.getEndTime()))
        {
            activity.setEndTime(activity.getStartTime());
        }
    }

    /**
     * 删除活动。
     *
     * @param ids 活动主键串
     * @return 影响行数
     */
    @Override
    public int deleteMcActivityByIds(String ids)
    {
        Long[] activityIds = Convert.toLongArray(ids);
        for (Long activityId : activityIds)
        {
            McActivityRegistration query = new McActivityRegistration();
            query.setActivityId(activityId);
            if (!mcActivityRegistrationMapper.selectMcActivityRegistrationList(query).isEmpty())
            {
                throw new ServiceException("活动存在报名记录，不能直接删除");
            }
        }
        return mcActivityMapper.deleteMcActivityByIds(activityIds);
    }

    /**
     * 提交活动报名。
     *
     * @param activityId 活动主键
     * @param userId 用户主键
     * @return 报名记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public McActivityRegistration registerActivity(Long activityId, Long userId)
    {
        McActivity activity = requireActivity(activityId);
        McUser user = mcUserMapper.selectMcUserById(userId);
        if (user == null || !"0".equals(user.getStatus()))
        {
            throw new ServiceException("当前用户不可报名");
        }
        if (!com.ruoyi.motorclub.enums.McMemberStatusEnum.MEMBER.getCode().equals(user.getMemberStatus()))
        {
            throw new ServiceException("仅会员可报名当前活动");
        }
        if (!com.ruoyi.motorclub.enums.McActivityStatusEnum.UPCOMING.getCode().equals(activity.getStatus()))
        {
            throw new ServiceException("当前活动状态不允许报名");
        }
        if (activity.getCapacity() != null && activity.getCurrentParticipants() != null && activity.getCurrentParticipants() >= activity.getCapacity())
        {
            throw new ServiceException("活动报名人数已满");
        }
        McActivityRegistration exists = mcActivityRegistrationMapper.selectMcActivityRegistrationByActivityIdAndUserId(activityId, userId);
        if (exists != null)
        {
            throw new ServiceException("请勿重复报名当前活动");
        }
        McActivityRegistration registration = new McActivityRegistration();
        registration.setActivityId(activityId);
        registration.setUserId(userId);
        registration.setStatus(McRegistrationStatusEnum.PENDING.getCode());
        registration.setCheckedIn("0");
        registration.setCreateBy("app");
        registration.setCreateTime(DateUtils.getNowDate());
        registration.setUpdateBy("app");
        registration.setUpdateTime(DateUtils.getNowDate());
        mcActivityRegistrationMapper.insertMcActivityRegistration(registration);
        return mcActivityRegistrationMapper.selectMcActivityRegistrationById(registration.getRegistrationId());
    }

    /**
     * 查询单条报名记录。
     *
     * @param registrationId 报名主键
     * @return 报名记录
     */
    @Override
    public McActivityRegistration selectRegistrationById(Long registrationId)
    {
        return mcActivityRegistrationMapper.selectMcActivityRegistrationById(registrationId);
    }

    /**
     * 按活动和用户查询报名记录。
     *
     * @param activityId 活动主键
     * @param userId 用户主键
     * @return 报名记录
     */
    @Override
    public McActivityRegistration selectRegistrationByActivityAndUser(Long activityId, Long userId)
    {
        return mcActivityRegistrationMapper.selectMcActivityRegistrationByActivityIdAndUserId(activityId, userId);
    }

    /**
     * 查询报名列表。
     *
     * @param registration 查询条件
     * @return 报名列表
     */
    @Override
    public List<McActivityRegistration> selectRegistrationList(McActivityRegistration registration)
    {
        return mcActivityRegistrationMapper.selectMcActivityRegistrationList(registration);
    }

    /**
     * 查询我的活动。
     *
     * @param userId 用户主键
     * @return 报名列表
     */
    @Override
    public List<McActivityRegistration> selectMyRegistrationList(Long userId)
    {
        return mcActivityRegistrationMapper.selectMyMcActivityRegistrationList(userId);
    }

    /**
     * 审核报名。
     *
     * @param registrationId 报名主键
     * @param body 审核内容
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reviewRegistration(Long registrationId, McRegistrationReviewBody body, String operator)
    {
        McActivityRegistration registration = mcActivityRegistrationMapper.selectMcActivityRegistrationById(registrationId);
        if (registration == null)
        {
            throw new ServiceException("报名记录不存在");
        }
        if (!McRegistrationStatusEnum.PENDING.getCode().equals(registration.getStatus()))
        {
            throw new ServiceException("报名记录已审核，不能重复操作");
        }
        registration.setStatus(body.getStatus());
        registration.setRejectReason(body.getRejectReason());
        registration.setReviewBy(operator);
        registration.setReviewTime(DateUtils.getNowDate());
        registration.setUpdateBy(operator);
        registration.setUpdateTime(DateUtils.getNowDate());
        int rows = mcActivityRegistrationMapper.updateMcActivityRegistration(registration);
        if (McRegistrationStatusEnum.APPROVED.getCode().equals(body.getStatus()))
        {
            /* 报名审核通过后同步累计参与人数，供后台直接查看容量消耗情况。 */
            McActivity activity = requireActivity(registration.getActivityId());
            activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
            activity.setUpdateBy(operator);
            activity.setUpdateTime(DateUtils.getNowDate());
            mcActivityMapper.updateMcActivity(activity);
        }
        return rows;
    }

    /**
     * 查询并校验活动存在。
     *
     * @param activityId 活动主键
     * @return 活动信息
     */
    private McActivity requireActivity(Long activityId)
    {
        McActivity activity = mcActivityMapper.selectMcActivityById(activityId);
        if (activity == null)
        {
            throw new ServiceException("活动不存在");
        }
        return activity;
    }
}
