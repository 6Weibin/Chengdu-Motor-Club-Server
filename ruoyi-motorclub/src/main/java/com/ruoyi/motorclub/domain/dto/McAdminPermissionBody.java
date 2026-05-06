package com.ruoyi.motorclub.domain.dto;

import lombok.Data;

/**
 * 业务管理员权限保存请求
 *
 * @author AI.Coding
 */
@Data
public class McAdminPermissionBody
{
    /** 用户主键。 */
    private Long userId;

    /** 成员管理权限。 */
    private String memberManage;

    /** 活动管理权限。 */
    private String activityManage;

    /** Banner 管理权限。 */
    private String bannerManage;

    /** 权益管理权限。 */
    private String benefitManage;
}
