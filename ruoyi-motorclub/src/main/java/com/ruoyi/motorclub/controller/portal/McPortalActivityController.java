package com.ruoyi.motorclub.controller.portal;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.motorclub.service.IMcActivityService;

/**
 * 门户活动公开接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/portalApi/motorclub/activity")
public class McPortalActivityController extends BaseController
{
    @Resource
    private IMcActivityService mcActivityService;

    /**
     * 查询门户活动列表。
     *
     * @return 门户活动列表
     */
    @Anonymous
    @GetMapping("/list")
    public AjaxResult list()
    {
        return success(mcActivityService.selectPortalActivityList());
    }

    /**
     * 查询门户活动详情。
     *
     * @param activityId 活动主键
     * @return 门户活动详情
     */
    @Anonymous
    @GetMapping("/{activityId}")
    public AjaxResult detail(@PathVariable("activityId") Long activityId)
    {
        return success(mcActivityService.selectPortalActivityDetail(activityId));
    }
}
