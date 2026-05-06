package com.ruoyi.motorclub.domain.vo;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 小程序用户标准输出 VO
 * <p>
 * 用于小程序 app API 返回当前用户信息，统一派生 {@code isMember}/{@code isAdmin}/
 * {@code isSuperAdmin}/{@code adminPermissions} 字段，避免前端各处自行翻译；
 * 同时屏蔽 openid、unionid、idNumberCipher 等敏感字段。
 * </p>
 * <p>
 * 派生字段使用 {@link Boolean} 包装类型并通过 {@link JSONField} 显式锁定输出键名为
 * {@code isMember}/{@code isAdmin}/{@code isSuperAdmin}，以规避 fastjson v1
 * 对 {@code boolean isXxx} 风格字段名剥离 {@code is} 前缀的默认行为。
 * </p>
 *
 * @author AI.Coding
 */
@Data
public class McUserVo
{
    /** 用户主键。 */
    private Long userId;

    /** 昵称。 */
    private String nickName;

    /** 头像地址。 */
    private String avatarUrl;

    /** 手机号。 */
    private String phone;

    /** 真实姓名。 */
    private String realName;

    /** 证件号脱敏值。 */
    private String idNumberMasked;

    /** 车型。 */
    private String carModel;

    /** 会员卡号。 */
    private String memberCardNo;

    /** 入会时间。 */
    private Date joinTime;

    /** 用户状态。 */
    private String status;

    /** 最近登录 IP。 */
    private String lastLoginIp;

    /** 最近登录时间。 */
    private Date lastLoginTime;

    /** 是否会员（memberStatus == "member"）。 */
    @JSONField(name = "isMember")
    private Boolean isMember;

    /** 是否管理员（appAdminType IN {"admin","super_admin"}）。 */
    @JSONField(name = "isAdmin")
    private Boolean isAdmin;

    /** 是否超级管理员（appAdminType == "super_admin"）。 */
    @JSONField(name = "isSuperAdmin")
    private Boolean isSuperAdmin;

    /** 标准化的四类业务权限位。 */
    private McAdminPermissionsVo adminPermissions;
}
