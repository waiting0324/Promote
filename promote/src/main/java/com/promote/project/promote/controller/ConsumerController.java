package com.promote.project.promote.controller;

import com.promote.common.constant.Constants;
import com.promote.common.exception.CustomException;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.utils.MessageUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.redis.RedisCache;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.service.IConsumerService;
import com.promote.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author 6550 劉威廷
 * @date 2020/4/22 下午 02:19
 * @description 消費者 Controller
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController extends BaseController {

    @Autowired
    IConsumerService consumerService;

    @Autowired
    private RedisCache redisCache;

    @PreAuthorize("@ss.hasRole('hostel')")
    @GetMapping("/identity/{identity}")
    public AjaxResult selectByIdentity(@PathVariable("identity") String identity) {

        AjaxResult ajax = AjaxResult.success();
        List<SysUser> users = consumerService.selectByIdentity(identity);
        ajax.put("user", users);

        return ajax;
    }


    /**
     * 消費者註冊
     */
    @PostMapping("/regist")
    public AjaxResult regist(@RequestBody SysUser user, HttpServletRequest request) {

        Map<String, Object> params = user.getParams();

        // 校驗同意條款
        String isAgreeTerms = (String) params.get("isAgreeTerms");
        if (StringUtils.isEmpty(isAgreeTerms) || !("1".equals(isAgreeTerms))) {
            return AjaxResult.error(MessageUtils.message("pro.err.terms.not.check"));
        }

        // 必填欄位檢核
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword()) ||
                StringUtils.isEmpty(user.getConsumerInfo().getName()) || StringUtils.isEmpty(user.getIdentity()) ||
                StringUtils.isEmpty(user.getMobile()) || StringUtils.isEmpty(user.getConsumerInfo().getBirthday())) {
            return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
        }

        String userAgent = request.getHeader("User-Agent");
        // User-Agent不以 Mozilla 開頭，則代表是從APP發來的請求
        Boolean isFromApp = !userAgent.startsWith("Mozilla");

        // 圖形驗證碼校驗
        // 不是從APP訪問則需要圖形驗證碼
        if (!isFromApp) {
            String uuid = (String) params.get("uuid");
            String verifyKey = Constants.CAPTCHA_CODE_KEY + (StringUtils.isNotEmpty(uuid) ? uuid : "");
            String captcha = redisCache.getCacheObject(verifyKey);
            String code = (String) params.get("code");
            if (StringUtils.isEmpty(code)) {
                throw new CustomException(MessageUtils.message("user.jcaptcha.not.exist"));
            }
            if (!code.equalsIgnoreCase(captcha)) {
                throw new CaptchaException();
            }
        }

        consumerService.regist(user);
        return AjaxResult.success();
    }

}
