package com.ruoyi.motorclub.domain.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 签到请求对象
 *
 * @author AI.Coding
 */
@Data
public class McCheckinBody
{
    /** 签到令牌。 */
    private String token;

    /** 签到二维码完整内容。 */
    private String codeValue;
}
