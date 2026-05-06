package com.ruoyi.motorclub.service;

import com.ruoyi.motorclub.domain.McActivityCheckinCode;

/**
 * 签到业务服务接口
 *
 * @author AI.Coding
 */
public interface IMcCheckinService
{
    /**
     * 查询活动签到码。
     *
     * @param activityId 活动主键
     * @return 签到码
     */
    McActivityCheckinCode selectCheckinCodeByActivityId(Long activityId);

    /**
     * 生成或刷新签到码。
     *
     * @param activityId 活动主键
     * @param operator 操作人
     * @return 签到码
     */
    McActivityCheckinCode generateCheckinCode(Long activityId, String operator);

    /**
     * 执行签到。
     *
     * @param userId 用户主键
     * @param token 签到令牌
     * @return 影响行数
     */
    int checkin(Long userId, String token);
}
