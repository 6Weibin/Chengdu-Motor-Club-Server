package com.ruoyi.motorclub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

/**
 * 微信小程序登录配置
 *
 * @author AI.Coding
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "motorclub.wechat")
public class McWechatProperties
{
    /** 小程序 appId。 */
    private String appId;

    /** 小程序 secret。 */
    private String secret;

    /** 微信会话换取地址。 */
    private String jscode2sessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
}
