package com.ruoyi.motorclub.mapper;

import java.util.List;
import com.ruoyi.motorclub.domain.McActivity;

/**
 * 活动数据层
 *
 * @author AI.Coding
 */
public interface McActivityMapper
{
    /**
     * 通过主键查询活动。
     *
     * @param activityId 活动主键
     * @return 活动信息
     */
    McActivity selectMcActivityById(Long activityId);

    /**
     * 查询活动列表。
     *
     * @param activity 查询条件
     * @return 活动列表
     */
    List<McActivity> selectMcActivityList(McActivity activity);

    /**
     * 新增活动。
     *
     * @param activity 活动信息
     * @return 影响行数
     */
    int insertMcActivity(McActivity activity);

    /**
     * 修改活动。
     *
     * @param activity 活动信息
     * @return 影响行数
     */
    int updateMcActivity(McActivity activity);

    /**
     * 批量删除活动。
     *
     * @param activityIds 活动主键数组
     * @return 影响行数
     */
    int deleteMcActivityByIds(Long[] activityIds);
}
