package com.ruoyi.motorclub.service.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.motorclub.constant.McConstants;
import com.ruoyi.motorclub.convert.McUserVoConverter;
import com.ruoyi.motorclub.domain.McAdminPermission;
import com.ruoyi.motorclub.domain.McMembershipApplication;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.McUserToken;
import com.ruoyi.motorclub.domain.dto.McAdminPermissionBody;
import com.ruoyi.motorclub.domain.dto.McLoginBody;
import com.ruoyi.motorclub.domain.dto.McMembershipApplicationBody;
import com.ruoyi.motorclub.domain.dto.McProfileUpdateBody;
import com.ruoyi.motorclub.domain.dto.McReviewBody;
import com.ruoyi.motorclub.domain.vo.McLoginVo;
import com.ruoyi.motorclub.domain.vo.McSystemSettingVo;
import com.ruoyi.motorclub.domain.vo.McUserVo;
import com.ruoyi.motorclub.domain.vo.McWechatSessionVo;
import com.ruoyi.motorclub.enums.McAdminTypeEnum;
import com.ruoyi.motorclub.enums.McApplicationStatusEnum;
import com.ruoyi.motorclub.enums.McMemberStatusEnum;
import com.ruoyi.motorclub.mapper.McAdminPermissionMapper;
import com.ruoyi.motorclub.mapper.McMembershipApplicationMapper;
import com.ruoyi.motorclub.mapper.McUserMapper;
import com.ruoyi.motorclub.mapper.McUserTokenMapper;
import com.ruoyi.motorclub.service.IMcContentService;
import com.ruoyi.motorclub.service.IMcUserService;
import com.ruoyi.motorclub.service.IMcWechatAuthService;
import com.ruoyi.motorclub.util.McDataUtils;
import com.ruoyi.motorclub.util.McMediaUtils;

/**
 * 用户与会员业务服务实现
 *
 * @author AI.Coding
 */
@Service
public class McUserServiceImpl implements IMcUserService
{
    /** Token 默认有效期天数。 */
    private static final int TOKEN_EXPIRE_DAYS = 30;

    @Resource
    private McUserMapper mcUserMapper;

    @Resource
    private McUserTokenMapper mcUserTokenMapper;

    @Resource
    private McMembershipApplicationMapper mcMembershipApplicationMapper;

    @Resource
    private McAdminPermissionMapper mcAdminPermissionMapper;

    @Resource
    private IMcContentService mcContentService;

    @Resource
    private IMcWechatAuthService mcWechatAuthService;

    /**
     * 查询用户列表。
     *
     * @param user 查询条件
     * @return 用户列表
     */
    @Override
    public List<McUser> selectMcUserList(McUser user)
    {
        return mcUserMapper.selectMcUserList(user);
    }

    /**
     * 通过主键查询用户。
     *
     * @param userId 用户主键
     * @return 用户信息
     */
    @Override
    public McUser selectMcUserById(Long userId)
    {
        McUser user = mcUserMapper.selectMcUserById(userId);
        if (user != null)
        {
            user.setAdminPermission(mcAdminPermissionMapper.selectMcAdminPermissionByUserId(userId));
        }
        return user;
    }

    /**
     * 通过主键查询小程序标准用户信息。
     *
     * @param userId 用户主键
     * @return 标准用户信息
     */
    @Override
    public McUserVo selectMcUserVoById(Long userId)
    {
        // 统一在服务层完成标准 VO 转换，避免控制器直接暴露原始实体字段。
        return McUserVoConverter.toVo(selectMcUserById(userId));
    }

