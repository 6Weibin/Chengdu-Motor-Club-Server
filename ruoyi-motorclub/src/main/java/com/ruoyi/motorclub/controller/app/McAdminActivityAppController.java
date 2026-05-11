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
import com.ruoyi.motorclub.domain.McActivity;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.dto.McActivityStatusBody;
import com.ruoyi.motorclub.service.IMcActivityService;

/**
 * 小程序管理端活动管理接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/admin/activities")
public class McAdminActivityAppController extends McAdminAppBaseController
{
    @Resource
    private IMcActivityService mcActivityService;

    /**
     * 查询活动列表。
     *
     * @param activity 查询条件
     * @return 活动列表
     */
    @GetMapping("/list")
    public AjaxResult list(McActivity activity)
    {
        getRequiredActivityManageUser();
        startPage();
        List<McActivity> list = mcActivityService.selectMcActivityList(activity);
        return tableSuccess(list);
    }

    /**
     * 查询活动详情。
     *
     * @param activityId 活动主键
     * @return 活动详情
     */
    @GetMapping("/{activityId}")
    public AjaxResult detail(@PathVariable("activityId") Long activityId)
    {
        getRequiredActivityManageUser();
        return success(mcActivityService.selectMcActivityById(activityId));
    }

    /**
     * 新增活动。
     *
     * @param activity 活动内容
     * @return 操作结果
     */
    @PostMapping("/create")
    public AjaxResult create(@RequestBody McActivity activity)
    {
        McUser admin = getRequiredActivityManageUser();
        mcActivityService.insertMcActivity(activity, buildOperator(admin));
        return success(activity);
    }

    /**
     * 修改活动。
     *
     * @param activity 活动内容
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody McActivity activity)
    {
        McUser admin = getRequiredActivityManageUser();
        mcActivityService.updateMcActivity(activity, buildOperator(admin));
        return success(mcActivityService.selectMcActivityById(activity.getActivityId()));
    }

    /**
     * 仅更新活动状态。
     *
     * @param activityId 活动主键
     * @param body 状态内容
     * @return 操作结果
     */
    @PostMapping("/status/{activityId}")
    public AjaxResult updateStatus(@PathVariable("activityId") Long activityId, @Valid @RequestBody McActivityStatusBody body)
    {
        McActivity activity = new McActivity();
        activity.setActivityId(activityId);
        activity.setStatus(body.getStatus());
        return update(activity);
    }

    /**
     * 逻辑删除活动。
     *
     * @param activityId 活动主键
     * @return 操作结果
     */
    @PostMapping("/remove/{activityId}")
    public AjaxResult remove(@PathVariable("activityId") Long activityId)
    {
        McUser admin = getRequiredActivityManageUser();
        return toAjax(mcActivityService.deleteMcActivityByIds(String.valueOf(activityId), buildOperator(admin)));
    }

    /**
     * 构造操作人标识。
     *
     * @param admin 当前管理员
     * @return 操作人
     */
    private String buildOperator(McUser admin)
    {
        return StringUtils.defaultIfBlank(admin.getNickName(), "app-admin");
    }
}
