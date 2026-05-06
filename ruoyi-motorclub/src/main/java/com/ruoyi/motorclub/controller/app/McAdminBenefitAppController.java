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
import com.ruoyi.motorclub.domain.McBenefit;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.service.IMcContentService;

/**
 * 小程序管理端权益接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/admin/benefits")
public class McAdminBenefitAppController extends McAdminAppBaseController
{
    @Resource
    private IMcContentService mcContentService;

    /**
     * 查询权益列表。
     *
     * @param benefit 查询条件
     * @return 权益列表
     */
    @GetMapping("/list")
    public AjaxResult list(McBenefit benefit)
    {
        getRequiredBenefitManageUser();
        startPage();
        List<McBenefit> list = mcContentService.selectMcBenefitList(benefit);
        return tableSuccess(list);
    }

    /**
     * 查询权益详情。
     *
     * @param benefitId 权益主键
     * @return 权益详情
     */
    @GetMapping("/{benefitId}")
    public AjaxResult detail(@PathVariable("benefitId") Long benefitId)
    {
        getRequiredBenefitManageUser();
        return success(mcContentService.selectMcBenefitById(benefitId));
    }

    /**
     * 新增权益。
     *
     * @param benefit 权益内容
     * @return 操作结果
     */
    @PostMapping("/create")
    public AjaxResult create(@RequestBody McBenefit benefit)
    {
        McUser admin = getRequiredBenefitManageUser();
        mcContentService.insertMcBenefit(benefit, buildOperator(admin));
        return success(benefit);
    }

    /**
     * 修改权益。
     *
     * @param benefit 权益内容
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody McBenefit benefit)
    {
        McUser admin = getRequiredBenefitManageUser();
        mcContentService.updateMcBenefit(benefit, buildOperator(admin));
        return success(mcContentService.selectMcBenefitById(benefit.getBenefitId()));
    }

    /**
     * 删除权益。
     *
     * @param benefitId 权益主键
     * @return 操作结果
     */
    @PostMapping("/remove/{benefitId}")
    public AjaxResult remove(@PathVariable("benefitId") Long benefitId)
    {
        getRequiredBenefitManageUser();
        return toAjax(mcContentService.deleteMcBenefitByIds(String.valueOf(benefitId)));
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
