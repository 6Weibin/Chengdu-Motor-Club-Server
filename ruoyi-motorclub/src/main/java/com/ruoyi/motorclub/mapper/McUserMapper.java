package com.ruoyi.motorclub.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.motorclub.domain.McUser;

/**
 * 小程序用户数据层
 *
 * @author AI.Coding
 */
public interface McUserMapper
{
    /**
     * 通过主键查询用户。
     *
     * @param userId 用户主键
     * @return 用户信息
     */
    McUser selectMcUserById(Long userId);

    /**
     * 通过 openid 查询用户。
     *
     * @param openid 微信 openid
     * @return 用户信息
     */
    McUser selectMcUserByOpenid(@Param("openid") String openid);

    /**
     * 统计指定头像路径的引用数量。
     *
     * @param avatarUrl 头像路径
     * @return 引用数量
     */
    int countMcUserByAvatarUrl(@Param("avatarUrl") String avatarUrl);

    /**
     * 查询用户列表。
     *
     * @param user 用户查询条件
     * @return 用户列表
     */
    List<McUser> selectMcUserList(McUser user);

    /**
     * 新增用户。
     *
     * @param user 用户信息
     * @return 影响行数
     */
    int insertMcUser(McUser user);

    /**
     * 修改用户。
     *
     * @param user 用户信息
     * @return 影响行数
     */
    int updateMcUser(McUser user);

    /**
     * 批量删除用户。
     *
     * @param userIds 用户主键数组
     * @return 影响行数
     */
    int deleteMcUserByIds(Long[] userIds);
}
