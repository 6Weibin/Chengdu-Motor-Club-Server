package com.ruoyi.motorclub.convert;

import com.ruoyi.motorclub.domain.McAdminPermission;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.vo.McAdminPermissionsVo;
import com.ruoyi.motorclub.domain.vo.McUserVo;
import com.ruoyi.motorclub.enums.McAdminTypeEnum;
import com.ruoyi.motorclub.enums.McMemberStatusEnum;

/**
 * McUser → McUserVo 转换器
 * <p>
 * 仅做内存层字段拷贝与派生计算，不访问 service/mapper；保持纯函数语义。
 * 派生规则与前端 {@code admin-permissions.js}/{@code auth.js} 对齐：
 * <ul>
 *     <li>{@code isMember = (memberStatus == "member")}</li>
 *     <li>{@code isSuperAdmin = (appAdminType == "super_admin")}</li>
 *     <li>{@code isAdmin = (appAdminType IN {"admin","super_admin"})}</li>
 *     <li>{@code adminPermissions}：超管 → 四键全 true；admin 且非超管 → 按 '1'/'0' 解析；其他 → 全 false</li>
 * </ul>
 * </p>
 *
 * @author AI.Coding
 */
public final class McUserVoConverter
{
    /** 数据库中权限位"启用"的字符值。 */
    private static final String PERMISSION_ENABLED = "1";

    /**
     * 工具类禁止实例化。
     */
    private McUserVoConverter()
    {
    }

    /**
     * 将 {@link McUser} 转换为 {@link McUserVo}。
     *
     * @param user 原始用户实体；允许为 null
     * @return 标准化输出 VO；入参为 null 时返回 null
     */
    public static McUserVo toVo(McUser user)
    {
        if (user == null)
        {
            return null;
        }

        McUserVo vo = new McUserVo();
        // 基础字段拷贝（敏感字段 openid/unionid/idNumberCipher 故意不拷贝）
        vo.setUserId(user.getUserId());
        vo.setNickName(user.getNickName());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setPhone(user.getPhone());
        vo.setRealName(user.getRealName());
        vo.setIdNumberMasked(user.getIdNumberMasked());
        vo.setCarModel(user.getCarModel());
        vo.setMemberCardNo(user.getMemberCardNo());
        vo.setJoinTime(user.getJoinTime());
        vo.setStatus(user.getStatus());
        vo.setLastLoginIp(user.getLastLoginIp());
        vo.setLastLoginTime(user.getLastLoginTime());

        // 派生会员标识
        boolean isMember = McMemberStatusEnum.MEMBER.getCode().equals(user.getMemberStatus());
        // 派生管理员/超级管理员标识
        boolean isSuperAdmin = McAdminTypeEnum.SUPER_ADMIN.getCode().equals(user.getAppAdminType());
        boolean isAdmin = isSuperAdmin
                || McAdminTypeEnum.ADMIN.getCode().equals(user.getAppAdminType());

        vo.setIsMember(isMember);
        vo.setIsAdmin(isAdmin);
        vo.setIsSuperAdmin(isSuperAdmin);
        // 派生四类业务权限位
        vo.setAdminPermissions(derivePermissions(isAdmin, isSuperAdmin, user.getAdminPermission()));

        return vo;
    }

    /**
     * 根据管理员标识和原始权限记录派生标准化权限位。
     *
     * @param isAdmin      是否管理员
     * @param isSuperAdmin 是否超级管理员
     * @param permission   原始权限记录；可能为 null
     * @return 标准化权限 VO，永不为 null
     */
    private static McAdminPermissionsVo derivePermissions(boolean isAdmin, boolean isSuperAdmin,
                                                          McAdminPermission permission)
    {
        McAdminPermissionsVo vo = new McAdminPermissionsVo();
        // 超管：四键直接全 true，无需查权限记录
        if (isSuperAdmin)
        {
            vo.setMemberManage(true);
            vo.setActivityManage(true);
            vo.setBannerManage(true);
            vo.setBenefitManage(true);
            return vo;
        }
        // 非管理员或权限记录缺失：保持默认全 false
        if (!isAdmin || permission == null)
        {
            return vo;
        }
        // 普通管理员：按 char(1) '1' 判定开启
        vo.setMemberManage(PERMISSION_ENABLED.equals(permission.getMemberManage()));
        vo.setActivityManage(PERMISSION_ENABLED.equals(permission.getActivityManage()));
        vo.setBannerManage(PERMISSION_ENABLED.equals(permission.getBannerManage()));
        vo.setBenefitManage(PERMISSION_ENABLED.equals(permission.getBenefitManage()));
        return vo;
    }
}
