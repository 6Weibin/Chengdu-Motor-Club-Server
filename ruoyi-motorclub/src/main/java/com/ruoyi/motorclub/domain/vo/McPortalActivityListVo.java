package com.ruoyi.motorclub.domain.vo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 门户活动列表视图对象
 *
 * @author AI.Coding
 */
@Data
public class McPortalActivityListVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 活动主键。 */
    private Long activityId;

    /** 活动标题。 */
    private String title;

    /** 活动描述。 */
    private String description;

    /** 封面图片地址。 */
    private String coverImageUrl;

    /** 活动日期。 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date activityDate;

    /** 开始时间。 */
    private String startTime;

    /** 活动地点。 */
    private String location;

    /** 活动人数上限。 */
    private Integer capacity;

    /** 当前参与人数。 */
    private Integer currentParticipants;

    /** 活动状态。 */
    private String status;
}
