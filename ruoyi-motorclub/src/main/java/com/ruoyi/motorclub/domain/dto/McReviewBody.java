package com.ruoyi.motorclub.domain.dto;

import lombok.Data;

/**
 * 通用审核请求
 *
 * @author AI.Coding
 */
@Data
public class McReviewBody
{
    /** 审核结果。 */
    private String status;

    /** 驳回原因。 */
    private String rejectReason;
}
