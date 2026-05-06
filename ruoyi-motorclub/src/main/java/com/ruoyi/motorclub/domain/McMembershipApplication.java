package com.ruoyi.motorclub.domain;

import java.util.Date;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入会申请对象 mc_membership_application
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McMembershipApplication extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 申请主键。 */
    private Long applicationId;

    /** 用户主键。 */
    private Long userId;

    /** 申请手机号快照。 */
    private String phone;

    /** 申请真实姓名快照。 */
    private String realName;

    /** 申请车型快照。 */
    private String carModel;

    /** 申请证件号脱敏值。 */
    private String idNumberMasked;

    /** 申请状态。 */
    private String status;

    /** 驳回原因。 */
    private String rejectReason;

    /** 审核人。 */
    private String reviewBy;

    /** 审核时间。 */
    private Date reviewTime;

    /** 关联用户信息。 */
    private McUser user;
}
