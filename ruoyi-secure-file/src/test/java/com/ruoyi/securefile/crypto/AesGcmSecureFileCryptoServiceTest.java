package com.ruoyi.securefile.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * AES-GCM 安全文件加解密测试
 *
 * @author AI.Coding
 */
public class AesGcmSecureFileCryptoServiceTest
{
    /** 测试密钥属性名。 */
    private static final String KEY_PROPERTY = "secure.file.crypto.key";

    private AesGcmSecureFileCryptoService cryptoService;

    /**
     * 初始化测试密钥。
     */
    @BeforeEach
    public void setUp()
    {
        System.setProperty(KEY_PROPERTY, "1234567890123456");
        cryptoService = new AesGcmSecureFileCryptoService();
    }

    /**
     * 清理测试密钥。
     */
    @AfterEach
    public void tearDown()
    {
        System.clearProperty(KEY_PROPERTY);
    }

    /**
     * 验证加密结果不是明文且可解密还原。
     *
     * @throws Exception 测试失败时抛出
     */
    @Test
    public void encryptAndDecryptShouldRestorePlainBytes() throws Exception
    {
        byte[] plainBytes = "secure file content".getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream cipherOutput = new ByteArrayOutputStream();

        SecureFileCryptoResult result = cryptoService.encrypt(new ByteArrayInputStream(plainBytes), cipherOutput);

        Assertions.assertEquals(AesGcmSecureFileCryptoService.ALGORITHM, result.getAlgorithm());
        Assertions.assertEquals(64, result.getSha256().length());
        Assertions.assertNotEquals(new String(plainBytes, StandardCharsets.UTF_8),
                new String(cipherOutput.toByteArray(), StandardCharsets.UTF_8), "密文不能等于明文");

        ByteArrayOutputStream plainOutput = new ByteArrayOutputStream();
        cryptoService.decrypt(new ByteArrayInputStream(cipherOutput.toByteArray()), plainOutput, result);

        Assertions.assertArrayEquals(plainBytes, plainOutput.toByteArray());
    }
}
