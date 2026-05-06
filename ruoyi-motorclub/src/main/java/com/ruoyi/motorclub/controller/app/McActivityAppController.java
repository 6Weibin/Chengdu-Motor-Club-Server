package com.ruoyi.motorclub.controller.app;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.motorclub.domain.McActivity;
import com.ruoyi.motorclub.domain.McActivityRegistration;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.service.IMcActivityService;

/**
 * 小程序活动接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/activity")
public class McActivityAppController extends McAppBaseController
{
    @Resource
    private IMcActivityService mcActivityService;

    /**
     * 查询活动列表。
     *
     * @return 活动列表
     */
    // 修复点：活动列表是小程序首页/活动页公开查询入口，必须允许匿名访问。
    @Anonymous
    @GetMapping("/list")
    public AjaxResult list()
    {
        McActivity query = new McActivity();
        List<McActivity> list = mcActivityService.selectMcActivityList(query);
        return success(list);
    }

    /**
     * 查询活动详情。
     *
     * @param activityId 活动主键
     * @return 活动详情
     */
    // 修复点：活动详情属于公开只读接口，应避免被后台网页登录会话链拦截。
    @Anonymous
    @GetMapping("/{activityId}")
    public AjaxResult detail(@PathVariable("activityId") Long activityId)
    {
        return success(mcActivityService.selectMcActivityById(activityId));
    }

    /**
     * 查询当前用户在单活动下的报名状态。
     *
     * @param activityId 活动主键
     * @return 报名状态
     */
    @GetMapping("/register/status/{activityId}")
    public AjaxResult registrationStatus(@PathVariable("activityId") Long activityId)
    {
        McUser user = getRequiredMcUser();
        return success(mcActivityService.selectRegistrationByActivityAndUser(activityId, user.getUserId()));
    }

    /**
     * 提交活动报名。
     *
     * @param activityId 活动主键
     * @return 报名结果
     */
    @PostMapping("/register/{activityId}")
    public AjaxResult register(@PathVariable("activityId") Long activityId)
    {
        McUser user = getRequiredMcUser();
        return success(mcActivityService.registerActivity(activityId, user.getUserId()));
    }

    /**
     * 查询我的活动。
     *
     * @return 报名列表
     */
    @GetMapping("/my")
    public AjaxResult my()
    {
        McUser user = getRequiredMcUser();
        List<McActivityRegistration> list = mcActivityService.selectMyRegistrationList(user.getUserId());
        return success(list);
    }
}
