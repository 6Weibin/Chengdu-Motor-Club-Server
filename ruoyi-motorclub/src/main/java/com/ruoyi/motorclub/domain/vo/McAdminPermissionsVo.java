package com.ruoyi.motorclub.domain.vo;

import lombok.Data;

/**
 * 小程序管理员权限标准化输出 VO
 * <p>
 * 与前端 {@code admin-permissions.js} 中四个权限键完全对齐，
 * 屏蔽底层 char(1) ('0'/'1') 的存储语义，统一以 boolean 输出。
 * </p>
 *
 * @author AI.Coding
 */
@Data
public class McAdminPermissionsVo
{
    /** 成员管理权限。 */
    private boolean memberManage;

    /** 活动管理权限。 */
    private boolean activityManage;

    /** Banner 管理权限。 */
    private boolean bannerManage;

    /** 权益管理权限。 */
    private boolean benefitManage;
}
