package com.ruoyi.motorclub.mapper;

import java.util.List;
import com.ruoyi.motorclub.domain.McNews;
import com.ruoyi.motorclub.domain.dto.McPortalNewsQuery;
import com.ruoyi.motorclub.domain.vo.McPortalNewsDetailVo;
import com.ruoyi.motorclub.domain.vo.McPortalNewsListVo;

/**
 * 新闻数据层
 *
 * @author AI.Coding
 */
public interface McNewsMapper
{
    /**
     * 通过主键查询新闻。
     *
     * @param newsId 新闻主键
     * @return 新闻信息
     */
    McNews selectMcNewsById(Long newsId);

    /**
     * 查询新闻列表。
     *
     * @param news 查询条件
     * @return 新闻列表
     */
    List<McNews> selectMcNewsList(McNews news);

    /**
     * 查询已删除新闻列表。
     *
     * @param news 查询条件
     * @return 已删除新闻列表
     */
    List<McNews> selectDeletedMcNewsList(McNews news);

    /**
     * 新增新闻。
     *
     * @param news 新闻信息
     * @return 影响行数
     */
    int insertMcNews(McNews news);

    /**
     * 修改新闻。
     *
     * @param news 新闻信息
     * @return 影响行数
     */
    int updateMcNews(McNews news);

    /**
     * 更新新闻状态。
     *
     * @param news 新闻状态更新信息
     * @return 影响行数
     */
    int updateMcNewsStatus(McNews news);

    /**
     * 查询门户新闻列表。
     *
     * @param query 查询对象
     * @return 门户新闻列表
     */
    List<McPortalNewsListVo> selectPortalNewsList(McPortalNewsQuery query);

    /**
     * 查询门户新闻详情。
     *
     * @param newsId 新闻主键
     * @return 门户新闻详情
     */
    McPortalNewsDetailVo selectPortalNewsDetailById(Long newsId);
}
