package com.promote.project.promote.controller;

import com.promote.common.constant.Constants;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.utils.StringUtils;
import com.promote.framework.redis.RedisCache;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.service.IConsumerService;
import com.promote.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        SysUser user = consumerService.selectByIdentity(identity);
        ajax.put("user", user);

        return ajax;
    }


    /**
     * 店家註冊
     */
    @PostMapping("/regist")
    public AjaxResult regist(String agreeTermsFlg, String isFromApp, String uuid, String code, String userName, String password, String name, String identity, String phonenumber, String birthday) {
        if (StringUtils.isEmpty(agreeTermsFlg) || !("1".equals(agreeTermsFlg))) {
            return AjaxResult.error("請勾選註冊條款");
        }
        if (StringUtils.isEmpty(isFromApp)) {
            //web
            String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
            String captcha = redisCache.getCacheObject(verifyKey);
            if (!code.equalsIgnoreCase(captcha)) {
                throw new CaptchaException();
            }
        }
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password) || StringUtils.isEmpty(name) || StringUtils.isEmpty(identity) || StringUtils.isEmpty(phonenumber) || StringUtils.isEmpty(birthday)) {
            return AjaxResult.error("所有欄位皆為必輸欄位");
        }
        consumerService.regist(userName, password, name , identity, phonenumber, birthday);
        return AjaxResult.success();
    }

}
