package com.ruoyi.securefile.controller.admin;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.securefile.domain.SecureFile;
import com.ruoyi.securefile.dto.SecureFileQuery;
import com.ruoyi.securefile.dto.SecureFileUploadRequest;
import com.ruoyi.securefile.enums.SecureFileStatus;
import com.ruoyi.securefile.service.ISecureFileService;

/**
 * 安全文件后台控制器
 *
 * @author AI.Coding
 */
@Controller
@RequestMapping("/secure-file/file")
public class SecureFileController extends BaseController
{
    /** 页面前缀。 */
    private static final String PREFIX = "securefile/file";

    @Resource
    private ISecureFileService secureFileService;

    /**
     * 打开文件管理页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("securefile:file:view")
    @GetMapping()
    public String file()
    {
        return PREFIX + "/file";
    }

    /**
     * 查询文件列表。
     *
     * @param query 查询条件
     * @return 分页列表
     */
    @RequiresPermissions("securefile:file:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SecureFileQuery query)
    {
        startPage();
        List<SecureFile> list = secureFileService.selectSecureFileList(query);
        return getDataTable(list);
    }

    /**
     * 上传安全文件。
     *
     * @param file 上传文件
     * @param request 上传参数
     * @return 上传结果
     */
    @RequiresPermissions("securefile:file:upload")
    @Log(title = "安全文件", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult upload(MultipartFile file, SecureFileUploadRequest request)
    {
        try
        {
            return success(secureFileService.upload(file, request, getLoginName()));
        }
        catch (Exception e)
        {
            // 修复点：上传失败只返回业务错误，不把服务器路径或加密细节暴露给前端。
            return error(e.getMessage());
        }
    }

    /**
     * 查询文件详情。
     *
     * @param fileId 文件 ID
     * @return 文件详情
     */
    @RequiresPermissions("securefile:file:list")
    @GetMapping("/detail/{fileId}")
    @ResponseBody
    public AjaxResult detail(@PathVariable("fileId") Long fileId)
    {
        return success(secureFileService.selectSecureFileDetail(fileId));
    }

    /**
     * 下载管理端文件。
     *
     * @param fileId 文件 ID
     * @param response 响应对象
     * @throws IOException 输出失败时抛出
     */
    @RequiresPermissions("securefile:file:download")
    @GetMapping("/download/{fileId}")
    public void download(@PathVariable("fileId") Long fileId, HttpServletResponse response) throws IOException
    {
        secureFileService.writeAdminDownload(fileId, response);
    }

    /**
     * 启用文件。
     *
     * @param fileId 文件 ID
     * @return 操作结果
     */
    @RequiresPermissions("securefile:file:edit")
    @Log(title = "安全文件", businessType = BusinessType.UPDATE)
    @PostMapping("/enable")
    @ResponseBody
    public AjaxResult enable(Long fileId)
    {
        return toAjax(secureFileService.updateStatus(fileId, SecureFileStatus.NORMAL.getCode(), getLoginName()));
    }

    /**
     * 禁用文件。
     *
     * @param fileId 文件 ID
     * @return 操作结果
     */
    @RequiresPermissions("securefile:file:edit")
    @Log(title = "安全文件", businessType = BusinessType.UPDATE)
    @PostMapping("/disable")
    @ResponseBody
    public AjaxResult disable(Long fileId)
    {
        return toAjax(secureFileService.updateStatus(fileId, SecureFileStatus.DISABLED.getCode(), getLoginName()));
    }

    /**
     * 逻辑删除文件。
     *
     * @param ids 文件 ID 串
     * @return 操作结果
     */
    @RequiresPermissions("securefile:file:remove")
    @Log(title = "安全文件", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(secureFileService.deleteByIds(ids, getLoginName()));
    }
}
