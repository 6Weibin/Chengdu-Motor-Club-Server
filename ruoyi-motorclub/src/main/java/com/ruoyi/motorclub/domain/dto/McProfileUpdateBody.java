package com.ruoyi.motorclub.domain.dto;

import lombok.Data;

/**
 * 小程序用户资料更新请求
 *
 * @author AI.Coding
 */
@Data
public class McProfileUpdateBody
{
    /** 昵称。 */
    private String nickName;

    /** 头像地址。 */
    private String avatarUrl;

    /** 手机号。 */
    private String phone;

    /** 真实姓名。 */
    private String realName;

    /** 证件号。 */
    private String idNumber;

    /** 车型。 */
    private String carModel;
}
