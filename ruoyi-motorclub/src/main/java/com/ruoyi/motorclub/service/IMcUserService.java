package com.ruoyi.motorclub.service;

import java.util.List;
import com.ruoyi.motorclub.domain.McMembershipApplication;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.dto.McAdminPermissionBody;
import com.ruoyi.motorclub.domain.dto.McLoginBody;
import com.ruoyi.motorclub.domain.dto.McMembershipApplicationBody;
import com.ruoyi.motorclub.domain.dto.McProfileUpdateBody;
import com.ruoyi.motorclub.domain.dto.McReviewBody;
import com.ruoyi.motorclub.domain.vo.McLoginVo;
import com.ruoyi.motorclub.domain.vo.McUserVo;

/**
 * 用户与会员业务服务接口
 *
 * @author AI.Coding
 */
public interface IMcUserService
{
    /**
     * 查询用户列表。
     *
     * @param user 查询条件
     * @return 用户列表
     */
    List<McUser> selectMcUserList(McUser user);

    /**
     * 通过主键查询用户。
     *
     * @param userId 用户主键
     * @return 用户信息
     */
    McUser selectMcUserById(Long userId);

    /**
     * 查询面向小程序的标准用户信息。
     *
     * @param userId 用户主键
     * @return 小程序标准用户信息
     */
    McUserVo selectMcUserVoById(Long userId);

    /**
     * 通过 token 查询登录用户。
     *
     * @param token token 值
     * @return 用户信息
     */
    McUser selectMcUserByToken(String token);

    /**
     * 判断指定路径是否被用户头像引用。
     *
     * @param avatarUrl 头像路径
     * @return true 表示存在引用
     */
    boolean existsAvatarPath(String avatarUrl);

    /**
     * 小程序登录（基于微信 code 换取身份）。
     *
     * @param loginBody 登录请求
     * @param loginIp 登录 IP
     * @return 登录结果
     */
    McLoginVo login(McLoginBody loginBody, String loginIp);

    /**
     * 更新用户资料。
     *
     * @param userId 用户主键
     * @param updateBody 更新内容
     * @return 更新后的用户
     */
    McUser updateProfile(Long userId, McProfileUpdateBody updateBody);

    /**
     * 停用用户。
     *
     * @param userId 用户主键
     * @param operator 操作人
     * @return 影响行数
     */
    int disableUser(Long userId, String operator);

    /**
     * 用户注销账号。
     *
     * @param userId 用户主键
     * @return 影响行数
     */
    int cancelUser(Long userId);

    /**
     * 提交入会申请。
     *
     * @param userId 用户主键
     * @param body 申请内容
     * @return 入会申请
     */
    McMembershipApplication submitMembershipApplication(Long userId, McMembershipApplicationBody body);

    /**
     * 查询用户最新入会申请。
     *
     * @param userId 用户主键
     * @return 入会申请
     */
    McMembershipApplication selectLatestMembershipApplication(Long userId);

    /**
     * 查询指定入会申请。
     *
     * @param applicationId 申请主键
     * @return 入会申请
     */
    McMembershipApplication selectMembershipApplicationById(Long applicationId);

    /**
     * 查询入会申请列表。
     *
     * @param application 查询条件
     * @return 入会申请列表
     */
    List<McMembershipApplication> selectMembershipApplicationList(McMembershipApplication application);

    /**
     * 审核入会申请。
     *
     * @param applicationId 申请主键
     * @param body 审核内容
     * @param operator 操作人
     * @return 影响行数
     */
    int reviewMembershipApplication(Long applicationId, McReviewBody body, String operator);

    /**
     * 保存业务管理员权限。
     *
     * @param body 权限内容
     * @param operator 操作人
     * @return 影响行数
     */
    int saveAdminPermission(McAdminPermissionBody body, String operator);

    /**
     * 保存后台用户信息。
     *
     * @param user 用户信息
     * @param operator 操作人
     * @return 影响行数
     */
    int updateMcUser(McUser user, String operator);
}
