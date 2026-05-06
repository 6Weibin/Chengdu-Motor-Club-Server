package com.ruoyi.motorclub.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.motorclub.constant.McConstants;
import com.ruoyi.motorclub.domain.McActivityCheckinCode;
import com.ruoyi.motorclub.domain.McActivityRegistration;
import com.ruoyi.motorclub.enums.McRegistrationStatusEnum;
import com.ruoyi.motorclub.mapper.McActivityCheckinCodeMapper;
import com.ruoyi.motorclub.mapper.McActivityRegistrationMapper;
import com.ruoyi.motorclub.service.IMcCheckinService;

/**
 * 签到业务服务实现
 *
 * @author AI.Coding
 */
@Service
public class McCheckinServiceImpl implements IMcCheckinService
{
    @Resource
    private McActivityCheckinCodeMapper mcActivityCheckinCodeMapper;

    @Resource
    private McActivityRegistrationMapper mcActivityRegistrationMapper;

    /**
     * 查询活动签到码。
     *
     * @param activityId 活动主键
     * @return 签到码
     */
    @Override
    public McActivityCheckinCode selectCheckinCodeByActivityId(Long activityId)
    {
        return mcActivityCheckinCodeMapper.selectMcActivityCheckinCodeByActivityId(activityId);
    }

    /**
     * 生成或刷新签到码。
     *
     * @param activityId 活动主键
     * @param operator 操作人
     * @return 签到码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public McActivityCheckinCode generateCheckinCode(Long activityId, String operator)
    {
        McActivityCheckinCode checkinCode = mcActivityCheckinCodeMapper.selectMcActivityCheckinCodeByActivityId(activityId);
        String token = IdUtils.simpleUUID();
        String codeValue = McConstants.CHECKIN_CODE_PREFIX + ":" + activityId + ":" + token;
        if (checkinCode == null)
        {
            checkinCode = new McActivityCheckinCode();
            checkinCode.setActivityId(activityId);
            checkinCode.setToken(token);
            checkinCode.setCodeValue(codeValue);
            checkinCode.setGeneratedTime(DateUtils.getNowDate());
            checkinCode.setGeneratedBy(operator);
            checkinCode.setCreateBy(operator);
            checkinCode.setCreateTime(DateUtils.getNowDate());
            checkinCode.setUpdateBy(operator);
            checkinCode.setUpdateTime(DateUtils.getNowDate());
            mcActivityCheckinCodeMapper.insertMcActivityCheckinCode(checkinCode);
        }
        else
        {
            checkinCode.setToken(token);
            checkinCode.setCodeValue(codeValue);
            checkinCode.setGeneratedTime(DateUtils.getNowDate());
            checkinCode.setGeneratedBy(operator);
            checkinCode.setUpdateBy(operator);
            checkinCode.setUpdateTime(DateUtils.getNowDate());
            mcActivityCheckinCodeMapper.updateMcActivityCheckinCode(checkinCode);
        }
        return mcActivityCheckinCodeMapper.selectMcActivityCheckinCodeByActivityId(activityId);
    }

    /**
     * 执行签到。
     *
     * @param userId 用户主键
     * @param token 签到令牌
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int checkin(Long userId, String token)
    {
        McActivityCheckinCode checkinCode = mcActivityCheckinCodeMapper.selectMcActivityCheckinCodeByToken(token);
        if (checkinCode == null)
        {
            throw new ServiceException("签到码不存在或已失效");
        }
        McActivityRegistration registration = mcActivityRegistrationMapper.selectMcActivityRegistrationByActivityIdAndUserId(checkinCode.getActivityId(), userId);
        if (registration == null)
        {
            throw new ServiceException("当前用户未报名该活动");
        }
        if (!McRegistrationStatusEnum.APPROVED.getCode().equals(registration.getStatus()))
        {
            throw new ServiceException("当前报名状态不允许签到");
        }
        if ("1".equals(registration.getCheckedIn()))
        {
            throw new ServiceException("当前用户已完成签到");
        }
        registration.setCheckedIn("1");
        registration.setCheckedInAt(DateUtils.getNowDate());
        registration.setUpdateBy("app");
        registration.setUpdateTime(DateUtils.getNowDate());
        return mcActivityRegistrationMapper.updateMcActivityRegistration(registration);
    }
}
