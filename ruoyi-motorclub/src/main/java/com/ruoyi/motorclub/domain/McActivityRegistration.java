package com.ruoyi.motorclub.domain;

import java.util.Date;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动报名对象 mc_activity_registration
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McActivityRegistration extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报名主键。 */
    private Long registrationId;

    /** 活动主键。 */
    private Long activityId;

    /** 用户主键。 */
    private Long userId;

    /** 报名状态。 */
    private String status;

    /** 驳回原因。 */
    private String rejectReason;

    /** 是否已签到。 */
    private String checkedIn;

    /** 签到时间。 */
    private Date checkedInAt;

    /** 审核人。 */
    private String reviewBy;

    /** 审核时间。 */
    private Date reviewTime;

    /** 关联活动标题。 */
    private String activityTitle;

    /** 关联用户昵称。 */
    private String userNickName;

    /** 关联用户实名。 */
    private String userRealName;
}
