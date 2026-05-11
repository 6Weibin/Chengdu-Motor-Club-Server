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
    CANCELED("canceled"),
    DELETED("deleted");

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

    /**
     * 判断状态是否允许作为已删除活动的恢复目标。
     *
     * @param code 状态编码
     * @return true 表示可以恢复到该状态
     */
    public static boolean isRecoverableStatus(String code)
    {
        // 业务规则：恢复时只能回到正常业务状态，不能恢复为 deleted 或未知状态。
        return UPCOMING.getCode().equals(code)
                || ONGOING.getCode().equals(code)
                || COMPLETED.getCode().equals(code)
                || CANCELED.getCode().equals(code);
    }
}
