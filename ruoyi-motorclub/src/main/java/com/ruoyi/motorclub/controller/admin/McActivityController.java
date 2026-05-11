package com.ruoyi.motorclub.controller.admin;

import java.util.List;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.motorclub.domain.McActivity;
import com.ruoyi.motorclub.service.IMcActivityService;

/**
 * 活动后台控制器
 *
 * @author AI.Coding
 */
@Controller
@RequestMapping("/motorclub/activity")
public class McActivityController extends BaseController
{
    /** 页面前缀。 */
    private static final String PREFIX = "motorclub/activity";

    @Resource
    private IMcActivityService mcActivityService;

    /**
     * 打开活动管理页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:activity:view")
    @GetMapping()
    public String activity()
    {
        return PREFIX + "/activity";
    }

    /**
     * 查询活动列表。
     *
     * @param activity 查询条件
     * @return 活动列表
     */
    @RequiresPermissions("motorclub:activity:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(McActivity activity)
    {
        startPage();
        List<McActivity> list = mcActivityService.selectMcActivityList(activity);
        return getDataTable(list);
    }

    /**
     * 打开已删除活动管理页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:activity:deleted:view")
    @GetMapping("/deleted")
    public String deleted()
    {
        return PREFIX + "/deleted";
    }

    /**
     * 查询已删除活动列表。
     *
     * @param activity 查询条件
     * @return 已删除活动列表
     */
    @RequiresPermissions("motorclub:activity:deleted:list")
    @PostMapping("/deleted/list")
    @ResponseBody
    public TableDataInfo deletedList(McActivity activity)
    {
        startPage();
        List<McActivity> list = mcActivityService.selectDeletedMcActivityList(activity);
        return getDataTable(list);
    }

    /**
     * 打开活动新增页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:activity:add")
    @GetMapping("/add")
    public String add()
    {
        return PREFIX + "/add";
    }

    /**
     * 新增活动。
     *
     * @param activity 活动信息
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:activity:add")
    @Log(title = "车友会活动", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(McActivity activity)
    {
        return toAjax(mcActivityService.insertMcActivity(activity, getLoginName()));
    }

    /**
     * 打开活动编辑页面。
     *
     * @param activityId 活动主键
     * @param mmap 页面模型
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:activity:edit")
    @GetMapping("/edit/{activityId}")
    public String edit(@PathVariable("activityId") Long activityId, ModelMap mmap)
    {
        mmap.put("activity", mcActivityService.selectMcActivityById(activityId));
        return PREFIX + "/edit";
    }

    /**
     * 修改活动。
     *
     * @param activity 活动信息
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:activity:edit")
    @Log(title = "车友会活动", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(McActivity activity)
    {
        return toAjax(mcActivityService.updateMcActivity(activity, getLoginName()));
    }

    /**
     * 逻辑删除活动。
     *
     * @param ids 主键串
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:activity:remove")
    @Log(title = "车友会活动", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(mcActivityService.deleteMcActivityByIds(ids, getLoginName()));
    }

    /**
     * 恢复已删除活动。
     *
     * @param activityId 活动主键
     * @param status 恢复后的目标状态
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:activity:restore")
    @Log(title = "车友会活动", businessType = BusinessType.UPDATE)
    @PostMapping("/restore")
    @ResponseBody
    public AjaxResult restore(Long activityId, String status)
    {
        return toAjax(mcActivityService.restoreMcActivity(activityId, status, getLoginName()));
    }
}
