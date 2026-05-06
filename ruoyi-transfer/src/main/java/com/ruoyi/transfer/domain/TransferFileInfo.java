package com.ruoyi.transfer.domain;

/**
 * 传输文件展示信息。
 *
 * <p>by AI.Coding</p>
 *
 * @author AI.Coding
 */
public class TransferFileInfo
{
    /** 文件唯一键 */
    private final String key;

    /** 原始文件名 */
    private final String name;

    /** 文件大小 */
    private final long size;

    /** 上传时间 */
    private final long uploadTime;

    /** 下载地址 */
    private final String downloadUrl;

    /**
     * 构造文件展示信息。
     *
     * @param key 文件唯一键
     * @param name 原始文件名
     * @param size 文件大小
     * @param uploadTime 上传时间
     * @param downloadUrl 下载地址
     */
    public TransferFileInfo(String key, String name, long size, long uploadTime, String downloadUrl)
    {
        this.key = key;
        this.name = name;
        this.size = size;
        this.uploadTime = uploadTime;
        this.downloadUrl = downloadUrl;
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
    public String getName()
    {
        return name;
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

    /**
     * 获取上传时间。
     *
     * @return 上传时间
     */
    public long getUploadTime()
    {
        return uploadTime;
    }

    /**
     * 获取下载地址。
     *
     * @return 下载地址
     */
    public String getDownloadUrl()
    {
        return downloadUrl;
    }
}
