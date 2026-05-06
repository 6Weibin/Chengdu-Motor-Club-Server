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
import com.ruoyi.motorclub.domain.McBanner;
import com.ruoyi.motorclub.service.IMcContentService;

/**
 * Banner 后台控制器
 *
 * @author AI.Coding
 */
@Controller
@RequestMapping("/motorclub/banner")
public class McBannerController extends BaseController
{
    /** 页面前缀。 */
    private static final String PREFIX = "motorclub/banner";

    @Resource
    private IMcContentService mcContentService;

    /**
     * 打开 Banner 页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:banner:view")
    @GetMapping()
    public String banner()
    {
        return PREFIX + "/banner";
    }

    /**
     * 查询 Banner 列表。
     *
     * @param banner 查询条件
     * @return Banner 列表
     */
    @RequiresPermissions("motorclub:banner:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(McBanner banner)
    {
        startPage();
        List<McBanner> list = mcContentService.selectMcBannerList(banner);
        return getDataTable(list);
    }

    /**
     * 打开 Banner 新增页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:banner:add")
    @GetMapping("/add")
    public String add()
    {
        return PREFIX + "/add";
    }

    /**
     * 新增 Banner。
     *
     * @param banner Banner 信息
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:banner:add")
    @Log(title = "车友会Banner", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(McBanner banner)
    {
        return toAjax(mcContentService.insertMcBanner(banner, getLoginName()));
    }

    /**
     * 打开 Banner 编辑页面。
     *
     * @param bannerId Banner 主键
     * @param mmap 页面模型
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:banner:edit")
    @GetMapping("/edit/{bannerId}")
    public String edit(@PathVariable("bannerId") Long bannerId, ModelMap mmap)
    {
        mmap.put("banner", mcContentService.selectMcBannerById(bannerId));
        return PREFIX + "/edit";
    }

    /**
     * 修改 Banner。
     *
     * @param banner Banner 信息
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:banner:edit")
    @Log(title = "车友会Banner", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(McBanner banner)
    {
        return toAjax(mcContentService.updateMcBanner(banner, getLoginName()));
    }

    /**
     * 删除 Banner。
     *
     * @param ids 主键串
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:banner:remove")
    @Log(title = "车友会Banner", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(mcContentService.deleteMcBannerByIds(ids));
    }
}
