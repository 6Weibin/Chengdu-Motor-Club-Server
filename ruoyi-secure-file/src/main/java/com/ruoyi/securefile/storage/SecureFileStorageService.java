package com.ruoyi.securefile.storage;

import java.io.File;
import java.io.IOException;

/**
 * 安全文件存储服务
 *
 * @author AI.Coding
 */
public interface SecureFileStorageService
{
    /**
     * 生成当前上传批次的内部相对目录。
     *
     * @return 相对目录
     */
    String createStoragePath();

    /**
     * 生成密文文件名。
     *
     * @param extension 原始文件后缀
     * @return 密文文件名
     */
    String createStoredName(String extension);

    /**
     * 解析密文文件。
     *
     * @param storagePath 内部相对目录
     * @param storedName 密文文件名
     * @return 密文文件
     * @throws IOException 路径非法时抛出
     */
    File resolveCipherFile(String storagePath, String storedName) throws IOException;

    /**
     * 删除密文文件。
     *
     * @param storagePath 内部相对目录
     * @param storedName 密文文件名
     */
    void deleteQuietly(String storagePath, String storedName);
}