    /**
     * 通过 token 查询登录用户。
     *
     * @param token token 值
     * @return 用户信息
     */
    @Override
    public McUser selectMcUserByToken(String token)
    {
        if (StringUtils.isBlank(token))
        {
            return null;
        }
        McUserToken userToken = mcUserTokenMapper.selectMcUserTokenByValue(token);
        if (userToken == null || !"0".equals(userToken.getStatus()))
        {
            return null;
        }
        if (userToken.getExpiredAt() != null && userToken.getExpiredAt().before(DateUtils.getNowDate()))
        {
            userToken.setStatus("1");
            userToken.setUpdateTime(DateUtils.getNowDate());
            mcUserTokenMapper.updateMcUserToken(userToken);
            return null;
        }
        McUser user = selectMcUserById(userToken.getUserId());
        if (user == null || !"0".equals(user.getStatus()))
        {
            return null;
        }
        userToken.setLastAccessTime(DateUtils.getNowDate());
        userToken.setUpdateTime(DateUtils.getNowDate());
        mcUserTokenMapper.updateMcUserToken(userToken);
        return user;
    }

    /**
     * 判断头像路径是否被用户资料引用。
     *
     * @param avatarUrl 头像路径
     * @return true 表示存在引用
     */
    @Override
    public boolean existsAvatarPath(String avatarUrl)
    {
        String normalizedAvatarUrl = McMediaUtils.normalizeStoredUrl(avatarUrl);
        if (StringUtils.isBlank(normalizedAvatarUrl))
        {
            return false;
        }
        return mcUserMapper.countMcUserByAvatarUrl(normalizedAvatarUrl) > 0;
    }

    /**
     * 小程序登录。
     *
     * @param loginBody 登录请求
     * @param loginIp 登录 IP
     * @return 登录结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public McLoginVo login(McLoginBody loginBody, String loginIp)
    {
        /* 先通过微信 code 换取真实身份，确保前端无法伪造 openid。 */
        McWechatSessionVo session = mcWechatAuthService.exchangeCode(loginBody.getCode());
        String openid = session.getOpenid();

        McUser user = mcUserMapper.selectMcUserByOpenid(openid);
        Date now = DateUtils.getNowDate();
        if (user == null)
        {
            user = new McUser();
            user.setOpenid(openid);
            user.setUnionid(session.getUnionid());
            user.setNickName(StringUtils.defaultIfBlank(loginBody.getNickName(), "微信用户"));
            user.setAvatarUrl(McMediaUtils.normalizeStoredUrl(loginBody.getAvatarUrl()));
            user.setPhone(loginBody.getPhone());
            user.setMemberStatus(McMemberStatusEnum.VISITOR.getCode());
            user.setAppAdminType(McAdminTypeEnum.NONE.getCode());
            user.setStatus("0");
            user.setLastLoginIp(loginIp);
            user.setLastLoginTime(now);
            user.setCreateBy("app");
            user.setCreateTime(now);
            user.setUpdateBy("app");
            user.setUpdateTime(now);
            mcUserMapper.insertMcUser(user);
        }
        else
        {
            /* 拦截已停用/注销账号，禁止其重新通过微信登录获得新 token。 */
            if (!"0".equals(user.getStatus()))
            {
                throw new ServiceException("当前账号已停用，无法登录");
            }
            user.setUnionid(StringUtils.defaultIfBlank(session.getUnionid(), user.getUnionid()));
            user.setNickName(StringUtils.defaultIfBlank(loginBody.getNickName(), user.getNickName()));
            user.setAvatarUrl(StringUtils.defaultIfBlank(McMediaUtils.normalizeStoredUrl(loginBody.getAvatarUrl()), user.getAvatarUrl()));
            user.setPhone(StringUtils.defaultIfBlank(loginBody.getPhone(), user.getPhone()));
            user.setLastLoginIp(loginIp);
            user.setLastLoginTime(now);
            user.setUpdateBy("app");
            user.setUpdateTime(now);
            mcUserMapper.updateMcUser(user);
        }

        /* 重新登录时失效历史 token，确保当前终端使用最新凭证。 */
        mcUserTokenMapper.expireMcUserTokenByUserId(user.getUserId());
        McUserToken userToken = new McUserToken();
        userToken.setUserId(user.getUserId());
        userToken.setTokenValue(IdUtils.simpleUUID());
        userToken.setExpiredAt(DateUtils.addDays(now, TOKEN_EXPIRE_DAYS));
        userToken.setLastAccessTime(now);
        userToken.setStatus("0");
        userToken.setCreateBy("app");
        userToken.setCreateTime(now);
        userToken.setUpdateBy("app");
        userToken.setUpdateTime(now);
        mcUserTokenMapper.insertMcUserToken(userToken);

