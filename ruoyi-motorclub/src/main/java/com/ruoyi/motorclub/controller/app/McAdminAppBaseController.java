package com.ruoyi.motorclub.controller.app;

import java.util.List;
import javax.annotation.Resource;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.motorclub.constant.McConstants;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.service.McAdminAuthHelper;

/**
 * 小程序管理端接口控制器基类
 *
 * @author AI.Coding
 */
public class McAdminAppBaseController extends McAppBaseController
{
    @Resource
    private McAdminAuthHelper mcAdminAuthHelper;

    /**
     * 获取当前管理员用户。
     *
     * @return 当前管理员
     */
    protected McUser getRequiredAdminUser()
    {
        McUser user = getRequiredMcUser();
        mcAdminAuthHelper.requireAdmin(user);
        return user;
    }

    /**
     * 校验成员管理权限。
     *
     * @return 当前管理员
     */
    protected McUser getRequiredMemberManageUser()
    {
        return getRequiredPermissionUser(McConstants.PERMISSION_MEMBER_MANAGE);
    }

    /**
     * 校验活动管理权限。
     *
     * @return 当前管理员
     */
    protected McUser getRequiredActivityManageUser()
    {
        return getRequiredPermissionUser(McConstants.PERMISSION_ACTIVITY_MANAGE);
    }

    /**
     * 校验 Banner 管理权限。
     *
     * @return 当前管理员
     */
    protected McUser getRequiredBannerManageUser()
    {
        return getRequiredPermissionUser(McConstants.PERMISSION_BANNER_MANAGE);
    }

    /**
     * 校验权益管理权限。
     *
     * @return 当前管理员
     */
    protected McUser getRequiredBenefitManageUser()
    {
        return getRequiredPermissionUser(McConstants.PERMISSION_BENEFIT_MANAGE);
    }

    /**
     * 将分页列表包装为统一成功响应。
     *
     * @param list 分页列表
     * @return 成功响应
     */
    protected AjaxResult tableSuccess(List<?> list)
    {
        return success(getDataTable(list));
    }

    /**
     * 校验指定业务权限。
     *
     * @param permissionKey 权限键
     * @return 当前管理员
     */
    private McUser getRequiredPermissionUser(String permissionKey)
    {
        McUser user = getRequiredAdminUser();
        // 管理端接口统一通过 helper 断言细粒度权限，保持 403 语义一致。
        mcAdminAuthHelper.requirePermission(user, permissionKey);
        return user;
    }
}
