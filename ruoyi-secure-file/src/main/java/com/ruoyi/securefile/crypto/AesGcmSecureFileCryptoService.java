package com.ruoyi.securefile.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;

/**
 * AES-GCM 安全文件加解密服务
 *
 * @author AI.Coding
 */
@Service
public class AesGcmSecureFileCryptoService implements SecureFileCryptoService
{
    /** 加密算法标识。 */
    public static final String ALGORITHM = "AES-GCM";

    /** JVM 属性密钥名。 */
    private static final String KEY_PROPERTY = "secure.file.crypto.key";

    /** 环境变量密钥名。 */
    private static final String KEY_ENV = "SECURE_FILE_CRYPTO_KEY";

    /** GCM IV 推荐长度。 */
    private static final int IV_LENGTH = 12;

    /** GCM 认证标签长度。 */
    private static final int TAG_LENGTH_BITS = 128;

    /** 流式处理缓冲区。 */
    private static final int BUFFER_SIZE = 8192;

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * 加密输入流并计算原文摘要。
     *
     * @param input 原始文件输入流
     * @param output 密文输出流
     * @return 加密结果
     * @throws IOException 加密失败时抛出
     */
    @Override
    public SecureFileCryptoResult encrypt(InputStream input, OutputStream output) throws IOException
    {
        try
        {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);
            Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, iv);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // 修复点：加密时按流复制，避免大文件上传时将完整文件载入内存。
            try (CipherOutputStream cipherOutput = new CipherOutputStream(output, cipher))
            {
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = input.read(buffer)) != -1)
                {
                    digest.update(buffer, 0, length);
                    cipherOutput.write(buffer, 0, length);
                }
            }
            SecureFileCryptoResult result = new SecureFileCryptoResult();
            result.setAlgorithm(ALGORITHM);
            result.setIv(Base64.getEncoder().encodeToString(iv));
            result.setSha256(toHex(digest.digest()));
            return result;
        }
        catch (GeneralSecurityException e)
        {
            throw new IOException("文件加密失败", e);
        }
    }

    /**
     * 解密密文输入流。
     *
     * @param input 密文输入流
     * @param output 明文输出流
     * @param metadata 加密元数据
     * @throws IOException 解密失败时抛出
     */
    @Override
    public void decrypt(InputStream input, OutputStream output, SecureFileCryptoMetadata metadata) throws IOException
    {
        try
        {
            byte[] iv = Base64.getDecoder().decode(metadata.getIv());
            Cipher cipher = initCipher(Cipher.DECRYPT_MODE, iv);
            try (CipherInputStream cipherInput = new CipherInputStream(input, cipher))
            {
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = cipherInput.read(buffer)) != -1)
                {
                    output.write(buffer, 0, length);
                }
            }
        }
        catch (GeneralSecurityException | IllegalArgumentException e)
        {
            throw new IOException("文件解密失败", e);
        }
    }

    /**
     * 初始化 AES-GCM Cipher。
     *
     * @param mode 加密或解密模式
     * @param iv 初始化向量
     * @return Cipher 实例
     * @throws GeneralSecurityException 初始化失败时抛出
     * @throws IOException 密钥配置错误时抛出
     */
    private Cipher initCipher(int mode, byte[] iv) throws GeneralSecurityException, IOException
    {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(mode, new SecretKeySpec(loadKey(), "AES"), new GCMParameterSpec(TAG_LENGTH_BITS, iv));
        return cipher;
    }

    /**
     * 加载加密密钥。
     *
     * @return AES 密钥字节
     * @throws IOException 密钥不存在或长度非法时抛出
     */
    private byte[] loadKey() throws IOException
    {
        String configuredKey = System.getProperty(KEY_PROPERTY);
        if (StringUtils.isBlank(configuredKey))
        {
            configuredKey = System.getenv(KEY_ENV);
        }
        if (StringUtils.isBlank(configuredKey))
        {
            throw new IOException("未配置安全文件加密密钥");
        }
        byte[] key = decodeKey(configuredKey.trim());
        if (key.length != 16 && key.length != 24 && key.length != 32)
        {
            throw new IOException("安全文件加密密钥长度必须为 16、24 或 32 字节");
        }
        return key;
    }

    /**
     * 解码密钥配置。
     *
     * @param configuredKey 配置值
     * @return 密钥字节
     */
    private byte[] decodeKey(String configuredKey)
    {
        try
        {
            byte[] decoded = Base64.getDecoder().decode(configuredKey);
            // 修复点：优先支持 Base64 密钥，便于部署时传递不可见字节。
            if (decoded.length == 16 || decoded.length == 24 || decoded.length == 32)
            {
                return decoded;
            }
        }
        catch (IllegalArgumentException ignored)
        {
            // 非 Base64 配置按明文 UTF-8 密钥处理。
        }
        return configuredKey.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 将字节数组转为小写十六进制。
     *
     * @param bytes 字节数组
     * @return 十六进制文本
     */
    private String toHex(byte[] bytes)
    {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte item : bytes)
        {
            builder.append(String.format("%02x", item));
        }
        return builder.toString();
    }
}
