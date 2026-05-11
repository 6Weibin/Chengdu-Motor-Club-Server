package com.ruoyi.securefile.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安全文件对象 sf_file
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SecureFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件主键。 */
    private Long fileId;

    /** 原始文件名。 */
    private String originalName;

    /** 文件后缀。 */
    private String fileExt;

    /** 原始文件大小。 */
    private Long fileSize;

    /** MIME 类型。 */
    private String contentType;

    /** 业务类型。 */
    private String bizType;

    /** 是否公开访问。 */
    private String publicAccess;

    /** 重命名后的密文文件名。 */
    private String storedName;

    /** 模块内部相对存储路径。 */
    private String storagePath;

    /** 原始内容 SHA-256 摘要。 */
    private String sha256;

    /** 加密算法。 */
    private String encryptAlg;

    /** 加密初始化向量。 */
    private String encryptIv;

    /** 状态（0正常 1禁用 2删除）。 */
    private String status;
}
