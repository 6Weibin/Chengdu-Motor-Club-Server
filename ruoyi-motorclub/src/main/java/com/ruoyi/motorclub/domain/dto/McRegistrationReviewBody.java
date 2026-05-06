package com.ruoyi.motorclub.domain.dto;

import lombok.Data;

/**
 * 报名审核请求
 *
 * @author AI.Coding
 */
@Data
public class McRegistrationReviewBody
{
    /** 审核结果。 */
    private String status;

    /** 驳回原因。 */
    private String rejectReason;
}
