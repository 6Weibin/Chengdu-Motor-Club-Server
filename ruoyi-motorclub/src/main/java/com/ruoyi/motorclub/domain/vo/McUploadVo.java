package com.ruoyi.motorclub.domain.vo;

import lombok.Data;

/**
 * 小程序上传结果视图
 *
 * @author AI.Coding
 */
@Data
public class McUploadVo
{
    /** 可访问地址。 */
    private String url;

    /** 资源相对路径。 */
    private String fileName;

    /** 新文件名。 */
    private String newFileName;

    /** 原始文件名。 */
    private String originalFilename;
}
