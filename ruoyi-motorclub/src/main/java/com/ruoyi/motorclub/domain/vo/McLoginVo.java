package com.ruoyi.motorclub.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 小程序登录响应对象
 *
 * @author AI.Coding
 */
@Data
public class McLoginVo
{
    /** 登录 token。 */
    private String token;

    /** 过期时间。 */
    private Date expiredAt;

    /** 登录用户标准视图。 */
    private McUserVo user;
}
