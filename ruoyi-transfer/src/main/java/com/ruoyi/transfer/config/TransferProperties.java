package com.ruoyi.transfer.config;

import java.io.File;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.StringUtils;

/**
 * 在线文件传输模块配置。
 *
 * <p>by AI.Coding</p>
 *
 * @author AI.Coding
 */
@Component
@ConfigurationProperties(prefix = "ruoyi.transfer")
public class TransferProperties
{
    /** 页面标题 */
    private String pageTitle = "在线文件传输";

    /** 绝对存储目录，留空时自动拼接到 profile 目录下 */
    private String storageRoot;

    /** 相对 profile 的子目录 */
    private String storageDir = "transfer";

    /**
     * 获取页面标题。
     *
     * @return 页面标题
     */
    public String getPageTitle()
    {
        return pageTitle;
    }

    /**
     * 设置页面标题。
     *
     * @param pageTitle 页面标题
     */
    public void setPageTitle(String pageTitle)
    {
        this.pageTitle = pageTitle;
    }

    /**
     * 获取绝对存储目录。
     *
     * @return 绝对存储目录
     */
    public String getStorageRoot()
    {
        return storageRoot;
    }

    /**
     * 设置绝对存储目录。
     *
     * @param storageRoot 绝对存储目录
     */
    public void setStorageRoot(String storageRoot)
    {
        this.storageRoot = storageRoot;
    }

    /**
     * 获取相对存储子目录。
     *
     * @return 相对存储子目录
     */
    public String getStorageDir()
    {
        return storageDir;
    }

    /**
     * 设置相对存储子目录。
     *
     * @param storageDir 相对存储子目录
     */
    public void setStorageDir(String storageDir)
    {
        this.storageDir = storageDir;
    }

    /**
     * 解析最终使用的文件存储根目录。
     *
     * @return 解析后的文件存储根目录
     */
    public String resolveStorageRoot()
    {
        // 优先使用显式配置的绝对目录，方便按环境覆写。
        if (StringUtils.isNotEmpty(storageRoot))
        {
            return storageRoot;
        }
        return RuoYiConfig.getProfile() + File.separator + storageDir;
    }
}
