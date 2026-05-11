package com.ruoyi.securefile.controller.portal;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.securefile.service.ISecureFileService;

/**
 * 安全文件门户访问控制器
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/portalApi/file")
public class SecureFilePortalController
{
    @Resource
    private ISecureFileService secureFileService;

    /**
     * 门户免登录查看公开图片。
     *
     * @param fileId 文件 ID
     * @param response 响应对象
     * @throws IOException 输出失败时抛出
     */
    @Anonymous
    @GetMapping("/{fileId}/view")
    public void view(@PathVariable("fileId") Long fileId, HttpServletResponse response) throws IOException
    {
        secureFileService.writePortalView(fileId, response);
    }

    /**
     * 门户免登录下载公开文件。
     *
     * @param fileId 文件 ID
     * @param response 响应对象
     * @throws IOException 输出失败时抛出
     */
    @Anonymous
    @GetMapping("/{fileId}/download")
    public void download(@PathVariable("fileId") Long fileId, HttpServletResponse response) throws IOException
    {
        secureFileService.writePortalDownload(fileId, response);
    }
}
