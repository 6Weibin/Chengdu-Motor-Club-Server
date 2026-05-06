package com.ruoyi.motorclub.controller.app;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.motorclub.constant.McConstants;
import com.ruoyi.motorclub.domain.McUser;
import com.ruoyi.motorclub.domain.dto.McCheckinBody;
import com.ruoyi.motorclub.service.IMcCheckinService;

/**
 * 小程序签到接口
 *
 * @author AI.Coding
 */
@RestController
@RequestMapping("/app-api/motorclub/checkin")
public class McCheckinAppController extends McAppBaseController
{
    @Resource
    private IMcCheckinService mcCheckinService;

    /**
     * 提交签到。
     *
     * @param body 签到内容
     * @return 操作结果
     */
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody McCheckinBody body)
    {
        McUser user = getRequiredMcUser();
        return toAjax(mcCheckinService.checkin(user.getUserId(), resolveCheckinToken(body)));
    }

    /**
     * 从请求体中解析签到 token。
     *
     * @param body 签到请求
     * @return 签到 token
     */
    private String resolveCheckinToken(McCheckinBody body)
    {
        if (body == null)
        {
            throw new ServiceException("签到码不能为空");
        }
        if (StringUtils.isNotBlank(body.getToken()))
        {
            return body.getToken();
        }
        if (StringUtils.isBlank(body.getCodeValue()))
        {
            throw new ServiceException("签到码不能为空");
        }
        // 兼容云端二维码协议：CDMC_ACTIVITY_CHECKIN:activityId:token。
        String[] segments = StringUtils.split(body.getCodeValue(), ":");
        if (segments == null || segments.length != 3 || !McConstants.CHECKIN_CODE_PREFIX.equals(segments[0]))
        {
            throw new ServiceException("签到二维码内容无效");
        }
        return segments[2];
    }
}
