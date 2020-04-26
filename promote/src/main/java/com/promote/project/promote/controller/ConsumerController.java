package com.promote.project.promote.controller;

import com.promote.common.constant.Constants;
import com.promote.common.exception.CustomException;
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
        SysUser user = consumerService.selectByIdentity(identity);
        ajax.put("user", user);

        return ajax;
    }


    /**
     * 消費者註冊
     */
    @PostMapping("/regist")
    public AjaxResult regist(@RequestBody SysUser user) {
        if (StringUtils.isEmpty(user.getIsAgreeTerms()) || !("1".equals(user.getIsAgreeTerms()))) {
            return AjaxResult.error("消費者需勾選註冊條款");
        }
        // 必填欄位檢核
        String userName = user.getUsername();
        String password = user.getPassword();
        String name = user.getName();
        String identity = user.getIdentity();
        String phonenumber = user.getPhonenumber();
        String birthday = user.getBirthday();
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(name) || StringUtils.isEmpty(identity) ||
                StringUtils.isEmpty(phonenumber) || StringUtils.isEmpty(birthday)) {
            return AjaxResult.error("所有欄位皆為必輸欄位");
        }

        Map<String, Object> params = user.getParams();
        String isFromApp = (String) params.get("isFromApp");
        if ("0".equals(isFromApp)) {
            //web
            String uuid = (String) params.get("uuid");
            String verifyKey = Constants.CAPTCHA_CODE_KEY + (StringUtils.isNotEmpty(uuid) ? uuid : "");
            String captcha = redisCache.getCacheObject(verifyKey);
            String code = (String) params.get("code");
            if (StringUtils.isEmpty(code)) {
                throw new CustomException("未輸入驗證碼");
            }
            if (!code.equalsIgnoreCase(captcha)) {
                throw new CaptchaException();
            }
        }

        consumerService.regist(userName, password, name, identity, phonenumber, birthday);
        return AjaxResult.success();
    }

}
