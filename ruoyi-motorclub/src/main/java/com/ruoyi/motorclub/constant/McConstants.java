package com.ruoyi.motorclub.constant;

/**
 * 车友会模块常量
 *
 * <p>集中定义配置键、认证头和业务常量，避免在业务代码中散落硬编码。</p>
 *
 * @author AI.Coding
 */
public final class McConstants
{
    /** 资料编辑开关键。 */
    public static final String CONFIG_ALLOW_PROFILE_EDIT = "motorclub.allow-profile-edit";

    /** 入会审核开关键。 */
    public static final String CONFIG_JOIN_NEED_REVIEW = "motorclub.join-need-review";

    /** 入会实名资料开关键。 */
    public static final String CONFIG_JOIN_REQUIRE_DETAIL = "motorclub.join-require-detailed-profile";

    /** 小程序 token 请求头。 */
    public static final String APP_TOKEN_HEADER = "X-Mc-Token";

    /** 小程序 Bearer Token 请求头。 */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /** 业务权限成员管理键。 */
    public static final String PERMISSION_MEMBER_MANAGE = "memberManage";

    /** 业务权限活动管理键。 */
    public static final String PERMISSION_ACTIVITY_MANAGE = "activityManage";

    /** 业务权限 Banner 管理键。 */
    public static final String PERMISSION_BANNER_MANAGE = "bannerManage";

    /** 业务权限权益管理键。 */
    public static final String PERMISSION_BENEFIT_MANAGE = "benefitManage";

    /** 签到码前缀。 */
    public static final String CHECKIN_CODE_PREFIX = "CDMC_ACTIVITY_CHECKIN";

    /**
     * 私有构造方法。
     *
     * <p>工具常量类不允许外部实例化。</p>
     */
    private McConstants()
    {
    }
}
