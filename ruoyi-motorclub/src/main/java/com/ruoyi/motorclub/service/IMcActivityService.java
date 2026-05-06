package com.ruoyi.motorclub.service;

import java.util.List;
import com.ruoyi.motorclub.domain.McActivity;
import com.ruoyi.motorclub.domain.McActivityRegistration;
import com.ruoyi.motorclub.domain.dto.McRegistrationReviewBody;

/**
 * 活动与报名业务服务接口
 *
 * @author AI.Coding
 */
public interface IMcActivityService
{
    /**
     * 查询活动列表。
     *
     * @param activity 查询条件
     * @return 活动列表
     */
    List<McActivity> selectMcActivityList(McActivity activity);

    /**
     * 通过主键查询活动。
     *
     * @param activityId 活动主键
     * @return 活动信息
     */
    McActivity selectMcActivityById(Long activityId);

    /**
     * 新增活动。
     *
     * @param activity 活动信息
     * @param operator 操作人
     * @return 影响行数
     */
    int insertMcActivity(McActivity activity, String operator);

    /**
     * 修改活动。
     *
     * @param activity 活动信息
     * @param operator 操作人
     * @return 影响行数
     */
    int updateMcActivity(McActivity activity, String operator);

    /**
     * 删除活动。
     *
     * @param ids 活动主键串
     * @return 影响行数
     */
    int deleteMcActivityByIds(String ids);

    /**
     * 提交活动报名。
     *
     * @param activityId 活动主键
     * @param userId 用户主键
     * @return 报名记录
     */
    McActivityRegistration registerActivity(Long activityId, Long userId);

    /**
     * 查询单条报名记录。
     *
     * @param registrationId 报名主键
     * @return 报名记录
     */
    McActivityRegistration selectRegistrationById(Long registrationId);

    /**
     * 按活动和用户查询报名记录。
     *
     * @param activityId 活动主键
     * @param userId 用户主键
     * @return 报名记录
     */
    McActivityRegistration selectRegistrationByActivityAndUser(Long activityId, Long userId);

    /**
     * 查询报名列表。
     *
     * @param registration 查询条件
     * @return 报名列表
     */
    List<McActivityRegistration> selectRegistrationList(McActivityRegistration registration);

    /**
     * 查询我的活动。
     *
     * @param userId 用户主键
     * @return 报名列表
     */
    List<McActivityRegistration> selectMyRegistrationList(Long userId);

    /**
     * 审核报名。
     *
     * @param registrationId 报名主键
     * @param body 审核内容
     * @param operator 操作人
     * @return 影响行数
     */
    int reviewRegistration(Long registrationId, McRegistrationReviewBody body, String operator);
}
