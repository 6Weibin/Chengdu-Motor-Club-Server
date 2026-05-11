package com.ruoyi.securefile.vo;

import lombok.Data;

/**
 * 安全文件上传结果
 *
 * @author AI.Coding
 */
@Data
public class SecureFileUploadVo
{
    /** 文件 ID。 */
    private Long fileId;

    /** 原始文件名称。 */
    private String fileName;

    /** 文件后缀。 */
    private String fileExt;

    /** 文件大小。 */
    private Long fileSize;

    /** MIME 类型。 */
    private String contentType;

    /** 公开访问标识。 */
    private String publicAccess;

    /** 图片预览地址。 */
    private String viewUrl;

    /** 文件下载地址。 */
    private String downloadUrl;
}
