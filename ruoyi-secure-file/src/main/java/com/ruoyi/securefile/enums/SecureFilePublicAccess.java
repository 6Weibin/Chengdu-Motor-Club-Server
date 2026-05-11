package com.ruoyi.securefile.enums;

import java.util.Objects;

/**
 * 安全文件公开访问标识
 *
 * @author AI.Coding
 */
public enum SecureFilePublicAccess
{
    /** 允许门户免登录访问。 */
    YES("Y"),

    /** 不允许门户免登录访问。 */
    NO("N");

    /** 数据库存储编码。 */
    private final String code;

    /**
     * 创建公开访问标识。
     *
     * @param code 标识编码
     */
    SecureFilePublicAccess(String code)
    {
        this.code = code;
    }

    /**
     * 获取公开访问编码。
     *
     * @return 公开访问编码
     */
    public String getCode()
    {
        return code;
    }

    /**
     * 判断是否公开访问。
     *
     * @param value 数据库存储值
     * @return true 表示公开
     */
    public static boolean isPublic(String value)
    {
        return Objects.equals(YES.code, value);
    }

    /**
     * 归一化公开访问参数。
     *
     * @param value 前端提交值
     * @return 数据库存储值
     */
    public static String normalize(String value)
    {
        // 修复点：公开访问只能写入 Y/N，避免前端传入任意字符串影响门户访问判断。
        return Objects.equals(YES.code, value) ? YES.code : NO.code;
    }
}
