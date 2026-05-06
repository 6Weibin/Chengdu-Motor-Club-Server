package com.ruoyi.motorclub.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.motorclub.constant.McConstants;
import com.ruoyi.motorclub.domain.McBanner;
import com.ruoyi.motorclub.domain.McBenefit;
import com.ruoyi.motorclub.domain.vo.McSystemSettingVo;
import com.ruoyi.motorclub.mapper.McBannerMapper;
import com.ruoyi.motorclub.mapper.McBenefitMapper;
import com.ruoyi.motorclub.service.IMcContentService;
import com.ruoyi.motorclub.util.McMediaUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 内容与系统配置服务实现
 *
 * @author AI.Coding
 */
@Service
public class McContentServiceImpl implements IMcContentService
{
    @Resource
    private McBannerMapper mcBannerMapper;

    @Resource
    private McBenefitMapper mcBenefitMapper;

    @Resource
    private ISysConfigService sysConfigService;

    /**
     * 查询 Banner 列表。
     *
     * @param banner 查询条件
     * @return Banner 列表
     */
    @Override
    public List<McBanner> selectMcBannerList(McBanner banner)
    {
        return mcBannerMapper.selectMcBannerList(banner);
    }

    /**
     * 查询启用 Banner 列表。
     *
     * @return Banner 列表
     */
    @Override
    public List<McBanner> selectEnabledBannerList()
    {
        return mcBannerMapper.selectEnabledMcBannerList();
    }

    /**
     * 通过主键查询 Banner。
     *
     * @param bannerId Banner 主键
     * @return Banner 信息
     */
    @Override
    public McBanner selectMcBannerById(Long bannerId)
    {
        return mcBannerMapper.selectMcBannerById(bannerId);
    }

    /**
     * 新增 Banner。
     *
     * @param banner Banner 信息
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int insertMcBanner(McBanner banner, String operator)
    {
        banner.setImageUrl(McMediaUtils.normalizeStoredUrl(banner.getImageUrl()));
        banner.setCreateBy(operator);
        banner.setCreateTime(DateUtils.getNowDate());
        banner.setUpdateBy(operator);
        banner.setUpdateTime(DateUtils.getNowDate());
        return mcBannerMapper.insertMcBanner(banner);
    }

    /**
     * 修改 Banner。
     *
     * @param banner Banner 信息
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int updateMcBanner(McBanner banner, String operator)
    {
        banner.setImageUrl(McMediaUtils.normalizeStoredUrl(banner.getImageUrl()));
        banner.setUpdateBy(operator);
        banner.setUpdateTime(DateUtils.getNowDate());
        return mcBannerMapper.updateMcBanner(banner);
    }

    /**
     * 删除 Banner。
     *
     * @param ids 主键串
     * @return 影响行数
     */
    @Override
    public int deleteMcBannerByIds(String ids)
    {
        return mcBannerMapper.deleteMcBannerByIds(Convert.toLongArray(ids));
    }

    /**
     * 查询权益列表。
     *
     * @param benefit 查询条件
     * @return 权益列表
     */
    @Override
    public List<McBenefit> selectMcBenefitList(McBenefit benefit)
    {
        return mcBenefitMapper.selectMcBenefitList(benefit);
    }

    /**
     * 查询启用权益列表。
     *
     * @return 权益列表
     */
    @Override
    public List<McBenefit> selectEnabledBenefitList()
    {
        return mcBenefitMapper.selectEnabledMcBenefitList();
    }

    /**
     * 通过主键查询权益。
     *
     * @param benefitId 权益主键
     * @return 权益信息
     */
    @Override
    public McBenefit selectMcBenefitById(Long benefitId)
    {
        return mcBenefitMapper.selectMcBenefitById(benefitId);
    }

    /**
     * 新增权益。
     *
     * @param benefit 权益信息
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int insertMcBenefit(McBenefit benefit, String operator)
    {
        benefit.setImageUrl(McMediaUtils.normalizeStoredUrl(benefit.getImageUrl()));
        benefit.setCreateBy(operator);
        benefit.setCreateTime(DateUtils.getNowDate());
        benefit.setUpdateBy(operator);
        benefit.setUpdateTime(DateUtils.getNowDate());
        return mcBenefitMapper.insertMcBenefit(benefit);
    }

    /**
     * 修改权益。
     *
     * @param benefit 权益信息
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int updateMcBenefit(McBenefit benefit, String operator)
    {
        benefit.setImageUrl(McMediaUtils.normalizeStoredUrl(benefit.getImageUrl()));
        benefit.setUpdateBy(operator);
        benefit.setUpdateTime(DateUtils.getNowDate());
        return mcBenefitMapper.updateMcBenefit(benefit);
    }

    /**
     * 删除权益。
     *
     * @param ids 主键串
     * @return 影响行数
     */
    @Override
    public int deleteMcBenefitByIds(String ids)
    {
        return mcBenefitMapper.deleteMcBenefitByIds(Convert.toLongArray(ids));
    }

    /**
     * 查询系统配置。
     *
     * @return 系统配置
     */
    @Override
    public McSystemSettingVo getSystemSettings()
    {
        McSystemSettingVo settingVo = new McSystemSettingVo();
        settingVo.setAllowProfileEdit(Boolean.valueOf(sysConfigService.selectConfigByKey(McConstants.CONFIG_ALLOW_PROFILE_EDIT)));
        settingVo.setJoinNeedReview(Boolean.valueOf(sysConfigService.selectConfigByKey(McConstants.CONFIG_JOIN_NEED_REVIEW)));
        settingVo.setJoinRequireDetailedProfile(Boolean.valueOf(sysConfigService.selectConfigByKey(McConstants.CONFIG_JOIN_REQUIRE_DETAIL)));
        return settingVo;
    }

    /**
     * 保存系统配置。
     *
     * @param settingVo 配置内容
     * @param operator 操作人
     */
    @Override
    public void saveSystemSettings(McSystemSettingVo settingVo, String operator)
    {
        saveConfig(McConstants.CONFIG_ALLOW_PROFILE_EDIT, "车友会-是否允许用户编辑资料", String.valueOf(settingVo.getAllowProfileEdit()), operator);
        saveConfig(McConstants.CONFIG_JOIN_NEED_REVIEW, "车友会-入会是否需要审核", String.valueOf(settingVo.getJoinNeedReview()), operator);
        saveConfig(McConstants.CONFIG_JOIN_REQUIRE_DETAIL, "车友会-入会是否要求完整实名资料", String.valueOf(settingVo.getJoinRequireDetailedProfile()), operator);
    }

    /**
     * 保存单个系统配置。
     *
     * @param key 配置键
     * @param name 配置名称
     * @param value 配置值
     * @param operator 操作人
     */
    private void saveConfig(String key, String name, String value, String operator)
    {
        SysConfig query = new SysConfig();
        query.setConfigKey(key);
        SysConfig config = null;
        List<SysConfig> configList = sysConfigService.selectConfigList(query);
        if (!configList.isEmpty())
        {
            config = configList.get(0);
        }
        if (config == null)
        {
            config = new SysConfig();
            config.setConfigName(name);
            config.setConfigKey(key);
            config.setConfigValue(value);
            config.setConfigType("N");
            config.setCreateBy(operator);
            sysConfigService.insertConfig(config);
        }
        else
        {
            config.setConfigValue(value);
            config.setUpdateBy(operator);
            sysConfigService.updateConfig(config);
        }
    }
}
