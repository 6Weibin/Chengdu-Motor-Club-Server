package com.ruoyi.motorclub.domain;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动对象 mc_activity
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McActivity extends BaseEntity
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

    /** 修复点：小程序活动表单提交的是 yyyy-MM-dd，活动日期字段需要按“日期”而不是“日期时间”解析。 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    /** 活动日期。 */
    private Date activityDate;

    /** 开始时间。 */
    private String startTime;

    /** 结束时间。 */
    private String endTime;

    /** 活动地点。 */
    private String location;

    /** 活动人数上限。 */
    private Integer capacity;

    /** 当前参与人数。 */
    private Integer currentParticipants;

    /** 活动状态。 */
    private String status;
}
