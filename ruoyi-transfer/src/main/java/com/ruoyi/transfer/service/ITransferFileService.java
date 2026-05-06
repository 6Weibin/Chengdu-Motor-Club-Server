package com.ruoyi.transfer.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.transfer.domain.TransferFileInfo;
import com.ruoyi.transfer.domain.TransferStoredFile;

/**
 * 在线文件传输服务接口。
 *
 * <p>by AI.Coding</p>
 *
 * @author AI.Coding
 */
public interface ITransferFileService
{
    /**
     * 保存上传文件。
     *
     * @param file 上传文件
     * @return 文件展示信息
     * @throws IOException 文件保存异常
     */
    TransferFileInfo saveFile(MultipartFile file) throws IOException;

    /**
     * 查询当前可下载文件列表。
     *
     * @return 文件列表
     * @throws IOException 文件读取异常
     */
    List<TransferFileInfo> listFiles() throws IOException;

    /**
     * 按文件键加载下载实体。
     *
     * @param key 文件键
     * @return 下载实体
     * @throws IOException 文件读取异常
     */
    TransferStoredFile loadFile(String key) throws IOException;

    /**
     * 删除指定文件。
     *
     * @param key 文件键
     * @throws IOException 删除异常
     */
    void deleteFile(String key) throws IOException;
}
