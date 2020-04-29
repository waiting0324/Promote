package com.promote.project.promote.controller;

import com.promote.common.constant.Constants;
import com.promote.common.utils.MessageUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.security.service.SysLoginService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.service.ICommonService;
import com.promote.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * @author 6550 劉威廷
 * @date 2020/4/23 下午 01:58
 * @description 通用功能模塊
 */
@RestController
@RequestMapping
public class ProCommonController extends BaseController {

    @Autowired
    ICommonService commonService;

    @Autowired
    private SysLoginService loginService;

    /**
     * 發送驗證碼
     *
     * @param sysUser 使用者資料
     */
    @PostMapping("/captcha")
    public AjaxResult captcha(@RequestBody SysUser sysUser) throws MessagingException {
        //帳號
        String username = sysUser.getUsername();
        Map<String, Object> params = sysUser.getParams();
        //驗證類型(1:Email,2:手機)
        String type = (String) params.get("type");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(type)) {
            return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
        }
        commonService.sendCaptcha(username, type);
        return AjaxResult.success();
    }

    /**
     * 登入方法
     */
    @PostMapping("/common/login")
    public AjaxResult login(@RequestBody SysUser user)
    {
        Map<String, Object> params = user.getParams();
        String uuid = (String) params.get("uuid");
        String code = (String) params.get("code");

        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(user.getUsername(), user.getPassword(), code, uuid);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }



    /**
     * 忘記密碼，驗證並更新密碼
     *
     * @param sysUser 使用者資料
     * @return 結果
     */
    @PostMapping("/forgetPwd")
    public AjaxResult forgetPwd(@RequestBody SysUser sysUser) {
        String username = (String) sysUser.getUsername();
        Map<String, Object> params = sysUser.getParams();
        //新密碼
        String newPwd = (String) params.get("newPwd");
        //確認新密碼
        String checkNewPwd = (String) params.get("checkNewPwd");
        //驗證碼
        String code = (String) params.get("code");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(checkNewPwd) || StringUtils.isEmpty(code)) {
            return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
        }
        if (!newPwd.equals(checkNewPwd)) {
            AjaxResult.error("兩次輸入的密碼不一致");
        }

        if (!newPwd.matches(Constants.PASSWORD_REGEX)) {
            AjaxResult.error("新密碼不符合6-20字元的英數字規則");
        }

        // 確認驗證碼並更新密碼
        return toAjax(commonService.forgetPwd(code, username, newPwd));
    }
}
