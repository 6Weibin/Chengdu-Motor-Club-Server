package com.ruoyi.motorclub.enums;

/**
 * 会员身份状态枚举
 *
 * @author AI.Coding
 */
public enum McMemberStatusEnum
{
    VISITOR("visitor"),
    MEMBER("member"),
    DISABLED("disabled");

    /** 枚举编码。 */
    private final String code;

    /**
     * 构造会员身份状态枚举。
     *
     * @param code 枚举编码
     */
    McMemberStatusEnum(String code)
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
