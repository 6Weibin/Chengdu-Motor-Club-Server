package com.ruoyi.motorclub.mapper;

import org.apache.ibatis.annotations.Param;
import com.ruoyi.motorclub.domain.McActivityCheckinCode;

/**
 * 活动签到码数据层
 *
 * @author AI.Coding
 */
public interface McActivityCheckinCodeMapper
{
    /**
     * 按活动查询签到码。
     *
     * @param activityId 活动主键
     * @return 签到码
     */
    McActivityCheckinCode selectMcActivityCheckinCodeByActivityId(@Param("activityId") Long activityId);

    /**
     * 按令牌查询签到码。
     *
     * @param token 令牌
     * @return 签到码
     */
    McActivityCheckinCode selectMcActivityCheckinCodeByToken(@Param("token") String token);

    /**
     * 新增签到码。
     *
     * @param checkinCode 签到码
     * @return 影响行数
     */
    int insertMcActivityCheckinCode(McActivityCheckinCode checkinCode);

    /**
     * 修改签到码。
     *
     * @param checkinCode 签到码
     * @return 影响行数
     */
    int updateMcActivityCheckinCode(McActivityCheckinCode checkinCode);
}
