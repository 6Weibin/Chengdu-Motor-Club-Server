package com.ruoyi.transfer.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.transfer.config.TransferProperties;
import com.ruoyi.transfer.domain.TransferStoredFile;
import com.ruoyi.transfer.service.ITransferFileService;

/**
 * 在线文件传输控制器。
 *
 * <p>by AI.Coding</p>
 *
 * @author AI.Coding
 */
@Controller
@RequestMapping("/transfer")
public class TransferController
{
    /** 传输服务 */
    private final ITransferFileService transferFileService;

    /** 传输配置 */
    private final TransferProperties transferProperties;

    /**
     * 构造在线文件传输控制器。
     *
     * @param transferFileService 传输服务
     * @param transferProperties 传输配置
     */
    public TransferController(ITransferFileService transferFileService, TransferProperties transferProperties)
    {
        this.transferFileService = transferFileService;
        this.transferProperties = transferProperties;
    }

    /**
     * 打开在线文件传输页面。
     *
     * @param mmap 页面模型
     * @return 模板路径
     */
    @Anonymous
    @GetMapping
    public String index(ModelMap mmap)
    {
        // 页面仅展示必要配置，避免暴露服务端本地存储目录。
        mmap.put("pageTitle", transferProperties.getPageTitle());
        return "transfer/index";
    }

    /**
     * 查询当前文件列表。
     *
     * @return 文件列表结果
     */
    @Anonymous
    @GetMapping("/files")
    @ResponseBody
    public AjaxResult listFiles()
    {
        try
        {
            return AjaxResult.success(transferFileService.listFiles());
        }
        catch (IOException e)
        {
            return AjaxResult.error("读取文件列表失败：" + e.getMessage());
        }
    }

    /**
     * 上传文件。
     *
     * @param file 上传文件
     * @return 上传结果
     */
    @Anonymous
    @PostMapping("/files")
    @ResponseBody
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile file)
    {
        try
        {
            return AjaxResult.success("上传成功", transferFileService.saveFile(file));
        }
        catch (IllegalArgumentException | IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 下载指定文件。
     *
     * @param key 文件键
     * @param response 响应对象
     * @throws IOException 下载异常
     */
    @Anonymous
    @GetMapping("/files/download")
    public void downloadFile(@RequestParam("key") String key, HttpServletResponse response) throws IOException
    {
        try
        {
            TransferStoredFile storedFile = transferFileService.loadFile(key);
            Path filePath = storedFile.getFilePath();

            // 下载统一按二进制流返回，兼容手机浏览器保存文件。
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentLengthLong(storedFile.getSize());
            FileUtils.setAttachmentResponseHeader(response, storedFile.getOriginalFilename());
            Files.copy(filePath, response.getOutputStream());
            response.flushBuffer();
        }
        catch (IllegalArgumentException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        catch (NoSuchFileException e)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件不存在");
        }
    }

    /**
     * 删除指定文件。
     *
     * @param key 文件键
     * @return 删除结果
     */
    @Anonymous
    @DeleteMapping("/files")
    @ResponseBody
    public AjaxResult deleteFile(@RequestParam("key") String key)
    {
        try
        {
            transferFileService.deleteFile(key);
            return AjaxResult.success("删除成功");
        }
        catch (IllegalArgumentException e)
        {
            return AjaxResult.error(e.getMessage());
        }
        catch (NoSuchFileException e)
        {
            return AjaxResult.error("文件不存在");
        }
        catch (IOException e)
        {
            return AjaxResult.error("删除文件失败：" + e.getMessage());
        }
    }
}
