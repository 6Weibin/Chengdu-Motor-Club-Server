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
import com.ruoyi.motorclub.domain.McActivityRegistration;
import com.ruoyi.motorclub.domain.dto.McRegistrationReviewBody;
import com.ruoyi.motorclub.service.IMcActivityService;

/**
 * 活动报名后台控制器
 *
 * @author AI.Coding
 */
@Controller
@RequestMapping("/motorclub/registration")
public class McRegistrationController extends BaseController
{
    /** 页面前缀。 */
    private static final String PREFIX = "motorclub/registration";

    @Resource
    private IMcActivityService mcActivityService;

    /**
     * 打开报名管理页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:registration:view")
    @GetMapping()
    public String registration()
    {
        return PREFIX + "/registration";
    }

    /**
     * 查询报名列表。
     *
     * @param registration 查询条件
     * @return 报名列表
     */
    @RequiresPermissions("motorclub:registration:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(McActivityRegistration registration)
    {
        startPage();
        List<McActivityRegistration> list = mcActivityService.selectRegistrationList(registration);
        return getDataTable(list);
    }

    /**
     * 打开报名审核页面。
     *
     * @param registrationId 报名主键
     * @param mmap 页面模型
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:registration:review")
    @GetMapping("/review/{registrationId}")
    public String review(@PathVariable("registrationId") Long registrationId, ModelMap mmap)
    {
        McActivityRegistration query = new McActivityRegistration();
        query.setRegistrationId(registrationId);
        List<McActivityRegistration> list = mcActivityService.selectRegistrationList(query);
        mmap.put("registration", list.isEmpty() ? null : list.get(0));
        return PREFIX + "/review";
    }

    /**
     * 审核报名。
     *
     * @param registrationId 报名主键
     * @param body 审核内容
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:registration:review")
    @Log(title = "车友会报名", businessType = BusinessType.UPDATE)
    @PostMapping("/review/{registrationId}")
    @ResponseBody
    public AjaxResult reviewSave(@PathVariable("registrationId") Long registrationId, @RequestBody McRegistrationReviewBody body)
    {
        return toAjax(mcActivityService.reviewRegistration(registrationId, body, getLoginName()));
    }
}
