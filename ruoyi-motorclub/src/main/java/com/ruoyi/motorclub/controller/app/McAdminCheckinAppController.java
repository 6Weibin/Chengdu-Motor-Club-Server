package com.ruoyi.motorclub.controller.app;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.service.IMcCheckinService;

/**
 * 小程序管理端签到码接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/admin/checkin")
public class McAdminCheckinAppController extends McAdminAppBaseController
{
    @Resource
    private IMcCheckinService mcCheckinService;

    /**
     * 查询活动签到码。
     *
     * @param activityId 活动主键
     * @return 签到码
     */
    @GetMapping("/code/{activityId}")
    public AjaxResult code(@PathVariable("activityId") Long activityId)
    {
        getRequiredActivityManageUser();
        return success(mcCheckinService.selectCheckinCodeByActivityId(activityId));
    }

    /**
     * 生成签到码。
     *
     * @param activityId 活动主键
     * @return 签到码
     */
    @PostMapping("/generate/{activityId}")
    public AjaxResult generate(@PathVariable("activityId") Long activityId)
    {
        McUser admin = getRequiredActivityManageUser();
        return success(mcCheckinService.generateCheckinCode(activityId, buildOperator(admin)));
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
