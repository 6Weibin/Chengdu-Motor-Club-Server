package com.ruoyi.motorclub.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员权益对象 mc_benefit
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McBenefit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 权益主键。 */
    private Long benefitId;

    /** 权益标题。 */
    private String title;

    /** 权益摘要。 */
    private String summary;

    /** 权益正文。 */
    private String content;

    /** 图片地址。 */
    private String imageUrl;

    /** 排序值。 */
    private Integer sort;

    /** 状态。 */
    private String status;
}
