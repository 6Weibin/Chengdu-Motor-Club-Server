package com.ruoyi.motorclub.domain.vo;

import lombok.Data;

/**
 * 入会申请统计视图
 *
 * @author AI.Coding
 */
@Data
public class McApplicationStatsVo
{
    /** 总数。 */
    private int total;

    /** 待审核数量。 */
    private int pending;

    /** 已通过数量。 */
    private int approved;

    /** 已拒绝数量。 */
    private int rejected;
}
