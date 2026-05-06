package com.ruoyi.motorclub.controller.app;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.motorclub.domain.McActivityRegistration;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.dto.McRegistrationReviewBody;
import com.ruoyi.motorclub.enums.McRegistrationStatusEnum;
import com.ruoyi.motorclub.service.IMcActivityService;

/**
 * 小程序管理端活动报名管理接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/admin/registrations")
public class McAdminRegistrationAppController extends McAdminAppBaseController
{
    @Resource
    private IMcActivityService mcActivityService;

    /**
     * 查询报名列表。
     *
     * @param registration 查询条件
     * @return 报名列表
     */
    @GetMapping("/list")
    public AjaxResult list(McActivityRegistration registration)
    {
        getRequiredActivityManageUser();
        startPage();
        List<McActivityRegistration> list = mcActivityService.selectRegistrationList(registration);
        return tableSuccess(list);
    }

    /**
     * 查询报名详情。
     *
     * @param registrationId 报名主键
     * @return 报名详情
     */
    @GetMapping("/{registrationId}")
    public AjaxResult detail(@PathVariable("registrationId") Long registrationId)
    {
        getRequiredActivityManageUser();
        return success(mcActivityService.selectRegistrationById(registrationId));
    }

    /**
     * 审核报名。
     *
     * @param registrationId 报名主键
     * @param body 审核内容
     * @return 操作结果
     */
    @PostMapping("/review/{registrationId}")
    public AjaxResult review(@PathVariable("registrationId") Long registrationId, @RequestBody McRegistrationReviewBody body)
    {
        McUser admin = getRequiredActivityManageUser();
        return toAjax(mcActivityService.reviewRegistration(registrationId, body, buildOperator(admin)));
    }

    /**
     * 通过报名。
     *
     * @param registrationId 报名主键
     * @return 操作结果
     */
    @PostMapping("/approve/{registrationId}")
    public AjaxResult approve(@PathVariable("registrationId") Long registrationId)
    {
        McRegistrationReviewBody body = new McRegistrationReviewBody();
        body.setStatus(McRegistrationStatusEnum.APPROVED.getCode());
        return review(registrationId, body);
    }

    /**
     * 拒绝报名。
     *
     * @param registrationId 报名主键
     * @param body 审核内容
     * @return 操作结果
     */
    @PostMapping("/reject/{registrationId}")
    public AjaxResult reject(@PathVariable("registrationId") Long registrationId, @RequestBody McRegistrationReviewBody body)
    {
        body.setStatus(McRegistrationStatusEnum.REJECTED.getCode());
        return review(registrationId, body);
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
