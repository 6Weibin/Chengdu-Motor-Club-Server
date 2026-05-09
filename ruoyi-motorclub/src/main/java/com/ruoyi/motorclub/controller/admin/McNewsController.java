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
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.motorclub.domain.McNews;
import com.ruoyi.motorclub.service.IMcContentService;

/**
 * 新闻后台控制器
 *
 * @author AI.Coding
 */
@Controller
@RequestMapping("/motorclub/news")
public class McNewsController extends BaseController
{
    /** 页面前缀。 */
    private static final String PREFIX = "motorclub/news";

    @Resource
    private IMcContentService mcContentService;

    /**
     * 打开新闻页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:news:view")
    @GetMapping()
    public String news()
    {
        return PREFIX + "/news";
    }

    /**
     * 查询新闻列表。
     *
     * @param news 查询条件
     * @return 新闻列表
     */
    @RequiresPermissions("motorclub:news:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(McNews news)
    {
        startPage();
        List<McNews> list = mcContentService.selectMcNewsList(news);
        return getDataTable(list);
    }

    /**
     * 打开新闻新增页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:news:add")
    @GetMapping("/add")
    public String add()
    {
        return PREFIX + "/add";
    }

    /**
     * 新增新闻。
     *
     * @param news 新闻信息
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:news:add")
    @Log(title = "车友会新闻", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(McNews news)
    {
        return toAjax(mcContentService.insertMcNews(news, getLoginName()));
    }

    /**
     * 打开新闻编辑页面。
     *
     * @param newsId 新闻主键
     * @param mmap 页面模型
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:news:edit")
    @GetMapping("/edit/{newsId}")
    public String edit(@PathVariable("newsId") Long newsId, ModelMap mmap)
    {
        McNews news = mcContentService.selectMcNewsById(newsId);
        if (news == null)
        {
            throw new ServiceException("新闻不存在");
        }
        mmap.put("news", news);
        return PREFIX + "/edit";
    }

    /**
     * 修改新闻。
     *
     * @param news 新闻信息
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:news:edit")
    @Log(title = "车友会新闻", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(McNews news)
    {
        return toAjax(mcContentService.updateMcNews(news, getLoginName()));
    }

    /**
     * 删除新闻。
     *
     * @param ids 主键串
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:news:remove")
    @Log(title = "车友会新闻", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(mcContentService.deleteMcNewsByIds(ids));
    }
}
