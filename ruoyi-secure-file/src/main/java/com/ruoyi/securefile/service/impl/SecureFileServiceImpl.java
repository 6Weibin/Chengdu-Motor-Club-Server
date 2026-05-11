package com.ruoyi.securefile.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.securefile.crypto.SecureFileCryptoMetadata;
import com.ruoyi.securefile.crypto.SecureFileCryptoResult;
import com.ruoyi.securefile.crypto.SecureFileCryptoService;
import com.ruoyi.securefile.domain.SecureFile;
import com.ruoyi.securefile.dto.SecureFileQuery;
import com.ruoyi.securefile.dto.SecureFileUploadRequest;
import com.ruoyi.securefile.enums.SecureFilePublicAccess;
import com.ruoyi.securefile.enums.SecureFileStatus;
import com.ruoyi.securefile.mapper.SecureFileMapper;
import com.ruoyi.securefile.service.ISecureFileService;
import com.ruoyi.securefile.storage.SecureFileStorageService;
import com.ruoyi.securefile.vo.SecureFileDetailVo;
import com.ruoyi.securefile.vo.SecureFileUploadVo;

/**
 * 安全文件服务实现
 *
 * @author AI.Coding
 */
@Service
public class SecureFileServiceImpl implements ISecureFileService
{
    /** 默认业务类型。 */
    private static final String DEFAULT_BIZ_TYPE = "default";

    /** 默认 MIME 类型。 */
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    @Resource
    private SecureFileMapper secureFileMapper;

    @Resource
    private SecureFileCryptoService secureFileCryptoService;

    @Resource
    private SecureFileStorageService secureFileStorageService;

    @Resource
    private ServerConfig serverConfig;

    /**
     * 上传安全文件。
     *
     * @param file 上传文件
     * @param request 上传业务参数
     * @param operator 操作人
     * @return 上传结果
     * @throws IOException 上传失败时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SecureFileUploadVo upload(MultipartFile file, SecureFileUploadRequest request, String operator) throws IOException
    {
        validateUploadFile(file);
        String extension = FileUploadUtils.getExtension(file).toLowerCase();
        String storagePath = secureFileStorageService.createStoragePath();
        String storedName = secureFileStorageService.createStoredName(extension);
        File cipherFile = secureFileStorageService.resolveCipherFile(storagePath, storedName);
        try
        {
            FileUploadUtils.assertAllowed(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
            SecureFileCryptoResult cryptoResult;
            try (FileOutputStream output = new FileOutputStream(cipherFile))
            {
                // 修复点：不调用 FileUploadUtils.upload，避免先写明文文件再二次处理。
                cryptoResult = secureFileCryptoService.encrypt(file.getInputStream(), output);
            }
            SecureFile secureFile = buildSecureFile(file, request, extension, storagePath, storedName, cryptoResult, operator);
            secureFileMapper.insertSecureFile(secureFile);
            return buildUploadVo(secureFile);
        }
        catch (Exception e)
        {
            secureFileStorageService.deleteQuietly(storagePath, storedName);
            if (e instanceof IOException)
            {
                throw (IOException) e;
            }
            throw new IOException("安全文件上传失败", e);
        }
    }

    /**
     * 查询安全文件列表。
     *
     * @param query 查询条件
     * @return 文件列表
     */
    @Override
    public List<SecureFile> selectSecureFileList(SecureFileQuery query)
    {
        return secureFileMapper.selectSecureFileList(query);
    }

    /**
     * 查询安全文件详情。
     *
     * @param fileId 文件 ID
     * @return 文件详情
     */
    @Override
    public SecureFileDetailVo selectSecureFileDetail(Long fileId)
    {
        SecureFile secureFile = secureFileMapper.selectSecureFileById(fileId);
        if (Objects.isNull(secureFile))
        {
            throw new ServiceException("文件不存在");
        }
        return buildDetailVo(secureFile);
    }

    /**
     * 写出管理端下载响应。
     *
     * @param fileId 文件 ID
     * @param response 响应对象
     * @throws IOException 文件读取或解密失败时抛出
     */
    @Override
    public void writeAdminDownload(Long fileId, HttpServletResponse response) throws IOException
    {
        SecureFile secureFile = secureFileMapper.selectSecureFileById(fileId);
        if (!isNormalFile(secureFile))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        writeFile(secureFile, response, true);
    }

