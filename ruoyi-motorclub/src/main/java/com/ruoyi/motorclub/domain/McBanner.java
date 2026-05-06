package com.ruoyi.motorclub.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Banner 对象 mc_banner
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McBanner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Banner 主键。 */
    private Long bannerId;

    /** 标题。 */
    private String title;

    /** 图片地址。 */
    private String imageUrl;

    /** 跳转链接。 */
    private String linkUrl;

    /** 排序值。 */
    private Integer sort;

    /** 状态。 */
    private String status;
}
