package com.ruoyi.motorclub.domain.vo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 门户新闻列表视图对象
 *
 * @author AI.Coding
 */
@Data
public class McPortalNewsListVo implements Serializable
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

    /** 发布时间。 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;
}
