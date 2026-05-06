package com.ruoyi.motorclub.controller.app;

import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.IpUtils;
import com.ruoyi.motorclub.domain.McMembershipApplication;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.dto.McLoginBody;
import com.ruoyi.motorclub.domain.dto.McMembershipApplicationBody;
import com.ruoyi.motorclub.domain.dto.McProfileUpdateBody;
import com.ruoyi.motorclub.service.IMcUserService;

/**
 * 小程序账号与会员接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/auth")
public class McAuthController extends McAppBaseController
{
    @Resource
    private IMcUserService mcUserService;

    /**
     * 执行小程序登录。
     *
     * @param loginBody 登录请求
     * @return 登录结果
     */
    // 修复点：登录接口必须允许匿名访问，否则请求会先被 Shiro 后台登录链 302 到 /login。
    @Anonymous
    @PostMapping("/login")
    public AjaxResult login(@Valid @RequestBody McLoginBody loginBody)
    {
        return success(mcUserService.login(loginBody, IpUtils.getIpAddr(getRequest())));
    }

    /**
     * 查询当前登录用户。
     *
     * @return 当前用户标准信息
     */
    @GetMapping("/me")
    public AjaxResult me()
    {
        McUser user = getRequiredMcUser();
        return success(mcUserService.selectMcUserVoById(user.getUserId()));
    }

    /**
     * 修改当前用户资料。
     *
     * @param body 更新内容
     * @return 更新后的标准用户信息
     */
    @PostMapping("/profile")
    public AjaxResult updateProfile(@RequestBody McProfileUpdateBody body)
    {
        McUser user = getRequiredMcUser();
        mcUserService.updateProfile(user.getUserId(), body);
        // 资料更新后统一返回标准用户 VO，保持登录与 me 接口字段契约一致。
        return success(mcUserService.selectMcUserVoById(user.getUserId()));
    }

    /**
     * 注销当前用户。
     *
     * @return 操作结果
     */
    @PostMapping("/cancel")
    public AjaxResult cancel()
    {
        McUser user = getRequiredMcUser();
        return toAjax(mcUserService.cancelUser(user.getUserId()));
    }

    /**
     * 提交入会申请。
     *
     * @param body 入会申请内容
     * @return 入会申请结果
     */
    @PostMapping("/membership/apply")
    public AjaxResult applyMembership(@Valid @RequestBody McMembershipApplicationBody body)
    {
        McUser user = getRequiredMcUser();
        return success(mcUserService.submitMembershipApplication(user.getUserId(), body));
    }

    /**
     * 查询最新入会申请。
     *
     * @return 入会申请结果
     */
    @GetMapping("/membership/latest")
    public AjaxResult latestMembership()
    {
        McUser user = getRequiredMcUser();
        McMembershipApplication application = mcUserService.selectLatestMembershipApplication(user.getUserId());
        return success(application);
    }
}
