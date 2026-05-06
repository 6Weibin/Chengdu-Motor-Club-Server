package com.ruoyi.motorclub.domain.vo;

import lombok.Data;

/**
 * 车友会系统配置对象
 *
 * @author AI.Coding
 */
@Data
public class McSystemSettingVo
{
    /** 是否允许用户编辑资料。 */
    private Boolean allowProfileEdit;

    /** 入会是否需要审核。 */
    private Boolean joinNeedReview;

    /** 入会是否要求完整实名资料。 */
    private Boolean joinRequireDetailedProfile;
}
