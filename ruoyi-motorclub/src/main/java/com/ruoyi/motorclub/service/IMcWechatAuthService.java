package com.ruoyi.motorclub.service;

import com.ruoyi.motorclub.domain.vo.McWechatSessionVo;

/**
 * 微信小程序鉴权服务接口
 *
 * @author AI.Coding
 */
public interface IMcWechatAuthService
{
    /**
     * 使用微信 code 换取会话身份。
     *
     * @param code 微信登录 code
     * @return 微信会话身份
     */
    McWechatSessionVo exchangeCode(String code);
}
