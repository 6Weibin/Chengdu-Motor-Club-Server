package com.ruoyi.motorclub.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.motorclub.domain.McActivityRegistration;

/**
 * 活动报名数据层
 *
 * @author AI.Coding
 */
public interface McActivityRegistrationMapper
{
    /**
     * 通过主键查询报名记录。
     *
     * @param registrationId 报名主键
     * @return 报名记录
     */
    McActivityRegistration selectMcActivityRegistrationById(Long registrationId);

    /**
     * 按活动和用户查询报名记录。
     *
     * @param activityId 活动主键
     * @param userId 用户主键
     * @return 报名记录
     */
    McActivityRegistration selectMcActivityRegistrationByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);

    /**
     * 查询报名记录列表。
     *
     * @param registration 查询条件
     * @return 报名记录列表
     */
    List<McActivityRegistration> selectMcActivityRegistrationList(McActivityRegistration registration);

    /**
     * 查询用户我的活动列表。
     *
     * @param userId 用户主键
     * @return 报名记录列表
     */
    List<McActivityRegistration> selectMyMcActivityRegistrationList(@Param("userId") Long userId);

    /**
     * 新增报名记录。
     *
     * @param registration 报名记录
     * @return 影响行数
     */
    int insertMcActivityRegistration(McActivityRegistration registration);

    /**
     * 修改报名记录。
     *
     * @param registration 报名记录
     * @return 影响行数
     */
    int updateMcActivityRegistration(McActivityRegistration registration);
}
