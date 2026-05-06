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
 * 小程序权限不足异常处理器
 * <p>
 * 独立于 {@code ruoyi-framework} 的 {@code GlobalExceptionHandler}，仅处理 motorclub 模块抛出的
 * {@link McForbiddenException}。返回 body code=403，HTTP 状态码保持 200，符合 RuoYi
 * {@link AjaxResult} 既有约定，便于前端在拦截器中按 body.code 区分"登录失效(401)"与"权限不足(403)"。
 * </p>
 * <p>
 * 通过 {@link Order} 设为最高优先级，确保此 advice 在通用 {@code RuntimeException} 处理器之前匹配；
 * 与 {@link McUnauthorizedExceptionHandler} 捕获不同异常类型，互不冲突。
 * </p>
 *
 * @author AI.Coding
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class McForbiddenExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(McForbiddenExceptionHandler.class);

    /** body 中 code 字段使用的 403 状态码 */
    private static final int CODE_FORBIDDEN = 403;

    /**
     * 捕获小程序权限不足异常并返回 code=403 的 {@link AjaxResult}。
     *
     * @param e       小程序权限不足异常
     * @param request 当前请求，用于日志定位
     * @return 含 403 状态码的 AjaxResult，msg 取自异常文案
     */
    @ExceptionHandler(McForbiddenException.class)
    public AjaxResult handleMcForbiddenException(McForbiddenException e, HttpServletRequest request)
    {
        // 记录访问路径与拒绝原因，便于排查越权访问场景
        log.warn("请求地址'{}',小程序权限不足:{}", request.getRequestURI(), e.getMessage());
        // 复用 AjaxResult.error(msg) 创建实例后，将 code 覆盖为 403
        AjaxResult result = AjaxResult.error(e.getMessage());
        result.put(AjaxResult.CODE_TAG, CODE_FORBIDDEN);
        return result;
    }
}
