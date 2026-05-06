package com.ruoyi.motorclub.exception;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 小程序参数错误异常处理器
 *
 * @author AI.Coding
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class McBadRequestExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(McBadRequestExceptionHandler.class);

    /** body 中 code 字段使用的 400 状态码 */
    private static final int CODE_BAD_REQUEST = 400;

    /**
     * 处理表单绑定异常。
     *
     * @param e 绑定异常
     * @param request 当前请求
     * @return 错误响应
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e, HttpServletRequest request)
    {
        return badRequest(request, e.getAllErrors().get(0).getDefaultMessage(), e);
    }

    /**
     * 处理 JSON 请求体验证异常。
     *
     * @param e 验证异常
     * @param request 当前请求
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request)
    {
        return badRequest(request, e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), e);
    }

    /**
     * 处理缺少请求参数异常。
     *
     * @param e 参数缺失异常
     * @param request 当前请求
     * @return 错误响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public AjaxResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e,
                                                                    HttpServletRequest request)
    {
        return badRequest(request, "缺少必要参数:" + e.getParameterName(), e);
    }

    /**
     * 处理 JSON 反序列化异常。
     *
     * @param e 反序列化异常
     * @param request 当前请求
     * @return 错误响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public AjaxResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request)
    {
        return badRequest(request, "请求体格式不正确", e);
    }

    /**
     * 构造 400 错误响应。
     *
     * @param request 当前请求
     * @param message 错误文案
     * @param e 原始异常
     * @return 错误响应
     */
    private AjaxResult badRequest(HttpServletRequest request, String message, Exception e)
    {
        // 统一记录参数异常来源，便于前后端联调时快速定位报文问题。
        log.warn("请求地址'{}',参数错误:{}", request.getRequestURI(), message, e);
        AjaxResult result = AjaxResult.error(message);
        result.put(AjaxResult.CODE_TAG, CODE_BAD_REQUEST);
        return result;
    }
}
