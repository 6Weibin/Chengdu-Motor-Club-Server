package com.ruoyi.motorclub.domain.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 入会申请提交请求
 *
 * @author AI.Coding
 */
@Data
public class McMembershipApplicationBody
{
    /** 手机号。 */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /** 真实姓名。 */
    private String realName;

    /** 车型。 */
    private String carModel;

    /** 证件号。 */
    private String idNumber;
}
