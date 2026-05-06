package com.ruoyi.motorclub.domain.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 活动状态更新请求
 *
 * @author AI.Coding
 */
@Data
public class McActivityStatusBody
{
    /** 目标状态。 */
    @NotBlank(message = "活动状态不能为空")
    private String status;
}
