package com.ruoyi.motorclub.mapper;

import java.util.List;
import com.ruoyi.motorclub.domain.McBanner;

/**
 * Banner 数据层
 *
 * @author AI.Coding
 */
public interface McBannerMapper
{
    /**
     * 通过主键查询 Banner。
     *
     * @param bannerId Banner 主键
     * @return Banner 信息
     */
    McBanner selectMcBannerById(Long bannerId);

    /**
     * 查询 Banner 列表。
     *
     * @param banner 查询条件
     * @return Banner 列表
     */
    List<McBanner> selectMcBannerList(McBanner banner);

    /**
     * 查询启用的 Banner 列表。
     *
     * @return Banner 列表
     */
    List<McBanner> selectEnabledMcBannerList();

    /**
     * 新增 Banner。
     *
     * @param banner Banner 信息
     * @return 影响行数
     */
    int insertMcBanner(McBanner banner);

    /**
     * 修改 Banner。
     *
     * @param banner Banner 信息
     * @return 影响行数
     */
    int updateMcBanner(McBanner banner);

    /**
     * 批量删除 Banner。
     *
     * @param bannerIds Banner 主键数组
     * @return 影响行数
     */
    int deleteMcBannerByIds(Long[] bannerIds);
}
