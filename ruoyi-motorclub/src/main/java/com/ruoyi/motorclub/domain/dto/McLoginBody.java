package com.ruoyi.motorclub.domain.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 小程序登录请求对象
 *
 * @author AI.Coding
 */
@Data
public class McLoginBody
{
    /** 微信登录 code。 */
    @NotBlank(message = "微信登录凭证不能为空")
    private String code;

    /** 昵称。 */
    private String nickName;

    /** 头像地址。 */
    private String avatarUrl;

    /** 手机号。 */
    private String phone;
}