        McLoginVo loginVo = new McLoginVo();
        loginVo.setToken(userToken.getTokenValue());
        loginVo.setExpiredAt(userToken.getExpiredAt());
        // 登录返回直接输出小程序标准用户结构，屏蔽底层 memberStatus/appAdminType/adminPermission 字段。
        loginVo.setUser(selectMcUserVoById(user.getUserId()));
        return loginVo;
    }

    /**
     * 更新用户资料。
     *
     * @param userId 用户主键
     * @param updateBody 更新内容
     * @return 更新后的用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public McUser updateProfile(Long userId, McProfileUpdateBody updateBody)
    {
        McSystemSettingVo settingVo = mcContentService.getSystemSettings();
        if (Boolean.FALSE.equals(settingVo.getAllowProfileEdit()))
        {
            throw new ServiceException("当前系统不允许修改资料");
        }
        McUser user = requireUser(userId);
        user.setNickName(StringUtils.defaultIfBlank(updateBody.getNickName(), user.getNickName()));
        user.setAvatarUrl(StringUtils.defaultIfBlank(McMediaUtils.normalizeStoredUrl(updateBody.getAvatarUrl()), user.getAvatarUrl()));
        user.setPhone(StringUtils.defaultIfBlank(updateBody.getPhone(), user.getPhone()));
        user.setRealName(StringUtils.defaultIfBlank(updateBody.getRealName(), user.getRealName()));
        user.setCarModel(StringUtils.defaultIfBlank(updateBody.getCarModel(), user.getCarModel()));
        if (StringUtils.isNotBlank(updateBody.getIdNumber()))
        {
            /* 证件号只保存密文和脱敏值，避免后台与接口层直接持有明文。 */
            user.setIdNumberCipher(McDataUtils.cipherIdNumber(updateBody.getIdNumber()));
            user.setIdNumberMasked(McDataUtils.maskIdNumber(updateBody.getIdNumber()));
        }
        user.setUpdateBy("app");
        user.setUpdateTime(DateUtils.getNowDate());
        mcUserMapper.updateMcUser(user);
        return selectMcUserById(userId);
    }

    /**
     * 停用用户。
     *
     * @param userId 用户主键
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int disableUser(Long userId, String operator)
    {
        McUser user = requireUser(userId);
        user.setStatus("1");
        user.setUpdateBy(operator);
        user.setUpdateTime(DateUtils.getNowDate());
        mcUserTokenMapper.expireMcUserTokenByUserId(userId);
        return mcUserMapper.updateMcUser(user);
    }

    /**
     * 用户注销账号。
     *
     * @param userId 用户主键
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelUser(Long userId)
    {
        return disableUser(userId, "app");
    }

    /**
     * 提交入会申请。
     *
     * @param userId 用户主键
     * @param body 申请内容
     * @return 入会申请
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public McMembershipApplication submitMembershipApplication(Long userId, McMembershipApplicationBody body)
    {
        McUser user = requireUser(userId);
        McSystemSettingVo settingVo = mcContentService.getSystemSettings();
        validateMembershipBody(body, settingVo);

        /* 先同步用户实名资料，再将申请快照写入申请表，确保后续审核可追溯。 */
        user.setPhone(body.getPhone());
        user.setRealName(StringUtils.defaultIfBlank(body.getRealName(), user.getRealName()));
        user.setCarModel(StringUtils.defaultIfBlank(body.getCarModel(), user.getCarModel()));
        user.setIdNumberCipher(McDataUtils.cipherIdNumber(body.getIdNumber()));
        user.setIdNumberMasked(McDataUtils.maskIdNumber(body.getIdNumber()));
        user.setUpdateBy("app");
        user.setUpdateTime(DateUtils.getNowDate());
        mcUserMapper.updateMcUser(user);

        McMembershipApplication application = new McMembershipApplication();
        application.setUserId(userId);
        application.setPhone(body.getPhone());
        application.setRealName(user.getRealName());
        application.setCarModel(user.getCarModel());
        application.setIdNumberMasked(user.getIdNumberMasked());
        application.setStatus(Boolean.TRUE.equals(settingVo.getJoinNeedReview()) ? McApplicationStatusEnum.PENDING.getCode() : McApplicationStatusEnum.APPROVED.getCode());
        application.setCreateBy("app");
        application.setCreateTime(DateUtils.getNowDate());
        application.setUpdateBy("app");
        application.setUpdateTime(DateUtils.getNowDate());
        if (Boolean.FALSE.equals(settingVo.getJoinNeedReview()))
        {
            application.setReviewBy("system");
            application.setReviewTime(DateUtils.getNowDate());
        }
        mcMembershipApplicationMapper.insertMcMembershipApplication(application);

        if (Boolean.FALSE.equals(settingVo.getJoinNeedReview()))
        {
            /* 关闭审核开关时，申请提交后直接激活会员身份。 */
            enableMemberUser(user, "system");
        }
        return mcMembershipApplicationMapper.selectMcMembershipApplicationById(application.getApplicationId());
    }

    /**
     * 查询用户最新入会申请。
     *
     * @param userId 用户主键
     * @return 入会申请
     */
    @Override
    public McMembershipApplication selectLatestMembershipApplication(Long userId)
    {
        return mcMembershipApplicationMapper.selectLatestMcMembershipApplicationByUserId(userId);
    }

    /**
     * 查询指定入会申请。
     *
     * @param applicationId 申请主键
     * @return 入会申请
     */
    @Override
    public McMembershipApplication selectMembershipApplicationById(Long applicationId)
    {
        return mcMembershipApplicationMapper.selectMcMembershipApplicationById(applicationId);
    }

    /**
     * 查询入会申请列表。
     *
     * @param application 查询条件
     * @return 入会申请列表
     */
    @Override
    public List<McMembershipApplication> selectMembershipApplicationList(McMembershipApplication application)
    {
        return mcMembershipApplicationMapper.selectMcMembershipApplicationList(application);
    }

    /**
     * 审核入会申请。
     *
     * @param applicationId 申请主键
     * @param body 审核内容
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reviewMembershipApplication(Long applicationId, McReviewBody body, String operator)
    {
        McMembershipApplication application = mcMembershipApplicationMapper.selectMcMembershipApplicationById(applicationId);
        if (application == null)
        {
            throw new ServiceException("入会申请不存在");
        }
        if (!McApplicationStatusEnum.PENDING.getCode().equals(application.getStatus()))
        {
            throw new ServiceException("当前申请已完成审核，不能重复操作");
        }
        application.setStatus(body.getStatus());
        application.setRejectReason(body.getRejectReason());
        application.setReviewBy(operator);
        application.setReviewTime(DateUtils.getNowDate());
        application.setUpdateBy(operator);
        application.setUpdateTime(DateUtils.getNowDate());
        int rows = mcMembershipApplicationMapper.updateMcMembershipApplication(application);
        if (McApplicationStatusEnum.APPROVED.getCode().equals(body.getStatus()))
        {
            enableMemberUser(requireUser(application.getUserId()), operator);
        }
        return rows;
    }

    /**
     * 保存业务管理员权限。
     *
     * @param body 权限内容
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveAdminPermission(McAdminPermissionBody body, String operator)
    {
        McUser user = requireUser(body.getUserId());
        McAdminPermission permission = mcAdminPermissionMapper.selectMcAdminPermissionByUserId(body.getUserId());
        Date now = DateUtils.getNowDate();
        if (permission == null)
        {
            permission = new McAdminPermission();
            permission.setUserId(body.getUserId());
            permission.setMemberManage(StringUtils.defaultIfBlank(body.getMemberManage(), "0"));
            permission.setActivityManage(StringUtils.defaultIfBlank(body.getActivityManage(), "0"));
            permission.setBannerManage(StringUtils.defaultIfBlank(body.getBannerManage(), "0"));
            permission.setBenefitManage(StringUtils.defaultIfBlank(body.getBenefitManage(), "0"));
            permission.setCreateBy(operator);
            permission.setCreateTime(now);
            permission.setUpdateBy(operator);
            permission.setUpdateTime(now);
            mcAdminPermissionMapper.insertMcAdminPermission(permission);
        }
        else
        {
            permission.setMemberManage(StringUtils.defaultIfBlank(body.getMemberManage(), permission.getMemberManage()));
            permission.setActivityManage(StringUtils.defaultIfBlank(body.getActivityManage(), permission.getActivityManage()));
            permission.setBannerManage(StringUtils.defaultIfBlank(body.getBannerManage(), permission.getBannerManage()));
            permission.setBenefitManage(StringUtils.defaultIfBlank(body.getBenefitManage(), permission.getBenefitManage()));
            permission.setUpdateBy(operator);
            permission.setUpdateTime(now);
            mcAdminPermissionMapper.updateMcAdminPermission(permission);
        }
        /* 只要配置了业务权限，即自动升级为业务管理员，降低后台维护成本。 */
        if (McAdminTypeEnum.NONE.getCode().equals(user.getAppAdminType()))
        {
            user.setAppAdminType(McAdminTypeEnum.ADMIN.getCode());
            user.setUpdateBy(operator);
            user.setUpdateTime(now);
            mcUserMapper.updateMcUser(user);
        }
        return 1;
    }

    /**
     * 保存后台用户信息。
     *
     * @param user 用户信息
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int updateMcUser(McUser user, String operator)
    {
        McUser oldUser = requireUser(user.getUserId());
        oldUser.setPhone(StringUtils.defaultIfBlank(user.getPhone(), oldUser.getPhone()));
        oldUser.setRealName(StringUtils.defaultIfBlank(user.getRealName(), oldUser.getRealName()));
        oldUser.setCarModel(StringUtils.defaultIfBlank(user.getCarModel(), oldUser.getCarModel()));
        oldUser.setMemberStatus(StringUtils.defaultIfBlank(user.getMemberStatus(), oldUser.getMemberStatus()));
        oldUser.setAppAdminType(StringUtils.defaultIfBlank(user.getAppAdminType(), oldUser.getAppAdminType()));
        oldUser.setStatus(StringUtils.defaultIfBlank(user.getStatus(), oldUser.getStatus()));
        oldUser.setRemark(user.getRemark());
        oldUser.setUpdateBy(operator);
        oldUser.setUpdateTime(DateUtils.getNowDate());
        return mcUserMapper.updateMcUser(oldUser);
    }

    /**
     * 校验入会申请内容。
     *
     * @param body 申请内容
     * @param settingVo 系统配置
     */
    private void validateMembershipBody(McMembershipApplicationBody body, McSystemSettingVo settingVo)
    {
        if (Boolean.TRUE.equals(settingVo.getJoinRequireDetailedProfile())
            && (StringUtils.isBlank(body.getRealName()) || StringUtils.isBlank(body.getCarModel()) || StringUtils.isBlank(body.getIdNumber())))
        {
            throw new ServiceException("当前入会申请要求填写完整实名资料");
        }
    }

    /**
     * 激活会员身份。
     *
     * @param user 用户信息
     * @param operator 操作人
     */
    private void enableMemberUser(McUser user, String operator)
    {
        user.setMemberStatus(McMemberStatusEnum.MEMBER.getCode());
        user.setJoinTime(DateUtils.getNowDate());
        if (StringUtils.isBlank(user.getMemberCardNo()))
        {
            user.setMemberCardNo(McDataUtils.generateMemberCardNo(user.getJoinTime()));
        }
        user.setUpdateBy(operator);
        user.setUpdateTime(DateUtils.getNowDate());
        mcUserMapper.updateMcUser(user);
    }

    /**
     * 查询并校验用户存在。
     *
     * @param userId 用户主键
     * @return 用户信息
     */
    private McUser requireUser(Long userId)
    {
        McUser user = mcUserMapper.selectMcUserById(userId);
        if (user == null)
        {
            throw new ServiceException("用户不存在");
        }
        return user;
    }
}
