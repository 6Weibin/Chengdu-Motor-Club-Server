package com.ruoyi.securefile.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 安全文件加解密服务
 *
 * @author AI.Coding
 */
public interface SecureFileCryptoService
{
    /**
     * 加密输入流并写入输出流。
     *
     * @param input 原始文件输入流
     * @param output 密文输出流
     * @return 加密结果
     * @throws IOException 加密失败时抛出
     */
    SecureFileCryptoResult encrypt(InputStream input, OutputStream output) throws IOException;

    /**
     * 解密输入流并写入输出流。
     *
     * @param input 密文输入流
     * @param output 明文输出流
     * @param metadata 加密元数据
     * @throws IOException 解密失败时抛出
     */
    void decrypt(InputStream input, OutputStream output, SecureFileCryptoMetadata metadata) throws IOException;
}
