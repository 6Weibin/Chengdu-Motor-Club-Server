package com.ruoyi.motorclub.controller.admin;

import java.util.List;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.dto.McAdminPermissionBody;
import com.ruoyi.motorclub.service.IMcUserService;

/**
 * 会员与用户后台控制器
 *
 * @author AI.Coding
 */
@Controller
@RequestMapping("/motorclub/user")
public class McUserController extends BaseController
{
    /** 页面前缀。 */
    private static final String PREFIX = "motorclub/user";

    @Resource
    private IMcUserService mcUserService;

    /**
     * 打开用户管理页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:user:view")
    @GetMapping()
    public String user()
    {
        return PREFIX + "/user";
    }

    /**
     * 查询用户列表。
     *
     * @param user 查询条件
     * @return 用户列表
     */
    @RequiresPermissions("motorclub:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(McUser user)
    {
        startPage();
        List<McUser> list = mcUserService.selectMcUserList(user);
        return getDataTable(list);
    }

    /**
     * 打开用户编辑页面。
     *
     * @param userId 用户主键
     * @param mmap 页面模型
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:user:edit")
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("user", mcUserService.selectMcUserById(userId));
        return PREFIX + "/edit";
    }

    /**
     * 保存用户编辑。
     *
     * @param user 用户信息
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:user:edit")
    @Log(title = "车友会用户", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(McUser user)
    {
        return toAjax(mcUserService.updateMcUser(user, getLoginName()));
    }

    /**
     * 停用用户。
     *
     * @param userId 用户主键
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:user:remove")
    @Log(title = "车友会用户", businessType = BusinessType.UPDATE)
    @PostMapping("/disable/{userId}")
    @ResponseBody
    public AjaxResult disable(@PathVariable("userId") Long userId)
    {
        return toAjax(mcUserService.disableUser(userId, getLoginName()));
    }

    /**
     * 保存业务管理员权限。
     *
     * @param body 权限内容
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:user:permission")
    @Log(title = "车友会业务权限", businessType = BusinessType.UPDATE)
    @PostMapping("/permission")
    @ResponseBody
    public AjaxResult permission(@RequestBody McAdminPermissionBody body)
    {
        return toAjax(mcUserService.saveAdminPermission(body, getLoginName()));
    }
}
