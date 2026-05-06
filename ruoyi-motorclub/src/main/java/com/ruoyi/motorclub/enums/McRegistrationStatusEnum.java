package com.ruoyi.motorclub.enums;

/**
 * 活动报名状态枚举
 *
 * @author AI.Coding
 */
public enum McRegistrationStatusEnum
{
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");

    /** 枚举编码。 */
    private final String code;

    /**
     * 构造活动报名状态枚举。
     *
     * @param code 枚举编码
     */
    McRegistrationStatusEnum(String code)
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
