package com.ruoyi.securefile.service;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.securefile.domain.SecureFile;
import com.ruoyi.securefile.dto.SecureFileQuery;
import com.ruoyi.securefile.dto.SecureFileUploadRequest;
import com.ruoyi.securefile.vo.SecureFileDetailVo;
import com.ruoyi.securefile.vo.SecureFileUploadVo;

/**
 * 安全文件服务接口
 *
 * @author AI.Coding
 */
public interface ISecureFileService
{
    /**
     * 上传安全文件。
     *
     * @param file 上传文件
     * @param request 上传业务参数
     * @param operator 操作人
     * @return 上传结果
     * @throws IOException 上传失败时抛出
     */
    SecureFileUploadVo upload(MultipartFile file, SecureFileUploadRequest request, String operator) throws IOException;

    /**
     * 查询安全文件列表。
     *
     * @param query 查询条件
     * @return 文件列表
     */
    List<SecureFile> selectSecureFileList(SecureFileQuery query);

    /**
     * 查询安全文件详情。
     *
     * @param fileId 文件 ID
     * @return 文件详情
     */
    SecureFileDetailVo selectSecureFileDetail(Long fileId);

    /**
     * 写出管理端下载响应。
     *
     * @param fileId 文件 ID
     * @param response 响应对象
     * @throws IOException 文件读取或解密失败时抛出
     */
    void writeAdminDownload(Long fileId, HttpServletResponse response) throws IOException;

    /**
     * 写出门户图片查看响应。
     *
     * @param fileId 文件 ID
     * @param response 响应对象
     * @throws IOException 文件读取或解密失败时抛出
     */
    void writePortalView(Long fileId, HttpServletResponse response) throws IOException;

    /**
     * 写出门户下载响应。
     *
     * @param fileId 文件 ID
     * @param response 响应对象
     * @throws IOException 文件读取或解密失败时抛出
     */
    void writePortalDownload(Long fileId, HttpServletResponse response) throws IOException;

    /**
     * 更新文件状态。
     *
     * @param fileId 文件 ID
     * @param status 目标状态
     * @param operator 操作人
     * @return 影响行数
     */
    int updateStatus(Long fileId, String status, String operator);

    /**
     * 批量逻辑删除文件。
     *
     * @param ids 文件 ID 字符串
     * @param operator 操作人
     * @return 影响行数
     */
    int deleteByIds(String ids, String operator);
}
