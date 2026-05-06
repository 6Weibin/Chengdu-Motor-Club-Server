package com.ruoyi.motorclub.domain;

import java.util.Date;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 小程序用户 token 对象 mc_user_token
 *
 * @author AI.Coding
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class McUserToken extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Token 主键。 */
    private Long tokenId;

    /** 用户主键。 */
    private Long userId;

    /** Token 字符串。 */
    private String tokenValue;

    /** 失效时间。 */
    private Date expiredAt;

    /** 最后访问时间。 */
    private Date lastAccessTime;

    /** 状态。 */
    private String status;
}
