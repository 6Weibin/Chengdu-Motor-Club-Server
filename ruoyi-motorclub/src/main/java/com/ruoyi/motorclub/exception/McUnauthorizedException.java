package com.ruoyi.motorclub.exception;

/**
 * 小程序登录失效专用异常
 * <p>
 * 仅用于小程序 app API 场景下"未携带 token / token 已失效 / 用户已停用"等鉴权失败分支，
 * 由 {@link com.ruoyi.motorclub.controller.app.McAppBaseController#getRequiredMcUser()} 抛出。
 * 该异常会被 {@link McUnauthorizedExceptionHandler} 捕获，返回 body code=401，
 * 用于前端识别"登录态失效，需重新登录"的语义；不应用于普通业务校验失败场景。
 * </p>
 *
 * @author AI.Coding
 */
public class McUnauthorizedException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /** 缺少登录凭证（请求未携带 token 头） */
    public static final String MSG_NO_TOKEN = "未获取到登录凭证";

    /** token 失效或对应用户已被停用 */
    public static final String MSG_INVALID = "登录状态已失效，请重新登录";

    /**
     * 使用可读消息构造异常。
     *
     * @param message 失效原因文案，用于直接返回给前端
     */
    public McUnauthorizedException(String message)
    {
        super(message);
    }
}
