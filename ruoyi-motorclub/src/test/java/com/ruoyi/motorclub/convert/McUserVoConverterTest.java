package com.ruoyi.motorclub.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.ruoyi.motorclub.domain.McAdminPermission;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.vo.McUserVo;
import com.ruoyi.motorclub.enums.McAdminTypeEnum;
import com.ruoyi.motorclub.enums.McMemberStatusEnum;

/**
 * McUserVoConverter 单元测试
 *
 * @author AI.Coding
 */
class McUserVoConverterTest
{
    /**
     * 应将普通管理员转换为标准权限视图。
     */
    @Test
    void shouldConvertAdminUserToStandardPermissions()
    {
        McUser user = buildBaseUser();
        user.setMemberStatus(McMemberStatusEnum.MEMBER.getCode());
        user.setAppAdminType(McAdminTypeEnum.ADMIN.getCode());

        McAdminPermission permission = new McAdminPermission();
        permission.setMemberManage("1");
        permission.setActivityManage("0");
        permission.setBannerManage("1");
        permission.setBenefitManage("0");
        user.setAdminPermission(permission);

        McUserVo vo = McUserVoConverter.toVo(user);

        // 校验核心派生字段，确保前端无需直接解释底层状态码。
        Assertions.assertEquals(Long.valueOf(1001L), vo.getUserId());
        Assertions.assertTrue(vo.getIsMember());
        Assertions.assertTrue(vo.getIsAdmin());
        Assertions.assertFalse(vo.getIsSuperAdmin());
        Assertions.assertTrue(vo.getAdminPermissions().isMemberManage());
        Assertions.assertFalse(vo.getAdminPermissions().isActivityManage());
        Assertions.assertTrue(vo.getAdminPermissions().isBannerManage());
        Assertions.assertFalse(vo.getAdminPermissions().isBenefitManage());
    }

    /**
     * 应将超级管理员转换为全量权限视图。
     */
    @Test
    void shouldGrantAllPermissionsToSuperAdmin()
    {
        McUser user = buildBaseUser();
        user.setAppAdminType(McAdminTypeEnum.SUPER_ADMIN.getCode());

        McUserVo vo = McUserVoConverter.toVo(user);

        // 超管不依赖权限表，四类权限应全部开启。
        Assertions.assertFalse(vo.getIsMember());
        Assertions.assertTrue(vo.getIsAdmin());
        Assertions.assertTrue(vo.getIsSuperAdmin());
        Assertions.assertTrue(vo.getAdminPermissions().isMemberManage());
        Assertions.assertTrue(vo.getAdminPermissions().isActivityManage());
        Assertions.assertTrue(vo.getAdminPermissions().isBannerManage());
        Assertions.assertTrue(vo.getAdminPermissions().isBenefitManage());
    }

    /**
     * 应在空入参时返回空结果。
     */
    @Test
    void shouldReturnNullWhenUserIsNull()
    {
        Assertions.assertNull(McUserVoConverter.toVo(null));
    }

    /**
     * 构造测试用户。
     *
     * @return 测试用户
     */
    private McUser buildBaseUser()
    {
        McUser user = new McUser();
        // 构造与小程序契约相关的基础字段，避免测试只覆盖布尔派生。
        user.setUserId(1001L);
        user.setNickName("测试用户");
        user.setAvatarUrl("https://example.com/avatar.png");
        user.setPhone("13800000000");
        user.setRealName("张三");
        user.setIdNumberMasked("5101********1234");
        user.setCarModel("718");
        user.setMemberCardNo("VIP20250001");
        user.setStatus("0");
        user.setMemberStatus(McMemberStatusEnum.VISITOR.getCode());
        user.setAppAdminType(McAdminTypeEnum.NONE.getCode());
        return user;
    }
}
