package com.promote.project.promote.controller;

import com.promote.common.constant.Constants;
import com.promote.common.exception.CustomException;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.utils.MessageUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.ServletUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.redis.RedisCache;
import com.promote.framework.security.LoginUser;
import com.promote.framework.security.service.SysLoginService;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.service.ICommonService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

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
    public AjaxResult login(@RequestBody SysUser user, HttpServletRequest request)
    {

        String userAgent = request.getHeader("User-Agent");
        // User-Agent不以 Mozilla 開頭，則代表是從APP發來的請求
        Boolean isFromWeb = userAgent.startsWith("Mozilla");

        // 圖形驗證碼校驗
        // 不是從APP訪問則需要圖形驗證碼
        if (isFromWeb) {
            Map<String, Object> params = user.getParams();
            String uuid = (String) params.get("uuid");
            String code = (String) params.get("code");
            String verifyKey = Constants.CAPTCHA_CODE_KEY + (StringUtils.isNotEmpty(uuid) ? uuid : "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (StringUtils.isEmpty(code)) {
                throw new CustomException(MessageUtils.message("user.jcaptcha.not.exist"));
            }
            if (!code.equalsIgnoreCase(captcha)) {
                throw new CaptchaException();
            }
        }

        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(user.getUsername(), user.getPassword(), "", "");
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

    /**
     * 變更密碼
     * @return 結果
     */
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody Map<String, String> request)
    {

        String oldPassword = request.get("oldPwd");
        String newPassword = request.get("newPwd");

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return AjaxResult.error("修改密碼失敗，舊密碼錯誤");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return AjaxResult.error("新密碼不能與舊密碼相同");
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0)
        {
            // 更新快取使用者密碼
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改密碼異常，請聯絡管理員");
    }


    /**
     * 取得 消費者、商家 基本資料
     *
     * @return 結果
     */
    @GetMapping("/user/info")
    public AjaxResult getUserInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        user.setPassword(null);
        return AjaxResult.success(user);
    }

}
