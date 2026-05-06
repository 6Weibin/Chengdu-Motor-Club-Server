package com.ruoyi.motorclub.service;

import java.util.List;
import com.ruoyi.motorclub.domain.McBanner;
import com.ruoyi.motorclub.domain.McBenefit;
import com.ruoyi.motorclub.domain.vo.McSystemSettingVo;

/**
 * 内容与系统配置服务接口
 *
 * @author AI.Coding
 */
public interface IMcContentService
{
    /**
     * 查询 Banner 列表。
     *
     * @param banner 查询条件
     * @return Banner 列表
     */
    List<McBanner> selectMcBannerList(McBanner banner);

    /**
     * 查询启用 Banner 列表。
     *
     * @return Banner 列表
     */
    List<McBanner> selectEnabledBannerList();

    /**
     * 通过主键查询 Banner。
     *
     * @param bannerId Banner 主键
     * @return Banner 信息
     */
    McBanner selectMcBannerById(Long bannerId);

    /**
     * 新增 Banner。
     *
     * @param banner Banner 信息
     * @param operator 操作人
     * @return 影响行数
     */
    int insertMcBanner(McBanner banner, String operator);

    /**
     * 修改 Banner。
     *
     * @param banner Banner 信息
     * @param operator 操作人
     * @return 影响行数
     */
    int updateMcBanner(McBanner banner, String operator);

    /**
     * 删除 Banner。
     *
     * @param ids 主键串
     * @return 影响行数
     */
    int deleteMcBannerByIds(String ids);

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
    List<McBenefit> selectEnabledBenefitList();

    /**
     * 通过主键查询权益。
     *
     * @param benefitId 权益主键
     * @return 权益信息
     */
    McBenefit selectMcBenefitById(Long benefitId);

    /**
     * 新增权益。
     *
     * @param benefit 权益信息
     * @param operator 操作人
     * @return 影响行数
     */
    int insertMcBenefit(McBenefit benefit, String operator);

    /**
     * 修改权益。
     *
     * @param benefit 权益信息
     * @param operator 操作人
     * @return 影响行数
     */
    int updateMcBenefit(McBenefit benefit, String operator);

    /**
     * 删除权益。
     *
     * @param ids 主键串
     * @return 影响行数
     */
    int deleteMcBenefitByIds(String ids);

    /**
     * 查询系统配置。
     *
     * @return 系统配置
     */
    McSystemSettingVo getSystemSettings();

    /**
     * 保存系统配置。
     *
     * @param settingVo 配置内容
     * @param operator 操作人
     */
    void saveSystemSettings(McSystemSettingVo settingVo, String operator);
}
