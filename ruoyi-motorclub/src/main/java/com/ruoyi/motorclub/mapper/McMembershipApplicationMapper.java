package com.ruoyi.motorclub.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.motorclub.domain.McMembershipApplication;

/**
 * 入会申请数据层
 *
 * @author AI.Coding
 */
public interface McMembershipApplicationMapper
{
    /**
     * 通过主键查询入会申请。
     *
     * @param applicationId 申请主键
     * @return 入会申请
     */
    McMembershipApplication selectMcMembershipApplicationById(Long applicationId);

    /**
     * 查询用户最新申请。
     *
     * @param userId 用户主键
     * @return 入会申请
     */
    McMembershipApplication selectLatestMcMembershipApplicationByUserId(@Param("userId") Long userId);

    /**
     * 查询入会申请列表。
     *
     * @param application 查询条件
     * @return 入会申请列表
     */
    List<McMembershipApplication> selectMcMembershipApplicationList(McMembershipApplication application);

    /**
     * 新增入会申请。
     *
     * @param application 入会申请
     * @return 影响行数
     */
    int insertMcMembershipApplication(McMembershipApplication application);

    /**
     * 修改入会申请。
     *
     * @param application 入会申请
     * @return 影响行数
     */
    int updateMcMembershipApplication(McMembershipApplication application);
}