    /**
     * 写出门户图片查看响应。
     *
     * @param fileId 文件 ID
     * @param response 响应对象
     * @throws IOException 文件读取或解密失败时抛出
     */
    @Override
    public void writePortalView(Long fileId, HttpServletResponse response) throws IOException
    {
        SecureFile secureFile = secureFileMapper.selectSecureFileById(fileId);
        if (!isPublicNormalFile(secureFile))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (!StringUtils.startsWithIgnoreCase(secureFile.getContentType(), "image/"))
        {
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }
        writeFile(secureFile, response, false);
    }

    /**
     * 写出门户下载响应。
     *
     * @param fileId 文件 ID
     * @param response 响应对象
     * @throws IOException 文件读取或解密失败时抛出
     */
    @Override
    public void writePortalDownload(Long fileId, HttpServletResponse response) throws IOException
    {
        SecureFile secureFile = secureFileMapper.selectSecureFileById(fileId);
        if (!isPublicNormalFile(secureFile))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        writeFile(secureFile, response, true);
    }

    /**
     * 更新文件状态。
     *
     * @param fileId 文件 ID
     * @param status 目标状态
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int updateStatus(Long fileId, String status, String operator)
    {
        SecureFile secureFile = new SecureFile();
        secureFile.setFileId(fileId);
        secureFile.setStatus(status);
        secureFile.setUpdateBy(operator);
        secureFile.setUpdateTime(DateUtils.getNowDate());
        return secureFileMapper.updateSecureFileStatus(secureFile);
    }

    /**
     * 批量逻辑删除文件。
     *
     * @param ids 文件 ID 字符串
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    public int deleteByIds(String ids, String operator)
    {
        int rows = 0;
        for (Long fileId : Convert.toLongArray(ids))
        {
            rows += updateStatus(fileId, SecureFileStatus.DELETED.getCode(), operator);
        }
        return rows;
    }

    /**
     * 校验上传文件。
     *
     * @param file 上传文件
     */
    private void validateUploadFile(MultipartFile file)
    {
        if (Objects.isNull(file) || file.isEmpty())
        {
            throw new ServiceException("上传文件不能为空");
        }
        if (StringUtils.isBlank(file.getOriginalFilename()))
        {
            throw new ServiceException("上传文件名不能为空");
        }
    }

    /**
     * 构建安全文件记录。
     *
     * @param file 上传文件
     * @param request 上传参数
     * @param extension 文件后缀
     * @param storagePath 内部存储路径
     * @param storedName 密文文件名
     * @param cryptoResult 加密结果
     * @param operator 操作人
     * @return 文件记录
     */
    private SecureFile buildSecureFile(MultipartFile file, SecureFileUploadRequest request, String extension,
            String storagePath, String storedName, SecureFileCryptoResult cryptoResult, String operator)
    {
        Date now = DateUtils.getNowDate();
        SecureFile secureFile = new SecureFile();
        secureFile.setOriginalName(FilenameUtils.getName(file.getOriginalFilename()));
        secureFile.setFileExt(extension);
        secureFile.setFileSize(file.getSize());
        secureFile.setContentType(StringUtils.defaultIfBlank(file.getContentType(), DEFAULT_CONTENT_TYPE));
        secureFile.setBizType(resolveBizType(request));
        secureFile.setPublicAccess(resolvePublicAccess(request));
        secureFile.setStoredName(storedName);
        secureFile.setStoragePath(storagePath);
        secureFile.setSha256(cryptoResult.getSha256());
        secureFile.setEncryptAlg(cryptoResult.getAlgorithm());
        secureFile.setEncryptIv(cryptoResult.getIv());
        secureFile.setStatus(SecureFileStatus.NORMAL.getCode());
        secureFile.setCreateBy(operator);
        secureFile.setCreateTime(now);
        secureFile.setUpdateBy(operator);
        secureFile.setUpdateTime(now);
        return secureFile;
    }

    /**
     * 解析业务类型。
     *
     * @param request 上传参数
     * @return 业务类型
     */
    private String resolveBizType(SecureFileUploadRequest request)
    {
        if (Objects.isNull(request) || StringUtils.isBlank(request.getBizType()))
        {
            return DEFAULT_BIZ_TYPE;
        }
        return request.getBizType().trim();
    }

    /**
     * 解析公开访问标识。
     *
     * @param request 上传参数
     * @return 公开访问标识
     */
    private String resolvePublicAccess(SecureFileUploadRequest request)
    {
        if (Objects.isNull(request))
        {
            return SecureFilePublicAccess.NO.getCode();
        }
        return SecureFilePublicAccess.normalize(request.getPublicAccess());
    }

