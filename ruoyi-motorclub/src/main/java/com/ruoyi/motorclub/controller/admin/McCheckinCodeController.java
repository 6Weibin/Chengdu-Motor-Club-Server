package com.ruoyi.motorclub.controller.admin;

import java.util.List;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
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
import com.ruoyi.motorclub.service.IMcCheckinService;

/**
 * 签到码后台控制器
 *
 * @author AI.Coding
 */
@Controller
@RequestMapping("/motorclub/checkin")
public class McCheckinCodeController extends BaseController
{
    /** 页面前缀。 */
    private static final String PREFIX = "motorclub/checkin";

    @Resource
    private IMcActivityService mcActivityService;

    @Resource
    private IMcCheckinService mcCheckinService;

    /**
     * 打开签到管理页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:checkin:view")
    @GetMapping()
    public String checkin()
    {
        return PREFIX + "/checkin";
    }

    /**
     * 查询签到活动列表。
     *
     * @param activity 查询条件
     * @return 活动列表
     */
    @RequiresPermissions("motorclub:checkin:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(McActivity activity)
    {
        startPage();
        List<McActivity> list = mcActivityService.selectMcActivityList(activity);
        return getDataTable(list);
    }

    /**
     * 查询活动签到码。
     *
     * @param activityId 活动主键
     * @return 签到码
     */
    @RequiresPermissions("motorclub:checkin:view")
    @GetMapping("/code/{activityId}")
    @ResponseBody
    public AjaxResult code(@PathVariable("activityId") Long activityId)
    {
        return success(mcCheckinService.selectCheckinCodeByActivityId(activityId));
    }

    /**
     * 生成签到码。
     *
     * @param activityId 活动主键
     * @return 签到码
     */
    @RequiresPermissions("motorclub:checkin:generate")
    @Log(title = "车友会签到码", businessType = BusinessType.UPDATE)
    @PostMapping("/generate/{activityId}")
    @ResponseBody
    public AjaxResult generate(@PathVariable("activityId") Long activityId)
    {
        return success(mcCheckinService.generateCheckinCode(activityId, getLoginName()));
    }
}
