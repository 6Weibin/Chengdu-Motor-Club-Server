package com.ruoyi.securefile.dto;

import lombok.Data;

/**
 * 安全文件上传业务参数
 *
 * @author AI.Coding
 */
@Data
public class SecureFileUploadRequest
{
    /** 业务类型。 */
    private String bizType;

    /** 公开访问标识。 */
    private String publicAccess;
}
