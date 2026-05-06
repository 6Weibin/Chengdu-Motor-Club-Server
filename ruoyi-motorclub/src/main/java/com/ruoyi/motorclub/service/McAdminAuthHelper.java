package com.ruoyi.motorclub.service;

import org.springframework.stereotype.Component;
import com.ruoyi.motorclub.constant.McConstants;
import com.ruoyi.motorclub.domain.McAdminPermission;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.enums.McAdminTypeEnum;
import com.ruoyi.motorclub.exception.McForbiddenException;

/**
 * 小程序管理员权限校验工具
 * <p>
 * 集中实现"管理员身份判定 + 业务权限位判定"，供小程序管理端 controller / service 在执行管理操作前调用。
 * 校验失败时抛 {@link McForbiddenException}，由 {@code McForbiddenExceptionHandler} 统一返回 code=403。
 * </p>
 * <p>
 * 与前端 {@code admin-permissions.js} 保持语义对齐：
 * <ul>
 * <li>{@code super_admin} 直通所有断言（含未知 permissionKey）。</li>
 * <li>{@code admin} 仅在对应权限位为字符串 {@code "1"} 时通过 {@code requirePermission}。</li>
 * <li>其它身份（none / 未知 / null 用户）一律视为无权限。</li>
 * </ul>
 * </p>
 * <p>
 * 形态选择：使用 Spring {@code @Component} 而非纯静态工具类，与 RuoYi 服务层风格一致，便于将来注入审计日志、
 * 权限缓存等依赖。当前实现内部不依赖任何 bean，逻辑等价于纯函数。
 * </p>
 *
 * @author AI.Coding
 */
@Component
public class McAdminAuthHelper
{
    /**
     * 校验用户是否具备管理员身份（admin 或 super_admin）。
     *
     * @param user 当前小程序用户，可空
     * @throws McForbiddenException 用户为空或非管理员身份时抛出
     */
    public void requireAdmin(McUser user)
    {
        if (!isAdminOrAbove(user))
        {
            throw new McForbiddenException();
        }
    }

    /**
     * 校验用户是否具备指定业务权限：超管直通；admin 按 {@code "1"} 判定；其它一律拒绝。
     *
     * @param user          当前小程序用户，可空
     * @param permissionKey 权限键（取值见 {@link McConstants}.PERMISSION_*）
     * @throws McForbiddenException 用户为空、非管理员、未授权或 permissionKey 未知（在非超管路径下）时抛出
     */
    public void requirePermission(McUser user, String permissionKey)
    {
        if (!hasPermission(user, permissionKey))
        {
            throw new McForbiddenException();
        }
    }

    /**
     * 查询用户是否具备指定业务权限，不抛异常。
     *
     * @param user          当前小程序用户，可空
     * @param permissionKey 权限键（取值见 {@link McConstants}.PERMISSION_*）
     * @return true 表示具备权限；user 为空、非管理员、未授权或 permissionKey 未知（非超管路径下）均返回 false
     */
    public boolean hasPermission(McUser user, String permissionKey)
    {
        if (user == null)
        {
            return false;
        }
        // 超管对所有 key 直通，包含未知 key
        if (isSuperAdmin(user))
        {
            return true;
        }
        // 非超管必须是 admin 才进入权限位判定
        if (!isAdmin(user))
        {
            return false;
        }
        McAdminPermission permission = user.getAdminPermission();
        if (permission == null)
        {
            return false;
        }
        return isPermissionGranted(permission, permissionKey);
    }

    /**
     * 判断用户是否管理员（admin 或 super_admin）。
     *
     * @param user 用户，可空
     * @return true 表示具备管理员身份
     */
    private boolean isAdminOrAbove(McUser user)
    {
        return isSuperAdmin(user) || isAdmin(user);
    }

    /**
     * 判断用户是否超管。
     *
     * @param user 用户，可空
     * @return true 表示超管
     */
    private boolean isSuperAdmin(McUser user)
    {
        return user != null && McAdminTypeEnum.SUPER_ADMIN.getCode().equals(user.getAppAdminType());
    }

    /**
     * 判断用户是否普通管理员（不含超管）。
     *
     * @param user 用户，可空
     * @return true 表示 appAdminType == "admin"
     */
    private boolean isAdmin(McUser user)
    {
        return user != null && McAdminTypeEnum.ADMIN.getCode().equals(user.getAppAdminType());
    }

    /**
     * 按 permissionKey 取出对应字段并判断是否为 {@code "1"}。未知 key 返回 false。
     *
     * @param permission    权限实体，非空
     * @param permissionKey 权限键
     * @return true 表示该权限位等于字符串 "1"
     */
    private boolean isPermissionGranted(McAdminPermission permission, String permissionKey)
    {
        if (permissionKey == null)
        {
            return false;
        }
        String value;
        switch (permissionKey)
        {
            case McConstants.PERMISSION_MEMBER_MANAGE:
                value = permission.getMemberManage();
                break;
            case McConstants.PERMISSION_ACTIVITY_MANAGE:
                value = permission.getActivityManage();
                break;
            case McConstants.PERMISSION_BANNER_MANAGE:
                value = permission.getBannerManage();
                break;
            case McConstants.PERMISSION_BENEFIT_MANAGE:
                value = permission.getBenefitManage();
                break;
            default:
                // 未知 key 在非超管路径下视为无权限
                return false;
        }
        return "1".equals(value);
    }
}
