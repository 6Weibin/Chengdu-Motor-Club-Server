package com.ruoyi.motorclub.controller.app;

import javax.annotation.Resource;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.motorclub.constant.McConstants;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.exception.McUnauthorizedException;
import com.ruoyi.motorclub.service.IMcUserService;

/**
 * 小程序接口控制器基类
 *
 * @author AI.Coding
 */
public class McAppBaseController extends BaseController
{
    @Resource
    private IMcUserService mcUserService;

    /**
     * 获取当前登录的小程序用户。
     * <p>
     * 鉴权失败统一抛 {@link McUnauthorizedException}，由 advice 返回 body code=401，
     * 让前端能稳定识别"登录态失效，需重新登录"的语义。
     * </p>
     *
     * @return 当前用户
     * @throws McUnauthorizedException 未携带 token、token 失效或用户已停用
     */
    protected McUser getRequiredMcUser()
    {
        String token = getToken();
        // 请求未携带 token，视为缺少登录凭证
        if (StringUtils.isBlank(token))
        {
            throw new McUnauthorizedException(McUnauthorizedException.MSG_NO_TOKEN);
        }
        // selectMcUserByToken 内已过滤 token 不存在/过期/用户停用三种情况
        McUser user = mcUserService.selectMcUserByToken(token);
        if (user == null)
        {
            throw new McUnauthorizedException(McUnauthorizedException.MSG_INVALID);
        }
        return user;
    }

    /**
     * 解析请求中的 token。
     *
     * @return token 值
     */
    protected String getToken()
    {
        String token = getRequest().getHeader(McConstants.APP_TOKEN_HEADER);
        if (StringUtils.isBlank(token))
        {
            token = getRequest().getHeader(McConstants.AUTHORIZATION_HEADER);
            if (StringUtils.startsWithIgnoreCase(token, "Bearer "))
            {
                token = StringUtils.substring(token, 7);
            }
        }
        return token;
    }
}
