package com.ruoyi.motorclub.controller.app;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.vo.McSystemSettingVo;
import com.ruoyi.motorclub.service.IMcContentService;

/**
 * 小程序管理端系统设置接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/admin/settings")
public class McAdminSettingAppController extends McAdminAppBaseController
{
    @Resource
    private IMcContentService mcContentService;

    /**
     * 查询系统设置。
     *
     * @return 系统设置
     */
    @GetMapping
    public AjaxResult detail()
    {
        getRequiredAdminUser();
        return success(mcContentService.getSystemSettings());
    }

    /**
     * 保存系统设置。
     *
     * @param settingVo 系统设置
     * @return 操作结果
     */
    @PostMapping
    public AjaxResult save(@RequestBody McSystemSettingVo settingVo)
    {
        McUser admin = getRequiredAdminUser();
        mcContentService.saveSystemSettings(settingVo, buildOperator(admin));
        return success(mcContentService.getSystemSettings());
    }

    /**
     * 构造操作人标识。
     *
     * @param admin 当前管理员
     * @return 操作人
     */
    private String buildOperator(McUser admin)
    {
        return StringUtils.defaultIfBlank(admin.getNickName(), "app-admin");
    }
}
