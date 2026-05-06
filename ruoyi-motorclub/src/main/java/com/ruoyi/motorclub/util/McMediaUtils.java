package com.ruoyi.motorclub.util;

import java.io.File;
import java.io.IOException;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.StringUtils;

/**
 * 小程序媒体资源工具
 *
 * @author AI.Coding
 */
public final class McMediaUtils
{
    /** 小程序内部媒体资源统一使用的静态前缀。 */
    private static final String PROFILE_PREFIX = Constants.RESOURCE_PREFIX + "/";

    /**
     * 工具类禁止实例化。
     */
    private McMediaUtils()
    {
    }

    /**
     * 将媒体地址归一化为可持久化的相对路径。
     *
     * @param rawUrl 原始媒体地址
     * @return 相对路径或原值
     */
    public static String normalizeStoredUrl(String rawUrl)
    {
        if (StringUtils.isBlank(rawUrl))
        {
            return rawUrl;
        }
        String normalized = stripQueryAndHash(rawUrl.trim());
        int resourceIndex = StringUtils.indexOfIgnoreCase(normalized, Constants.RESOURCE_PREFIX + "/");
        if (resourceIndex >= 0)
        {
            return normalized.substring(resourceIndex);
        }
        return normalized;
    }

    /**
     * 判断是否属于系统内部上传资源。
     *
     * @param resourcePath 资源路径
     * @return true 表示内部上传资源
     */
    public static boolean isInternalProfilePath(String resourcePath)
    {
        return StringUtils.isNotBlank(resourcePath) && resourcePath.startsWith(PROFILE_PREFIX);
    }

    /**
     * 将资源相对路径解析为磁盘文件。
     *
     * @param resourcePath 资源路径
     * @return 文件对象
     * @throws IOException 路径非法或越界时抛出异常
     */
    public static File resolveProfileFile(String resourcePath) throws IOException
    {
        String normalizedPath = normalizeStoredUrl(resourcePath);
        if (!isInternalProfilePath(normalizedPath))
        {
            throw new IOException("媒体路径不合法");
        }
        String relativePath = normalizedPath.substring(Constants.RESOURCE_PREFIX.length());
        File profileRoot = new File(RuoYiConfig.getProfile()).getCanonicalFile();
        File targetFile = new File(profileRoot, StringUtils.stripStart(relativePath, "/")).getCanonicalFile();
        // 修复点：媒体代理接口必须防止通过 ../ 等方式越界读取 profile 目录外文件。
        if (!targetFile.getPath().startsWith(profileRoot.getPath() + File.separator) && !targetFile.equals(profileRoot))
        {
            throw new IOException("媒体路径越界");
        }
        return targetFile;
    }

    /**
     * 去掉资源路径中的查询参数与 hash。
     *
     * @param rawUrl 原始地址
     * @return 干净地址
     */
    private static String stripQueryAndHash(String rawUrl)
    {
        int queryIndex = rawUrl.indexOf('?');
        int hashIndex = rawUrl.indexOf('#');
        int cutIndex = -1;
        if (queryIndex >= 0 && hashIndex >= 0)
        {
            cutIndex = Math.min(queryIndex, hashIndex);
        }
        else if (queryIndex >= 0)
        {
            cutIndex = queryIndex;
        }
        else if (hashIndex >= 0)
        {
            cutIndex = hashIndex;
        }
        return cutIndex >= 0 ? rawUrl.substring(0, cutIndex) : rawUrl;
    }
}
