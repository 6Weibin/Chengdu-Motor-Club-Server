package com.ruoyi.motorclub.controller.app;

import java.io.File;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.service.IMcUserService;
import com.ruoyi.motorclub.util.McMediaUtils;

/**
 * 小程序媒体访问接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/media")
public class McMediaAppController extends McAppBaseController
{
    @Resource
    private IMcUserService mcUserService;

    /**
     * 公开访问非敏感图片资源。
     *
     * @param path 媒体路径
     * @param response 响应对象
     * @throws IOException 文件读取异常
     */
    @Anonymous
    @GetMapping("/public")
    public void publicMedia(@RequestParam("path") String path, HttpServletResponse response) throws IOException
    {
        String normalizedPath = McMediaUtils.normalizeStoredUrl(path);
        // 修复点：公开媒体接口必须显式排除人员头像，避免通过公共图片地址绕过登录保护。
        if (!McMediaUtils.isInternalProfilePath(normalizedPath) || mcUserService.existsAvatarPath(normalizedPath))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        writeMedia(normalizedPath, response);
    }

    /**
     * 登录后访问人员头像资源。
     *
     * @param path 头像路径
     * @param response 响应对象
     * @throws IOException 文件读取异常
     */
    @GetMapping("/avatar")
    public void avatarMedia(@RequestParam("path") String path, HttpServletResponse response) throws IOException
    {
        if (getAuthenticatedUser() == null)
        {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String normalizedPath = McMediaUtils.normalizeStoredUrl(path);
        if (!McMediaUtils.isInternalProfilePath(normalizedPath) || !mcUserService.existsAvatarPath(normalizedPath))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        writeMedia(normalizedPath, response);
    }

    /**
     * 获取当前登录的小程序用户；媒体下载场景使用标准 HTTP 401 语义，而不是 AjaxResult。
     *
     * @return 当前登录用户，不存在时返回 null
     */
    private McUser getAuthenticatedUser()
    {
        String token = getToken();
        if (StringUtils.isBlank(token))
        {
            return null;
        }
        return mcUserService.selectMcUserByToken(token);
    }

    /**
     * 将媒体文件写入响应。
     *
     * @param path 媒体路径
     * @param response 响应对象
     * @throws IOException 文件读取异常
     */
    private void writeMedia(String path, HttpServletResponse response) throws IOException
    {
        File mediaFile = McMediaUtils.resolveProfileFile(path);
        if (!mediaFile.isFile())
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String contentType = getRequest().getServletContext().getMimeType(mediaFile.getName());
        response.setContentType(StringUtils.defaultIfBlank(contentType, "application/octet-stream"));
        response.setHeader("Cache-Control", "private, max-age=300");
        FileUtils.writeBytes(mediaFile.getAbsolutePath(), response.getOutputStream());
    }
}
