package com.ruoyi.motorclub.controller.portal;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.motorclub.domain.dto.McPortalNewsQuery;
import com.ruoyi.motorclub.service.IMcContentService;

/**
 * 门户新闻公开接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/portalApi/motorclub/news")
public class McPortalNewsController extends BaseController
{
    @Resource
    private IMcContentService mcContentService;

    /**
     * 查询门户新闻列表。
     *
     * @param query 查询条件
     * @return 新闻列表
     */
    @Anonymous
    @GetMapping("/list")
    public AjaxResult list(McPortalNewsQuery query)
    {
        return success(mcContentService.selectPortalNewsList(query));
    }

    /**
     * 查询门户新闻详情。
     *
     * @param newsId 新闻主键
     * @return 新闻详情
     */
    @Anonymous
    @GetMapping("/{newsId}")
    public AjaxResult detail(@PathVariable("newsId") Long newsId)
    {
        return success(mcContentService.selectPortalNewsDetail(newsId));
    }
}
