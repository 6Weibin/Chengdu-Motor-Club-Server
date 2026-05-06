package com.ruoyi.motorclub.mapper;

import java.util.List;
import com.ruoyi.motorclub.domain.McBenefit;

/**
 * 权益数据层
 *
 * @author AI.Coding
 */
public interface McBenefitMapper
{
    /**
     * 通过主键查询权益。
     *
     * @param benefitId 权益主键
     * @return 权益信息
     */
    McBenefit selectMcBenefitById(Long benefitId);

    /**
     * 查询权益列表。
     *
     * @param benefit 查询条件
     * @return 权益列表
     */
    List<McBenefit> selectMcBenefitList(McBenefit benefit);

    /**
     * 查询启用权益列表。
     *
     * @return 权益列表
     */
    List<McBenefit> selectEnabledMcBenefitList();

    /**
     * 新增权益。
     *
     * @param benefit 权益信息
     * @return 影响行数
     */
    int insertMcBenefit(McBenefit benefit);

    /**
     * 修改权益。
     *
     * @param benefit 权益信息
     * @return 影响行数
     */
    int updateMcBenefit(McBenefit benefit);

    /**
     * 批量删除权益。
     *
     * @param benefitIds 权益主键数组
     * @return 影响行数
     */
    int deleteMcBenefitByIds(Long[] benefitIds);
}
