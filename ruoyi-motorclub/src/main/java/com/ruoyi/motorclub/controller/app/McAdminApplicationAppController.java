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
import com.ruoyi.motorclub.domain.McMembershipApplication;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.dto.McReviewBody;
import com.ruoyi.motorclub.domain.vo.McApplicationStatsVo;
import com.ruoyi.motorclub.enums.McApplicationStatusEnum;
import com.ruoyi.motorclub.service.IMcUserService;

/**
 * 小程序管理端入会审核接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/admin/applications")
public class McAdminApplicationAppController extends McAdminAppBaseController
{
    @Resource
    private IMcUserService mcUserService;

    /**
     * 查询申请统计。
     *
     * @return 统计结果
     */
    @GetMapping("/stats")
    public AjaxResult stats()
    {
        getRequiredMemberManageUser();
        List<McMembershipApplication> list = mcUserService.selectMembershipApplicationList(new McMembershipApplication());
        McApplicationStatsVo statsVo = new McApplicationStatsVo();
        statsVo.setTotal(list.size());
        // 统一按状态聚合，便于前端管理页直接展示角标与切换筛选。
        for (McMembershipApplication application : list)
        {
            if (McApplicationStatusEnum.PENDING.getCode().equals(application.getStatus()))
            {
                statsVo.setPending(statsVo.getPending() + 1);
            }
            else if (McApplicationStatusEnum.APPROVED.getCode().equals(application.getStatus()))
            {
                statsVo.setApproved(statsVo.getApproved() + 1);
            }
            else if (McApplicationStatusEnum.REJECTED.getCode().equals(application.getStatus()))
            {
                statsVo.setRejected(statsVo.getRejected() + 1);
            }
        }
        return success(statsVo);
    }

    /**
     * 查询申请列表。
     *
     * @param application 查询条件
     * @return 申请列表
     */
    @GetMapping("/list")
    public AjaxResult list(McMembershipApplication application)
    {
        getRequiredMemberManageUser();
        startPage();
        List<McMembershipApplication> list = mcUserService.selectMembershipApplicationList(application);
        return tableSuccess(list);
    }

    /**
     * 查询申请详情。
     *
     * @param applicationId 申请主键
     * @return 申请详情
     */
    @GetMapping("/{applicationId}")
    public AjaxResult detail(@PathVariable("applicationId") Long applicationId)
    {
        getRequiredMemberManageUser();
        return success(mcUserService.selectMembershipApplicationById(applicationId));
    }

    /**
     * 审核申请。
     *
     * @param applicationId 申请主键
     * @param body 审核内容
     * @return 操作结果
     */
    @PostMapping("/review/{applicationId}")
    public AjaxResult review(@PathVariable("applicationId") Long applicationId, @RequestBody McReviewBody body)
    {
        McUser admin = getRequiredMemberManageUser();
        return toAjax(mcUserService.reviewMembershipApplication(applicationId, body, buildOperator(admin)));
    }

    /**
     * 通过申请。
     *
     * @param applicationId 申请主键
     * @return 操作结果
     */
    @PostMapping("/approve/{applicationId}")
    public AjaxResult approve(@PathVariable("applicationId") Long applicationId)
    {
        McReviewBody body = new McReviewBody();
        body.setStatus(McApplicationStatusEnum.APPROVED.getCode());
        return review(applicationId, body);
    }

    /**
     * 拒绝申请。
     *
     * @param applicationId 申请主键
     * @param body 审核内容
     * @return 操作结果
     */
    @PostMapping("/reject/{applicationId}")
    public AjaxResult reject(@PathVariable("applicationId") Long applicationId, @RequestBody McReviewBody body)
    {
        body.setStatus(McApplicationStatusEnum.REJECTED.getCode());
        return review(applicationId, body);
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
