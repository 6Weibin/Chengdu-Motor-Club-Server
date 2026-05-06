package com.ruoyi.motorclub.mapper;

import org.apache.ibatis.annotations.Param;
import com.ruoyi.motorclub.domain.McAdminPermission;

/**
 * 业务管理员权限数据层
 *
 * @author AI.Coding
 */
public interface McAdminPermissionMapper
{
    /**
     * 按用户查询权限。
     *
     * @param userId 用户主键
     * @return 权限信息
     */
    McAdminPermission selectMcAdminPermissionByUserId(@Param("userId") Long userId);

    /**
     * 新增权限记录。
     *
     * @param permission 权限信息
     * @return 影响行数
     */
    int insertMcAdminPermission(McAdminPermission permission);

    /**
     * 修改权限记录。
     *
     * @param permission 权限信息
     * @return 影响行数
     */
    int updateMcAdminPermission(McAdminPermission permission);

    /**
     * 删除权限记录。
     *
     * @param userId 用户主键
     * @return 影响行数
     */
    int deleteMcAdminPermissionByUserId(@Param("userId") Long userId);
}
