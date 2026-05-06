package com.ruoyi.motorclub.domain;

import java.util.Date;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 小程序用户对象 mc_user
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户主键。 */
    private Long userId;

    /** 微信 openid。 */
    private String openid;

    /** 微信 unionid。 */
    private String unionid;

    /** 昵称。 */
    private String nickName;

    /** 头像地址。 */
    private String avatarUrl;

    /** 手机号。 */
    private String phone;

    /** 真实姓名。 */
    private String realName;

    /** 证件号密文。 */
    private String idNumberCipher;

    /** 证件号脱敏值。 */
    private String idNumberMasked;

    /** 车型。 */
    private String carModel;

    /** 会员状态。 */
    private String memberStatus;

    /** 会员卡号。 */
    private String memberCardNo;

    /** 入会时间。 */
    private Date joinTime;

    /** 小程序后台管理员类型。 */
    private String appAdminType;

    /** 用户状态。 */
    private String status;

    /** 最近登录 IP。 */
    private String lastLoginIp;

    /** 最近登录时间。 */
    private Date lastLoginTime;

    /** 关联业务权限。 */
    private McAdminPermission adminPermission;
}
