package com.ruoyi.securefile.enums;

/**
 * 安全文件状态枚举
 *
 * @author AI.Coding
 */
public enum SecureFileStatus
{
    /** 正常可访问。 */
    NORMAL("0"),

    /** 已禁用，不允许门户访问。 */
    DISABLED("1"),

    /** 已删除，保留记录用于审计。 */
    DELETED("2");

    /** 数据库存储编码。 */
    private final String code;

    /**
     * 创建文件状态。
     *
     * @param code 状态编码
     */
    SecureFileStatus(String code)
    {
        this.code = code;
    }

    /**
     * 获取状态编码。
     *
     * @return 状态编码
     */
    public String getCode()
    {
        return code;
    }
}
