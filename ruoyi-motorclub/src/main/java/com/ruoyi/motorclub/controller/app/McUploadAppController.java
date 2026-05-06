package com.ruoyi.motorclub.controller.app;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.motorclub.domain.vo.McUploadVo;

/**
 * 小程序通用上传接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/upload")
public class McUploadAppController extends McAppBaseController
{
    /**
     * 上传图片文件。
     *
     * @param file 图片文件
     * @return 上传结果
     * @throws Exception 上传异常
     */
    @PostMapping
    public AjaxResult upload(MultipartFile file) throws Exception
    {
        getRequiredMcUser();
        // 小程序仅使用头像、Banner、活动封面等图片上传场景，这里统一收敛为图片白名单。
        String fileName = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file, MimeTypeUtils.IMAGE_EXTENSION, true);
        McUploadVo uploadVo = new McUploadVo();
        // 修复点：上传结果只返回相对路径，避免媒体地址被持久化为“上传当时的绝对域名”，导致切换域名/IP 后全部失效。
        uploadVo.setUrl(fileName);
        uploadVo.setFileName(fileName);
        uploadVo.setNewFileName(FileUtils.getName(fileName));
        uploadVo.setOriginalFilename(file.getOriginalFilename());
        return success(uploadVo);
    }
}
