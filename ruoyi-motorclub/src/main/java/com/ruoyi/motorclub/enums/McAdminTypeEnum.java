package com.ruoyi.motorclub.enums;

/**
 * 小程序业务管理员类型枚举
 *
 * @author AI.Coding
 */
public enum McAdminTypeEnum
{
    NONE("none"),
    ADMIN("admin"),
    SUPER_ADMIN("super_admin");

    /** 枚举编码。 */
    private final String code;

    /**
     * 构造管理员类型枚举。
     *
     * @param code 枚举编码
     */
    McAdminTypeEnum(String code)
    {
        this.code = code;
    }

    /**
     * 获取枚举编码。
     *
     * @return 枚举编码
     */
    public String getCode()
    {
        return code;
    }
}
