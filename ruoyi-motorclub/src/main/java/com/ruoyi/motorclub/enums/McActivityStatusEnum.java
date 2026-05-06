package com.ruoyi.motorclub.enums;

/**
 * 活动状态枚举
 *
 * @author AI.Coding
 */
public enum McActivityStatusEnum
{
    UPCOMING("upcoming"),
    ONGOING("ongoing"),
    COMPLETED("completed"),
    CANCELED("canceled");

    /** 枚举编码。 */
    private final String code;

    /**
     * 构造活动状态枚举。
     *
     * @param code 枚举编码
     */
    McActivityStatusEnum(String code)
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
