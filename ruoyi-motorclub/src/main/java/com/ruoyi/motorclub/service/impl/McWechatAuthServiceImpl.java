package com.ruoyi.motorclub.service.impl;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.motorclub.config.McWechatProperties;
import com.ruoyi.motorclub.domain.vo.McWechatSessionVo;
import com.ruoyi.motorclub.service.IMcWechatAuthService;

/**
 * 微信小程序鉴权服务实现
 *
 * @author AI.Coding
 */
@Service
public class McWechatAuthServiceImpl implements IMcWechatAuthService
{
    private static final Logger log = LoggerFactory.getLogger(McWechatAuthServiceImpl.class);

    @Resource
    private McWechatProperties wechatProperties;

    /**
     * 使用微信 code 换取会话身份。
     *
     * @param code 微信登录 code
     * @return 微信会话身份
     */
    @Override
    public McWechatSessionVo exchangeCode(String code)
    {
        if (StringUtils.isBlank(code))
        {
            throw new ServiceException("微信登录凭证缺失");
        }
        if (StringUtils.isBlank(wechatProperties.getAppId()) || StringUtils.isBlank(wechatProperties.getSecret()))
        {
            log.error("微信小程序登录配置缺失：appId 或 secret 未配置");
            throw new ServiceException("微信登录服务暂不可用");
        }

        String params = String.format("appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
            wechatProperties.getAppId(), wechatProperties.getSecret(), code);

        String response;
        try
        {
            response = HttpUtils.sendGet(wechatProperties.getJscode2sessionUrl(), params);
        }
        catch (Exception e)
        {
            log.error("调用微信 jscode2session 异常", e);
            throw new ServiceException("微信登录失败，请稍后重试");
        }

        if (StringUtils.isBlank(response))
        {
            log.error("微信 jscode2session 返回为空");
            throw new ServiceException("微信登录失败，请稍后重试");
        }

        JSONObject json;
        try
        {
            json = JSONObject.parseObject(response);
        }
        catch (Exception e)
        {
            log.error("解析微信 jscode2session 返回异常: {}", response, e);
            throw new ServiceException("微信登录失败，请稍后重试");
        }

        Integer errcode = json.getInteger("errcode");
        if (errcode != null && errcode != 0)
        {
            log.warn("微信 jscode2session 返回失败: errcode={}, errmsg={}", errcode, json.getString("errmsg"));
            throw new ServiceException("微信登录失败，请重新授权");
        }

        String openid = json.getString("openid");
        if (StringUtils.isBlank(openid))
        {
            log.error("微信 jscode2session 未返回 openid: {}", response);
            throw new ServiceException("微信登录失败，请重新授权");
        }

        McWechatSessionVo vo = new McWechatSessionVo();
        vo.setOpenid(openid);
        vo.setUnionid(json.getString("unionid"));
        vo.setSessionKey(json.getString("session_key"));
        return vo;
    }
}
