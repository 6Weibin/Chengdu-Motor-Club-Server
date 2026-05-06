package com.ruoyi.motorclub.controller.app;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.motorclub.service.IMcContentService;

/**
 * 小程序内容与配置接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/content")
public class McContentAppController extends McAppBaseController
{
    @Resource
    private IMcContentService mcContentService;

    /**
     * 查询 Banner 列表。
     *
     * @return Banner 列表
     */
    // 修复点：首页公开内容接口需要匿名访问，避免被后台网页登录会话链误拦截。
    @Anonymous
    @GetMapping("/banners")
    public AjaxResult banners()
    {
        return success(mcContentService.selectEnabledBannerList());
    }

    /**
     * 查询权益列表。
     *
     * @return 权益列表
     */
    // 修复点：首页公开内容接口需要匿名访问，避免被后台网页登录会话链误拦截。
    @Anonymous
    @GetMapping("/benefits")
    public AjaxResult benefits()
    {
        return success(mcContentService.selectEnabledBenefitList());
    }

    /**
     * 查询权益详情。
     *
     * @param benefitId 权益主键
     * @return 权益详情
     */
    // 修复点：权益详情属于公开只读接口，应明确标记匿名访问语义。
    @Anonymous
    @GetMapping("/benefits/{benefitId}")
    public AjaxResult benefitDetail(@PathVariable("benefitId") Long benefitId)
    {
        return success(mcContentService.selectMcBenefitById(benefitId));
    }

    /**
     * 查询系统开关。
     *
     * @return 系统开关
     */
    // 修复点：系统设置前台读取接口需要匿名访问，供未登录用户也能渲染页面开关。
    @Anonymous
    @GetMapping("/settings")
    public AjaxResult settings()
    {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("settings", mcContentService.getSystemSettings());
        return success(result);
    }
}
