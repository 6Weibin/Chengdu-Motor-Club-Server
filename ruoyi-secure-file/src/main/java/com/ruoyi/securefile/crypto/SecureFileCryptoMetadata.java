package com.ruoyi.securefile.crypto;

import lombok.Data;

/**
 * 安全文件加解密元数据
 *
 * @author AI.Coding
 */
@Data
public class SecureFileCryptoMetadata
{
    /** 加密算法。 */
    private String algorithm;

    /** Base64 编码 IV。 */
    private String iv;
}
