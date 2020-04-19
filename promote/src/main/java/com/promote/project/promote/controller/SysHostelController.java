package com.promote.project.promote.controller;

import com.promote.common.utils.EmailUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.ServletUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.security.LoginUser;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.system.domain.SysUser;
import com.promote.project.promote.service.ISysHostelService;
import com.promote.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

/**
 * 旅宿業者 控制層
 *
 * @author 曾培晃
 */
@RestController
@RequestMapping("/hostel")
public class SysHostelController extends BaseController {
    @Autowired
    private ISysHostelService hostelService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService userService;

    /**
     * 旅宿業者註冊
     */
    @PostMapping("/regist")
    public AjaxResult regist(String acct, String pwd) {
        if (StringUtils.isNotEmpty(acct) && StringUtils.isNotEmpty(pwd)) {
            return toAjax(hostelService.regist(acct, pwd));
        }
        return AjaxResult.error("帳號or密碼未輸入值");
    }

    /**
     * 旅宿業者變更密碼
     */
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(String oldPwd, String newPwd) {
        if (StringUtils.isNotEmpty(oldPwd) && StringUtils.isNotEmpty(newPwd)) {
            String msg = matchesPassword(newPwd);
            if(StringUtils.isNotEmpty(msg)){
                return AjaxResult.error("msg");
            }
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String password = loginUser.getPassword();
            if (!SecurityUtils.matchesPassword(oldPwd, password)) {
                return AjaxResult.error("修改密碼失敗，舊密碼錯誤");
            }
            if (SecurityUtils.matchesPassword(newPwd, password)) {
                return AjaxResult.error("新密碼不能與舊密碼相同");
            }
            SysUser sysUser = loginUser.getUser();
            if (hostelService.resetPwd(sysUser, SecurityUtils.encryptPassword(newPwd)) > 0) {
                // 更新快取使用者密碼
                sysUser.setPassword(SecurityUtils.encryptPassword(newPwd));
                tokenService.setLoginUser(loginUser);
                return AjaxResult.success();
            }
            return AjaxResult.error("修改密碼異常，請聯絡管理員");
        }
        return AjaxResult.error("舊密碼or新密碼未輸入值");
    }

    /**
     * 判斷密碼是否符合規則(需有英文大小寫及數字,8-16碼)
     */
    private String matchesPassword(String pwd){
        String msg = null;
        if(pwd.length() < 8 || pwd.length() > 16){
            msg = "密碼須為8-16碼";
        }else{
            boolean lowerCase = false;
            boolean upperCase = false;
            boolean digit = false;
            for(int i = 0; i < pwd.length(); i++){
                if(lowerCase && upperCase && digit){
                    break;
                }
                char unit = pwd.charAt(i);
                if(Character.isLowerCase(unit)){
                    lowerCase = true;
                    continue;
                }
                if(Character.isUpperCase(unit)){
                    upperCase = true;
                    continue;
                }
                if(Character.isDigit(unit)){
                    digit = true;
                }
            }
            if(!lowerCase || !upperCase || !digit){
                msg = "密碼須需有英文大小寫及數字";
            }
        }
        return msg;
    }

    /**
     * 忘記密碼
     *
     * @param acct 帳號
     * @param newPwd 新密碼
     * @param validCode 驗證碼
     * @return 結果
     */
    @PostMapping("/forgetPwd")
    public AjaxResult forgetPwd(String acct, String newPwd,String validCode) {
        if(StringUtils.isNotEmpty(acct) && StringUtils.isNotEmpty(newPwd) && StringUtils.isNotEmpty(validCode)){
            String code = tokenService.getCacheObject("forget_pwd:" + acct);
            if (validCode.equals(code)) {

            }
            return AjaxResult.error("驗證碼錯誤");
        }
        return AjaxResult.error("帳號or新密碼or驗證碼未輸入值");
    }

    @RequestMapping("/sendOtpEmail")
    public AjaxResult sendOtpEmail(SysUser sysUser){
        if (StringUtils.isNotNull(sysUser) && StringUtils.isNotNull(sysUser.getUserId()) && StringUtils.isNotEmpty(sysUser.getBirthday())) {
            tokenService.setCacheObject("forget_pwd" + sysUser.getUserId(),userService.selectUserByIdBirthday(sysUser));
        }
        return AjaxResult.error("帳號or生日未輸入值");
    }
}
