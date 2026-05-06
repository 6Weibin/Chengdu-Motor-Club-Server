package com.ruoyi.motorclub.controller.app;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.motorclub.domain.McBanner;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.service.IMcContentService;

/**
 * 小程序管理端 Banner 接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/admin/banners")
public class McAdminBannerAppController extends McAdminAppBaseController
{
    @Resource
    private IMcContentService mcContentService;

    /**
     * 查询 Banner 列表。
     *
     * @param banner 查询条件
     * @return Banner 列表
     */
    @GetMapping("/list")
    public AjaxResult list(McBanner banner)
    {
        getRequiredBannerManageUser();
        startPage();
        List<McBanner> list = mcContentService.selectMcBannerList(banner);
        return tableSuccess(list);
    }

    /**
     * 查询 Banner 详情。
     *
     * @param bannerId Banner 主键
     * @return Banner 详情
     */
    @GetMapping("/{bannerId}")
    public AjaxResult detail(@PathVariable("bannerId") Long bannerId)
    {
        getRequiredBannerManageUser();
        return success(mcContentService.selectMcBannerById(bannerId));
    }

    /**
     * 新增 Banner。
     *
     * @param banner Banner 内容
     * @return 操作结果
     */
    @PostMapping("/create")
    public AjaxResult create(@RequestBody McBanner banner)
    {
        McUser admin = getRequiredBannerManageUser();
        mcContentService.insertMcBanner(banner, buildOperator(admin));
        return success(banner);
    }

    /**
     * 修改 Banner。
     *
     * @param banner Banner 内容
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody McBanner banner)
    {
        McUser admin = getRequiredBannerManageUser();
        mcContentService.updateMcBanner(banner, buildOperator(admin));
        return success(mcContentService.selectMcBannerById(banner.getBannerId()));
    }

    /**
     * 删除 Banner。
     *
     * @param bannerId Banner 主键
     * @return 操作结果
     */
    @PostMapping("/remove/{bannerId}")
    public AjaxResult remove(@PathVariable("bannerId") Long bannerId)
    {
        getRequiredBannerManageUser();
        return toAjax(mcContentService.deleteMcBannerByIds(String.valueOf(bannerId)));
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
