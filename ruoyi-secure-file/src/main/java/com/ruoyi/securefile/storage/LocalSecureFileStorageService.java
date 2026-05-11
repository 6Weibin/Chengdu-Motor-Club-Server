package com.ruoyi.securefile.storage;

import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;

/**
 * 本地安全文件存储服务
 *
 * @author AI.Coding
 */
@Service
public class LocalSecureFileStorageService implements SecureFileStorageService
{
    /** 安全文件模块根目录。 */
    private static final String SECURE_FILE_ROOT = "secure-file";

    /** 密文文件后缀。 */
    private static final String CIPHER_SUFFIX = ".bin";

    /**
     * 生成当前上传批次的内部相对目录。
     *
     * @return 相对目录
     */
    @Override
    public String createStoragePath()
    {
        return DateUtils.datePath();
    }

    /**
     * 生成密文文件名。
     *
     * @param extension 原始文件后缀
     * @return 密文文件名
     */
    @Override
    public String createStoredName(String extension)
    {
        String normalizedExtension = StringUtils.defaultIfBlank(extension, "file").toLowerCase();
        // 修复点：物理文件名不保留原始文件名，避免通过磁盘文件推断业务内容。
        return IdUtils.fastSimpleUUID() + "." + normalizedExtension + CIPHER_SUFFIX;
    }

    /**
     * 解析密文文件并确保路径不越界。
     *
     * @param storagePath 内部相对目录
     * @param storedName 密文文件名
     * @return 密文文件
     * @throws IOException 路径非法时抛出
     */
    @Override
    public File resolveCipherFile(String storagePath, String storedName) throws IOException
    {
        File root = getRootDirectory();
        File target = new File(new File(root, StringUtils.stripStart(storagePath, "/")), storedName).getCanonicalFile();
        // 修复点：所有文件访问都从数据库元数据解析，仍需防御异常数据造成的目录穿越。
        if (!target.getPath().startsWith(root.getPath() + File.separator))
        {
            throw new IOException("安全文件路径越界");
        }
        File parent = target.getParentFile();
        if (!parent.exists() && !parent.mkdirs())
        {
            throw new IOException("安全文件目录创建失败");
        }
        return target;
    }

    /**
     * 静默删除密文文件。
     *
     * @param storagePath 内部相对目录
     * @param storedName 密文文件名
     */
    @Override
    public void deleteQuietly(String storagePath, String storedName)
    {
        try
        {
            File file = resolveCipherFile(storagePath, storedName);
            if (file.isFile() && !file.delete())
            {
                file.deleteOnExit();
            }
        }
        catch (IOException ignored)
        {
            // 清理失败不覆盖原始异常，后续由业务日志定位。
        }
    }

    /**
     * 获取安全文件根目录。
     *
     * @return 根目录
     * @throws IOException 路径解析失败时抛出
     */
    private File getRootDirectory() throws IOException
    {
        File root = new File(RuoYiConfig.getProfile(), SECURE_FILE_ROOT).getCanonicalFile();
        if (!root.exists() && !root.mkdirs())
        {
            throw new IOException("安全文件根目录创建失败");
        }
        return root;
    }
}
