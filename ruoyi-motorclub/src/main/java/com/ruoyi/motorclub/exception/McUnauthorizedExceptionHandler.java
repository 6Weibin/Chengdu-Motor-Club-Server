package com.ruoyi.motorclub.exception;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 小程序登录失效异常处理器
 * <p>
 * 独立于 {@code ruoyi-framework} 的 {@code GlobalExceptionHandler}，仅处理 motorclub 模块抛出的
 * {@link McUnauthorizedException}。返回 body code=401，HTTP 状态码保持 200，符合 RuoYi
 * {@link AjaxResult} 既有约定，便于前端统一拦截"登录态失效"语义。
 * </p>
 * <p>
 * 通过 {@link Order} 设为最高优先级，确保此 advice 在通用 {@code RuntimeException} 处理器之前匹配。
 * </p>
 *
 * @author AI.Coding
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class McUnauthorizedExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(McUnauthorizedExceptionHandler.class);

    /** body 中 code 字段使用的 401 状态码 */
    private static final int CODE_UNAUTHORIZED = 401;

    /**
     * 捕获小程序登录失效异常并返回 code=401 的 {@link AjaxResult}。
     *
     * @param e       小程序登录失效异常
     * @param request 当前请求，用于日志定位
     * @return 含 401 状态码的 AjaxResult，msg 取自异常文案
     */
    @ExceptionHandler(McUnauthorizedException.class)
    public AjaxResult handleMcUnauthorizedException(McUnauthorizedException e, HttpServletRequest request)
    {
        // 记录访问路径与失败原因，便于排查 token 失效场景
        log.warn("请求地址'{}',小程序登录失效:{}", request.getRequestURI(), e.getMessage());
        // 复用 AjaxResult.error(msg) 创建实例后，将 code 覆盖为 401
        AjaxResult result = AjaxResult.error(e.getMessage());
        result.put(AjaxResult.CODE_TAG, CODE_UNAUTHORIZED);
        return result;
    }
}
