package com.ruoyi.motorclub.mapper;

import java.util.List;
import com.ruoyi.motorclub.domain.McActivity;
import com.ruoyi.motorclub.domain.vo.McPortalActivityDetailVo;
import com.ruoyi.motorclub.domain.vo.McPortalActivityListVo;

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
     * 查询已删除活动列表。
     *
     * @param activity 查询条件
     * @return 已删除活动列表
     */
    List<McActivity> selectDeletedMcActivityList(McActivity activity);

    /**
     * 查询门户活动列表。
     *
     * @return 门户活动列表
     */
    List<McPortalActivityListVo> selectPortalActivityList();

    /**
     * 查询门户活动详情。
     *
     * @param activityId 活动主键
     * @return 门户活动详情
     */
    McPortalActivityDetailVo selectPortalActivityDetailById(Long activityId);

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
     * 更新活动状态。
     *
     * @param activity 活动状态更新信息
     * @return 影响行数
     */
    int updateMcActivityStatus(McActivity activity);
}
