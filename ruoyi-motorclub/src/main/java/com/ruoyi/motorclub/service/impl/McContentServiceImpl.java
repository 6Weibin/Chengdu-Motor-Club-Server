package com.ruoyi.motorclub.service.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.motorclub.constant.McConstants;
import com.ruoyi.motorclub.domain.McBanner;
import com.ruoyi.motorclub.domain.McBenefit;
import com.ruoyi.motorclub.domain.McNews;
import com.ruoyi.motorclub.domain.dto.McPortalNewsQuery;
import com.ruoyi.motorclub.domain.vo.McPortalNewsDetailVo;
import com.ruoyi.motorclub.domain.vo.McPortalNewsListVo;
import com.ruoyi.motorclub.domain.vo.McSystemSettingVo;
import com.ruoyi.motorclub.mapper.McBannerMapper;
import com.ruoyi.motorclub.mapper.McBenefitMapper;
import com.ruoyi.motorclub.mapper.McNewsMapper;
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
    /** 新闻逻辑删除状态。 */
    private static final String NEWS_STATUS_DELETED = "deleted";

    /** 新闻上线状态。 */
    private static final String NEWS_STATUS_ONLINE = "0";

    /** 新闻下线状态。 */
    private static final String NEWS_STATUS_OFFLINE = "1";

    @Resource
    private McBannerMapper mcBannerMapper;

    @Resource
    private McBenefitMapper mcBenefitMapper;

    @Resource
    private McNewsMapper mcNewsMapper;

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
     * 查询新闻列表。
     *
     * @param news 查询条件
     * @return 新闻列表
     */
    @Override
    public List<McNews> selectMcNewsList(McNews news)
    {
        return mcNewsMapper.selectMcNewsList(news);
    }

    /**
     * 查询已删除新闻列表。
     *
     * @param news 查询条件
     * @return 已删除新闻列表
     */
    @Override
    public List<McNews> selectDeletedMcNewsList(McNews news)
    {
        return mcNewsMapper.selectDeletedMcNewsList(news);
    }

    /**
     * 通过主键查询新闻。
     *
     * @param newsId 新闻主键
     * @return 新闻信息
     */
    @Override
    public McNews selectMcNewsById(Long newsId)
    {
        return mcNewsMapper.selectMcNewsById(newsId);
    }

    /**
     * 新增新闻。
     *
     * @param news 新闻信息
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int insertMcNews(McNews news, String operator)
    {
        if (news == null)
        {
            throw new ServiceException("新闻内容不能为空");
        }
        // 新闻封面路径沿用现有媒体归一化规则，避免门户拿到混杂格式的地址。
        news.setCoverImageUrl(McMediaUtils.normalizeStoredUrl(news.getCoverImageUrl()));
        validateNews(news);
        Date now = DateUtils.getNowDate();
        news.setCreateBy(operator);
        news.setCreateTime(now);
        news.setUpdateBy(operator);
        news.setUpdateTime(now);
        return mcNewsMapper.insertMcNews(news);
    }

    /**
     * 修改新闻。
     *
     * @param news 新闻信息
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int updateMcNews(McNews news, String operator)
    {
        if (news == null)
        {
            throw new ServiceException("新闻内容不能为空");
        }
        if (news.getNewsId() == null)
        {
            throw new ServiceException("新闻主键不能为空");
        }
        requireNews(news.getNewsId());
        // 更新时同样先标准化封面路径，再统一做字段完整性校验。
        news.setCoverImageUrl(McMediaUtils.normalizeStoredUrl(news.getCoverImageUrl()));
        validateNews(news);
        news.setUpdateBy(operator);
        news.setUpdateTime(DateUtils.getNowDate());
        return mcNewsMapper.updateMcNews(news);
    }

    /**
     * 逻辑删除新闻。
     *
     * @param ids 主键串
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int deleteMcNewsByIds(String ids, String operator)
    {
        Long[] newsIds = Convert.toLongArray(ids);
        int rows = 0;
        for (Long newsId : newsIds)
        {
            McNews oldNews = requireNews(newsId);
            if (NEWS_STATUS_DELETED.equals(oldNews.getStatus()))
            {
                // 重复删除保持幂等，避免管理端重复点击时报错。
                rows++;
                continue;
            }
            McNews news = new McNews();
            news.setNewsId(newsId);
            news.setStatus(NEWS_STATUS_DELETED);
            news.setUpdateBy(operator);
            news.setUpdateTime(DateUtils.getNowDate());
            rows += mcNewsMapper.updateMcNewsStatus(news);
        }
        return rows;
    }

    /**
     * 恢复已删除新闻。
     *
     * @param newsId 新闻主键
     * @param status 恢复后的目标状态
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int restoreMcNews(Long newsId, String status, String operator)
    {
        if (StringUtils.isBlank(status))
        {
            throw new ServiceException("请选择恢复后的新闻状态");
        }
        if (!isRecoverableNewsStatus(status))
        {
            throw new ServiceException("恢复状态不合法");
        }
        McNews oldNews = requireNews(newsId);
        if (!NEWS_STATUS_DELETED.equals(oldNews.getStatus()))
        {
            throw new ServiceException("仅已删除新闻可恢复");
        }
        // 恢复时由管理员明确选择上线或下线，不保存删除前状态。
        McNews news = new McNews();
        news.setNewsId(newsId);
        news.setStatus(status);
        news.setUpdateBy(operator);
        news.setUpdateTime(DateUtils.getNowDate());
        return mcNewsMapper.updateMcNewsStatus(news);
    }

    /**
     * 查询门户新闻列表。
     *
     * @param query 查询条件
     * @return 门户新闻列表
     */
    @Override
    public List<McPortalNewsListVo> selectPortalNewsList(McPortalNewsQuery query)
    {
        return mcNewsMapper.selectPortalNewsList(query);
    }

    /**
     * 查询门户新闻详情。
     *
     * @param newsId 新闻主键
     * @return 门户新闻详情
     */
    @Override
    public McPortalNewsDetailVo selectPortalNewsDetail(Long newsId)
    {
        McPortalNewsDetailVo detailVo = mcNewsMapper.selectPortalNewsDetailById(newsId);
        if (detailVo == null)
        {
            throw new ServiceException("新闻不存在或暂不可访问");
        }
        return detailVo;
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

    /**
     * 校验新闻必填字段。
     *
     * @param news 新闻信息
     */
    private void validateNews(McNews news)
    {
        if (news == null)
        {
            throw new ServiceException("新闻内容不能为空");
        }
        if (StringUtils.isBlank(news.getTitle()))
        {
            throw new ServiceException("新闻标题不能为空");
        }
        if (StringUtils.isBlank(news.getTagName()))
        {
            throw new ServiceException("新闻标签不能为空");
        }
        if (StringUtils.isBlank(news.getSummary()))
        {
            throw new ServiceException("新闻摘要不能为空");
        }
        if (StringUtils.isBlank(news.getCoverImageUrl()))
        {
            throw new ServiceException("封面图不能为空");
        }
        if (StringUtils.isBlank(news.getContent()))
        {
            throw new ServiceException("新闻正文不能为空");
        }
        if (news.getPublishTime() == null)
        {
            throw new ServiceException("发布时间不能为空");
        }
        if (!NEWS_STATUS_ONLINE.equals(news.getStatus()) && !NEWS_STATUS_OFFLINE.equals(news.getStatus()))
        {
            throw new ServiceException("新闻状态不合法");
        }
    }

    /**
     * 判断新闻恢复目标状态是否合法。
     *
     * @param status 新闻状态
     * @return true 表示可恢复
     */
    private boolean isRecoverableNewsStatus(String status)
    {
        // 业务规则：已删除新闻只能恢复为上线或下线状态。
        return NEWS_STATUS_ONLINE.equals(status) || NEWS_STATUS_OFFLINE.equals(status);
    }

    /**
     * 校验新闻是否存在。
     *
     * @param newsId 新闻主键
     * @return 新闻信息
     */
    private McNews requireNews(Long newsId)
    {
        McNews news = mcNewsMapper.selectMcNewsById(newsId);
        if (news == null)
        {
            throw new ServiceException("新闻不存在");
        }
        return news;
    }
}
