package com.ruoyi.motorclub.exception;

/**
 * 小程序权限不足专用异常
 * <p>
 * 用于"已登录但无权限执行某管理操作"场景，由 {@code McAdminAuthHelper} 在 {@code requireAdmin} /
 * {@code requirePermission} 校验失败时抛出，由 {@link McForbiddenExceptionHandler} 捕获并返回
 * body code=403，前端据此识别"操作受限"语义；不应用于登录失效（见 {@link McUnauthorizedException}）
 * 或普通业务校验失败（继续使用 {@code ServiceException}）。
 * </p>
 *
 * @author AI.Coding
 */
public class McForbiddenException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /** 默认权限不足提示文案。 */
    public static final String DEFAULT_MSG = "无权限执行该操作";

    /**
     * 使用默认提示文案构造异常。
     */
    public McForbiddenException()
    {
        super(DEFAULT_MSG);
    }

    /**
     * 使用自定义提示文案构造异常。
     *
     * @param message 权限不足原因文案，用于直接返回给前端
     */
    public McForbiddenException(String message)
    {
        super(message);
    }
}
