package com.ruoyi.motorclub.controller.app;

import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.dto.McAdminPermissionBody;
import com.ruoyi.motorclub.service.IMcUserService;

/**
 * 小程序管理端会员管理接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/admin/users")
public class McAdminUserAppController extends McAdminAppBaseController
{
    @Resource
    private IMcUserService mcUserService;

    /**
     * 查询会员列表。
     *
     * @param user 查询条件
     * @return 会员列表
     */
    @GetMapping("/list")
    public AjaxResult list(McUser user)
    {
        getRequiredMemberManageUser();
        startPage();
        List<McUser> list = mcUserService.selectMcUserList(user);
        return tableSuccess(list);
    }

    /**
     * 查询会员详情。
     *
     * @param userId 用户主键
     * @return 会员详情
     */
    @GetMapping("/{userId}")
    public AjaxResult detail(@PathVariable("userId") Long userId)
    {
        getRequiredMemberManageUser();
        return success(mcUserService.selectMcUserVoById(userId));
    }

    /**
     * 保存会员信息。
     *
     * @param body 会员信息
     * @return 操作结果
     */
    @PostMapping("/save")
    public AjaxResult save(@RequestBody McUser body)
    {
        McUser admin = getRequiredMemberManageUser();
        mcUserService.updateMcUser(body, buildOperator(admin));
        return success(mcUserService.selectMcUserVoById(body.getUserId()));
    }

    /**
     * 停用会员。
     *
     * @param userId 用户主键
     * @return 操作结果
     */
    @PostMapping("/disable/{userId}")
    public AjaxResult disable(@PathVariable("userId") Long userId)
    {
        McUser admin = getRequiredMemberManageUser();
        return toAjax(mcUserService.disableUser(userId, buildOperator(admin)));
    }

    /**
     * 删除或停用会员。
     *
     * @param userId 用户主键
     * @return 操作结果
     */
    @PostMapping("/remove/{userId}")
    public AjaxResult remove(@PathVariable("userId") Long userId)
    {
        return disable(userId);
    }

    /**
     * 设置或取消管理员身份。
     *
     * @param userId 用户主键
     * @param body 管理员类型内容
     * @return 操作结果
     */
    @PostMapping("/admin-type/{userId}")
    public AjaxResult updateAdminType(@PathVariable("userId") Long userId, @RequestBody McUser body)
    {
        McUser admin = getRequiredMemberManageUser();
        McUser updateBody = new McUser();
        updateBody.setUserId(userId);
        updateBody.setAppAdminType(body.getAppAdminType());
        mcUserService.updateMcUser(updateBody, buildOperator(admin));
        return success(mcUserService.selectMcUserVoById(userId));
    }

    /**
     * 保存业务管理员权限。
     *
     * @param body 权限内容
     * @return 操作结果
     */
    @PostMapping("/permission")
    public AjaxResult savePermission(@Valid @RequestBody McAdminPermissionBody body)
    {
        McUser admin = getRequiredMemberManageUser();
        mcUserService.saveAdminPermission(body, buildOperator(admin));
        return success(mcUserService.selectMcUserVoById(body.getUserId()));
    }

    /**
     * 构造操作人标识。
     *
     * @param admin 当前管理员
     * @return 操作人
     */
    private String buildOperator(McUser admin)
    {
        // 小程序管理端统一使用当前管理员昵称记录审计，缺省时退化为固定标识。
        return StringUtils.defaultIfBlank(admin.getNickName(), "app-admin");
    }
}
