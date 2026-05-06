package com.ruoyi.transfer.domain;

import java.nio.file.Path;

/**
 * 传输文件下载实体。
 *
 * <p>by AI.Coding</p>
 *
 * @author AI.Coding
 */
public class TransferStoredFile
{
    /** 文件唯一键 */
    private final String key;

    /** 原始文件名 */
    private final String originalFilename;

    /** 本地路径 */
    private final Path filePath;

    /** 文件大小 */
    private final long size;

    /**
     * 构造传输文件下载实体。
     *
     * @param key 文件唯一键
     * @param originalFilename 原始文件名
     * @param filePath 本地路径
     * @param size 文件大小
     */
    public TransferStoredFile(String key, String originalFilename, Path filePath, long size)
    {
        this.key = key;
        this.originalFilename = originalFilename;
        this.filePath = filePath;
        this.size = size;
    }

    /**
     * 获取文件唯一键。
     *
     * @return 文件唯一键
     */
    public String getKey()
    {
        return key;
    }

    /**
     * 获取原始文件名。
     *
     * @return 原始文件名
     */
    public String getOriginalFilename()
    {
        return originalFilename;
    }

    /**
     * 获取本地路径。
     *
     * @return 本地路径
     */
    public Path getFilePath()
    {
        return filePath;
    }

    /**
     * 获取文件大小。
     *
     * @return 文件大小
     */
    public long getSize()
    {
        return size;
    }
}
