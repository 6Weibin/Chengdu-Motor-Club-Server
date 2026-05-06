package com.ruoyi.motorclub.domain.vo;

import lombok.Data;

/**
 * 微信会话换取结果
 *
 * @author AI.Coding
 */
@Data
public class McWechatSessionVo
{
    /** 微信 openid。 */
    private String openid;

    /** 微信 unionid，可能为空。 */
    private String unionid;

    /** 微信会话密钥（仅运行时持有，不落库）。 */
    private String sessionKey;
}
