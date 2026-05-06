package com.ruoyi.motorclub.util;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5Utils;

/**
 * 车友会业务数据工具类
 *
 * @author AI.Coding
 */
public final class McDataUtils
{
    /** 会员卡号序列补位长度。 */
    private static final int CARD_NO_LENGTH = 6;

    /** 会员卡号默认起始序列。 */
    private static final AtomicInteger CARD_NO_COUNTER = new AtomicInteger(1);

    /**
     * 私有构造方法。
     *
     * <p>工具类不允许实例化。</p>
     */
    private McDataUtils()
    {
    }

    /**
     * 生成证件号密文。
     *
     * @param idNumber 证件号明文
     * @return 证件号密文
     */
    public static String cipherIdNumber(String idNumber)
    {
        if (StringUtils.isBlank(idNumber))
        {
            return null;
        }
        return Md5Utils.hash(idNumber);
    }

    /**
     * 生成证件号脱敏值。
     *
     * @param idNumber 证件号明文
     * @return 脱敏值
     */
    public static String maskIdNumber(String idNumber)
    {
        if (StringUtils.isBlank(idNumber))
        {
            return null;
        }
        if (idNumber.length() <= 8)
        {
            return idNumber;
        }
        return idNumber.substring(0, 4) + "********" + idNumber.substring(idNumber.length() - 4);
    }

    /**
     * 生成会员卡号。
     *
     * @param joinTime 入会时间
     * @return 会员卡号
     */
    public static synchronized String generateMemberCardNo(Date joinTime)
    {
        Date targetDate = joinTime == null ? new Date() : joinTime;
        String datePart = new java.text.SimpleDateFormat("yyyyMMdd").format(targetDate);
        String sequencePart = String.format("%0" + CARD_NO_LENGTH + "d", CARD_NO_COUNTER.getAndIncrement());
        return "MC" + datePart + sequencePart;
    }
}
