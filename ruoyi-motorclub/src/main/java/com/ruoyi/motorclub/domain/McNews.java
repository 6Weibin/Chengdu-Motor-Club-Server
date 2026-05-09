package com.ruoyi.motorclub.domain;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 新闻对象 mc_news
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McNews extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 新闻主键。 */
    private Long newsId;

    /** 新闻标题。 */
    private String title;

    /** 门户展示标签。 */
    private String tagName;

    /** 新闻摘要。 */
    private String summary;

    /** 封面图地址。 */
    private String coverImageUrl;

    /** 富文本正文。 */
    private String content;

    /** 发布时间。 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date publishTime;

    /** 状态（0上线 1下线）。 */
    private String status;
}
