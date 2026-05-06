package com.ruoyi.transfer.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import com.ruoyi.transfer.config.TransferProperties;
import com.ruoyi.transfer.domain.TransferFileInfo;
import com.ruoyi.transfer.domain.TransferStoredFile;

/**
 * 本地文件传输服务单元测试。
 *
 * <p>by AI.Coding</p>
 *
 * @author AI.Coding
 */
public class LocalTransferFileServiceImplTest
{
    /** 临时测试目录 */
    private Path tempDirectory;

    /** 被测服务 */
    private LocalTransferFileServiceImpl transferFileService;

    /**
     * 初始化测试环境。
     *
     * @throws IOException 临时目录创建异常
     */
    @BeforeEach
    public void setUp() throws IOException
    {
        tempDirectory = Files.createTempDirectory("ruoyi-transfer-test");
        TransferProperties transferProperties = new TransferProperties();
        transferProperties.setStorageRoot(tempDirectory.toString());
        transferFileService = new LocalTransferFileServiceImpl(transferProperties);
        transferFileService.initStorageDirectory();
    }

    /**
     * 清理测试环境。
     *
     * @throws IOException 临时目录删除异常
     */
    @AfterEach
    public void tearDown() throws IOException
    {
        // 测试结束后主动清理临时目录，避免污染本地环境。
        org.apache.commons.io.FileUtils.deleteDirectory(new File(tempDirectory.toString()));
    }

    /**
     * 验证上传后可以查询并读取原始文件名。
     *
     * @throws IOException 文件操作异常
     */
    @Test
    public void shouldSaveAndListFileSuccessfully() throws IOException
    {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "手机照片.jpg", "image/jpeg",
                "mock-content".getBytes(StandardCharsets.UTF_8));

        TransferFileInfo savedFile = transferFileService.saveFile(multipartFile);
        List<TransferFileInfo> fileInfos = transferFileService.listFiles();
        TransferStoredFile storedFile = transferFileService.loadFile(savedFile.getKey());

        // 同时验证保存、列表与下载元数据，确保页面主流程可用。
        Assertions.assertEquals("手机照片.jpg", savedFile.getName());
        Assertions.assertEquals(1, fileInfos.size());
        Assertions.assertEquals("手机照片.jpg", fileInfos.get(0).getName());
        Assertions.assertEquals("手机照片.jpg", storedFile.getOriginalFilename());
        Assertions.assertTrue(Files.exists(storedFile.getFilePath()));
    }

    /**
     * 验证删除后文件无法再次读取。
     *
     * @throws IOException 文件操作异常
     */
    @Test
    public void shouldDeleteFileSuccessfully() throws IOException
    {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "delete-me.txt", "text/plain",
                "delete".getBytes(StandardCharsets.UTF_8));

        TransferFileInfo savedFile = transferFileService.saveFile(multipartFile);

        // 删除后再次加载应明确抛出文件不存在异常。
        transferFileService.deleteFile(savedFile.getKey());
        Assertions.assertThrows(NoSuchFileException.class, () -> transferFileService.loadFile(savedFile.getKey()));
    }
}
