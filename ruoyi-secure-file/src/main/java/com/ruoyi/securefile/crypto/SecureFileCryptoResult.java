package com.ruoyi.securefile.crypto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安全文件加密结果
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SecureFileCryptoResult extends SecureFileCryptoMetadata
{
    /** 原始文件 SHA-256 摘要。 */
    private String sha256;
}
