package com.ruoyi.securefile.dto;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

/**
 * 安全文件查询条件
 *
 * @author AI.Coding
 */
@Data
public class SecureFileQuery
{
    /** 原始文件名。 */
    private String originalName;

    /** 文件后缀。 */
    private String fileExt;

    /** 业务类型。 */
    private String bizType;

    /** 公开访问标识。 */
    private String publicAccess;

    /** 文件状态。 */
    private String status;

    /** 上传开始时间。 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /** 上传结束时间。 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
