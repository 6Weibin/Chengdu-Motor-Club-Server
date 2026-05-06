package com.ruoyi.motorclub.domain;

import java.util.Date;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动签到码对象 mc_activity_checkin_code
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McActivityCheckinCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 签到码主键。 */
    private Long codeId;

    /** 活动主键。 */
    private Long activityId;

    /** 签到令牌。 */
    private String token;

    /** 二维码原始值。 */
    private String codeValue;

    /** 生成时间。 */
    private Date generatedTime;

    /** 生成人。 */
    private String generatedBy;
}
