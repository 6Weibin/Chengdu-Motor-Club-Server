package com.ruoyi.securefile.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安全文件详情视图
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SecureFileDetailVo extends SecureFileUploadVo
{
    /** 业务类型。 */
    private String bizType;

    /** 文件状态。 */
    private String status;

    /** 上传人。 */
    private String createBy;

    /** 上传时间。 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