    /**
     * 构建上传结果。
     *
     * @param secureFile 文件记录
     * @return 上传结果
     */
    private SecureFileUploadVo buildUploadVo(SecureFile secureFile)
    {
        SecureFileUploadVo uploadVo = new SecureFileUploadVo();
        uploadVo.setFileId(secureFile.getFileId());
        uploadVo.setFileName(secureFile.getOriginalName());
        uploadVo.setFileExt(secureFile.getFileExt());
        uploadVo.setFileSize(secureFile.getFileSize());
        uploadVo.setContentType(secureFile.getContentType());
        uploadVo.setPublicAccess(secureFile.getPublicAccess());
        uploadVo.setViewUrl(buildPortalUrl(secureFile.getFileId(), "view"));
        uploadVo.setDownloadUrl(buildPortalUrl(secureFile.getFileId(), "download"));
        return uploadVo;
    }

    /**
     * 构建详情结果。
     *
     * @param secureFile 文件记录
     * @return 文件详情
     */
    private SecureFileDetailVo buildDetailVo(SecureFile secureFile)
    {
        SecureFileDetailVo detailVo = new SecureFileDetailVo();
        SecureFileUploadVo uploadVo = buildUploadVo(secureFile);
        detailVo.setFileId(uploadVo.getFileId());
        detailVo.setFileName(uploadVo.getFileName());
        detailVo.setFileExt(uploadVo.getFileExt());
        detailVo.setFileSize(uploadVo.getFileSize());
        detailVo.setContentType(uploadVo.getContentType());
        detailVo.setPublicAccess(uploadVo.getPublicAccess());
        detailVo.setViewUrl(uploadVo.getViewUrl());
        detailVo.setDownloadUrl(uploadVo.getDownloadUrl());
        detailVo.setBizType(secureFile.getBizType());
        detailVo.setStatus(secureFile.getStatus());
        detailVo.setCreateBy(secureFile.getCreateBy());
        detailVo.setCreateTime(secureFile.getCreateTime());
        return detailVo;
    }

    /**
     * 构建门户访问 URL。
     *
     * @param fileId 文件 ID
     * @param action 访问动作
     * @return 访问 URL
     */
    private String buildPortalUrl(Long fileId, String action)
    {
        return serverConfig.getUrl() + "/portalApi/file/" + fileId + "/" + action;
    }

    /**
     * 判断是否为正常文件。
     *
     * @param secureFile 文件记录
     * @return true 表示正常
     */
    private boolean isNormalFile(SecureFile secureFile)
    {
        return Objects.nonNull(secureFile) && Objects.equals(SecureFileStatus.NORMAL.getCode(), secureFile.getStatus());
    }

    /**
     * 判断是否为门户可访问的公开正常文件。
     *
     * @param secureFile 文件记录
     * @return true 表示门户可访问
     */
    private boolean isPublicNormalFile(SecureFile secureFile)
    {
        return isNormalFile(secureFile) && SecureFilePublicAccess.isPublic(secureFile.getPublicAccess());
    }

    /**
     * 解密并写出文件响应。
     *
     * @param secureFile 文件记录
     * @param response 响应对象
     * @param attachment 是否下载附件
     * @throws IOException 读取或解密失败时抛出
     */
    private void writeFile(SecureFile secureFile, HttpServletResponse response, boolean attachment) throws IOException
    {
        File cipherFile = secureFileStorageService.resolveCipherFile(secureFile.getStoragePath(), secureFile.getStoredName());
        if (!cipherFile.isFile())
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType(StringUtils.defaultIfBlank(secureFile.getContentType(), DEFAULT_CONTENT_TYPE));
        response.setContentLengthLong(secureFile.getFileSize());
        response.setHeader("Cache-Control", attachment ? "private, max-age=300" : "public, max-age=300");
        if (attachment)
        {
            FileUtils.setAttachmentResponseHeader(response, secureFile.getOriginalName());
        }
        SecureFileCryptoMetadata metadata = new SecureFileCryptoMetadata();
        metadata.setAlgorithm(secureFile.getEncryptAlg());
        metadata.setIv(secureFile.getEncryptIv());
        try (FileInputStream input = new FileInputStream(cipherFile))
        {
            // 修复点：所有对外访问都实时解密输出，禁止把密文路径或密文内容直接返回给客户端。
            secureFileCryptoService.decrypt(input, response.getOutputStream(), metadata);
        }
    }
}
