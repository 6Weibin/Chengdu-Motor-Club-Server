package com.ruoyi.motorclub.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.motorclub.domain.McUserToken;

/**
 * 小程序用户 Token 数据层
 *
 * @author AI.Coding
 */
public interface McUserTokenMapper
{
    /**
     * 通过 Token 查询记录。
     *
     * @param tokenValue Token 值
     * @return Token 记录
     */
    McUserToken selectMcUserTokenByValue(@Param("tokenValue") String tokenValue);

    /**
     * 查询用户 Token 列表。
     *
     * @param userId 用户主键
     * @return Token 列表
     */
    List<McUserToken> selectMcUserTokenListByUserId(@Param("userId") Long userId);

    /**
     * 新增 Token。
     *
     * @param token Token 记录
     * @return 影响行数
     */
    int insertMcUserToken(McUserToken token);

    /**
     * 修改 Token。
     *
     * @param token Token 记录
     * @return 影响行数
     */
    int updateMcUserToken(McUserToken token);

    /**
     * 按用户失效 Token。
     *
     * @param userId 用户主键
     * @return 影响行数
     */
    int expireMcUserTokenByUserId(@Param("userId") Long userId);
}
