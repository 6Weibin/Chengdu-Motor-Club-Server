package com.ruoyi.motorclub.enums;

/**
 * 入会申请状态枚举
 *
 * @author AI.Coding
 */
public enum McApplicationStatusEnum
{
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");

    /** 枚举编码。 */
    private final String code;

    /**
     * 构造入会申请状态枚举。
     *
     * @param code 枚举编码
     */
    McApplicationStatusEnum(String code)
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
