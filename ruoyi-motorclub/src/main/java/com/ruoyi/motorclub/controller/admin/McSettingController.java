package com.ruoyi.motorclub.controller.admin;

import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.motorclub.domain.vo.McSystemSettingVo;
import com.ruoyi.motorclub.service.IMcContentService;

/**
 * 系统开关后台控制器
 *
 * @author AI.Coding
 */
@Controller
@RequestMapping("/motorclub/setting")
public class McSettingController extends BaseController
{
    /** 页面前缀。 */
    private static final String PREFIX = "motorclub/setting";

    @Resource
    private IMcContentService mcContentService;

    /**
     * 打开系统设置页面。
     *
     * @return 页面路径
     */
    @RequiresPermissions("motorclub:setting:view")
    @GetMapping()
    public String setting()
    {
        return PREFIX + "/setting";
    }

    /**
     * 查询系统设置。
     *
     * @return 系统设置
     */
    @RequiresPermissions("motorclub:setting:list")
    @GetMapping("/detail")
    @ResponseBody
    public AjaxResult detail()
    {
        return success(mcContentService.getSystemSettings());
    }

    /**
     * 保存系统设置。
     *
     * @param settingVo 系统设置
     * @return 操作结果
     */
    @RequiresPermissions("motorclub:setting:edit")
    @Log(title = "车友会系统配置", businessType = BusinessType.UPDATE)
    @PostMapping("/save")
    @ResponseBody
    public AjaxResult save(@RequestBody McSystemSettingVo settingVo)
    {
        mcContentService.saveSystemSettings(settingVo, getLoginName());
        return success();
    }
}
