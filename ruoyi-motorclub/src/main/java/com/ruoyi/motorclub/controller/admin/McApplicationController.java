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
import com.ruoyi.motorclub.domain.McMembershipApplication;
import com.ruoyi.motorclub.domain.dto.McReviewBody;
import com.ruoyi.motorclub.service.IMcUserService;

/**
 * 入会申请后台控制器
 *
 * @author AI.Coding
 */
@Controller
@RequestMapping("/motorclub/application")
public class McApplicationController extends BaseController
{
    /** 页面前缀。 */
    private static final String PREFIX = "motorclub/application";

    @Resource
    private IMcUserService mcUserService;

    /**
     * 打开申请管理页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:application:view")
    @GetMapping()
    public String application()
    {
        return PREFIX + "/application";
    }

    /**
     * 查询申请列表。
     *
     * @param application 查询条件
     * @return 申请列表
     */
    @RequiresPermissions("motorclub:application:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(McMembershipApplication application)
    {
        startPage();
        List<McMembershipApplication> list = mcUserService.selectMembershipApplicationList(application);
        return getDataTable(list);
    }

    /**
     * 打开申请详情页面。
     *
     * @param userId 用户主键
     * @param mmap 页面模型
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:application:review")
    @GetMapping("/detail/{userId}")
    public String detail(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("application", mcUserService.selectLatestMembershipApplication(userId));
        return PREFIX + "/detail";
    }

    /**
     * 审核申请。
     *
     * @param applicationId 申请主键
     * @param body 审核内容
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:application:review")
    @Log(title = "车友会入会申请", businessType = BusinessType.UPDATE)
    @PostMapping("/review/{applicationId}")
    @ResponseBody
    public AjaxResult review(@PathVariable("applicationId") Long applicationId, @RequestBody McReviewBody body)
    {
        return toAjax(mcUserService.reviewMembershipApplication(applicationId, body, getLoginName()));
    }
}
