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
import com.ruoyi.motorclub.domain.McBenefit;
import com.ruoyi.motorclub.service.IMcContentService;

/**
 * 权益后台控制器
 *
 * @author AI.Coding
 */
@Controller
@RequestMapping("/motorclub/benefit")
public class McBenefitController extends BaseController
{
    /** 页面前缀。 */
    private static final String PREFIX = "motorclub/benefit";

    @Resource
    private IMcContentService mcContentService;

    /**
     * 打开权益页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:benefit:view")
    @GetMapping()
    public String benefit()
    {
        return PREFIX + "/benefit";
    }

    /**
     * 查询权益列表。
     *
     * @param benefit 查询条件
     * @return 权益列表
     */
    @RequiresPermissions("motorclub:benefit:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(McBenefit benefit)
    {
        startPage();
        List<McBenefit> list = mcContentService.selectMcBenefitList(benefit);
        return getDataTable(list);
    }

    /**
     * 打开权益新增页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:benefit:add")
    @GetMapping("/add")
    public String add()
    {
        return PREFIX + "/add";
    }

    /**
     * 新增权益。
     *
     * @param benefit 权益信息
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:benefit:add")
    @Log(title = "车友会权益", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(McBenefit benefit)
    {
        return toAjax(mcContentService.insertMcBenefit(benefit, getLoginName()));
    }

    /**
     * 打开权益编辑页面。
     *
     * @param benefitId 权益主键
     * @param mmap 页面模型
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:benefit:edit")
    @GetMapping("/edit/{benefitId}")
    public String edit(@PathVariable("benefitId") Long benefitId, ModelMap mmap)
    {
        mmap.put("benefit", mcContentService.selectMcBenefitById(benefitId));
        return PREFIX + "/edit";
    }

    /**
     * 修改权益。
     *
     * @param benefit 权益信息
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:benefit:edit")
    @Log(title = "车友会权益", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(McBenefit benefit)
    {
        return toAjax(mcContentService.updateMcBenefit(benefit, getLoginName()));
    }

    /**
     * 删除权益。
     *
     * @param ids 主键串
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:benefit:remove")
    @Log(title = "车友会权益", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(mcContentService.deleteMcBenefitByIds(ids));
    }
}
