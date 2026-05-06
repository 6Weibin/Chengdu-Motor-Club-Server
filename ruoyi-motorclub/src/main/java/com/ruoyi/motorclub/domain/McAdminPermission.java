package com.ruoyi.motorclub.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务管理员权限对象 mc_admin_permission
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McAdminPermission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 权限主键。 */
    private Long permissionId;

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
