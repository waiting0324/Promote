package com.promote.project.promote.controller;

import com.promote.common.constant.Constants;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.utils.ServletUtils;
import com.promote.common.utils.StringUtils;
import com.promote.common.utils.ip.IpUtils;
import com.promote.framework.redis.RedisCache;
import com.promote.framework.security.LoginUser;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.monitor.service.ISysOperLogService;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.service.IProStoreService;
import com.promote.project.promote.service.IProWhitelistService;
import com.promote.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 店家 控制層
 *
 * @author 曾培晃
 */
@RestController
@RequestMapping("/store")
public class ProStoreController extends BaseController {
    @Autowired
    private IProWhitelistService whitelistService;

    @Autowired
    private ISysOperLogService operLogService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IProStoreService storeService;

    @Autowired
    private TokenService tokenService;

    /**
     * 店家白名單檢核
     */
    @GetMapping("/checkWhitelist")
    public AjaxResult checkWhitelist(String taxNo) {
        if (StringUtils.isNotEmpty(taxNo)) {
            List<ProWhitelist> proWhitelist = whitelistService.selectProWhitelistByTaxNo(taxNo);
            if (proWhitelist != null && proWhitelist.size() > 0) {
                return AjaxResult.success(proWhitelist);
            }
            operLogService.insertOperlog("白名單", null, null, ProStoreController.class.getName() + ".checkWhitelist(String taxNo)", ServletUtils.getRequest().getMethod(), null, null, null, ServletUtils.getRequest().getRequestURI(), IpUtils.getIpAddr(ServletUtils.getRequest()), null, null, null, 1, "白名單內查無資料");
            return AjaxResult.error("白名單內查無資料");
        }
        return AjaxResult.error("未輸入任何值");
    }

    /**
     * 店家是否同意註冊條款
     */
//    @PutMapping("/updIsAgreeTerms")
//    public AjaxResult updIsAgreeTerms(String id, String agreeTermsFlg) {
//        if (StringUtils.isNotEmpty(agreeTermsFlg)) {
//            if ("1".equals(agreeTermsFlg)) {
//                //同意
//                ProWhitelist proWhitelist = new ProWhitelist();
//                proWhitelist.setId(id);
//                proWhitelist.setIsAgreeTerms(agreeTermsFlg);
//                if (whitelistService.updateProWhitelist(proWhitelist) > 0) {
//                    return AjaxResult.success();
//                }
//                return AjaxResult.error("更新白名單是否同意註冊條款失敗");
//            }
//            operLogService.insertOperlog("白名單", null, null, ProStoreController.class.getName() + ".updIsAgreeTerms(String id, String agreeTermsFlg)", ServletUtils.getRequest().getMethod(), null, null, null, ServletUtils.getRequest().getRequestURI(), IpUtils.getIpAddr(ServletUtils.getRequest()), null, null, null, 1, "店家不同意註冊條款");
//            return AjaxResult.error("店家不同意註冊條款");
//        }
//        return AjaxResult.error("店家需填選註冊條款");
//    }


    /**
     * 店家註冊
     */
    @PostMapping("/regist")
    public AjaxResult regist(String agreeTermsFlg, String isFromApp, String uuid, String code, String id, String userName, String password, String name, String identity, String phonenumber, String storeName, String address, String bankAccount, String bankAccountName) {
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
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password) || StringUtils.isEmpty(name) || StringUtils.isEmpty(identity) || StringUtils.isEmpty(phonenumber) || StringUtils.isEmpty(storeName) || StringUtils.isEmpty(address) || StringUtils.isEmpty(bankAccount) || StringUtils.isEmpty(bankAccountName)) {
            return AjaxResult.error("所有欄位皆為必輸欄位");
        }
        storeService.regist(id, userName, password, identity, name, phonenumber, storeName, address, bankAccount, bankAccountName);
        return AjaxResult.success();
    }

    /**
     * 取得店家基本資料
     */
    @GetMapping("/getStoreInfo")
    public AjaxResult getStoreInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser != null) {
            SysUser user = loginUser.getUser();
            if (user != null) {
                user.setPassword(null);
                return AjaxResult.success(user);
            }
            return AjaxResult.error("查無此店家");
        }
        return AjaxResult.error("非法登入");
    }

    /**
     * 修改店家基本資料
     */
    @PutMapping("/updateStoreInfo")
    public AjaxResult updateStoreInfo(@RequestBody SysUser sysUser) {
        if(sysUser != null){
            if(sysUser.getUserId() == null){
                return AjaxResult.error("userId需有值");
            }
            if(storeService.updateStoreInfo(sysUser) > 0){
                return AjaxResult.success();
            }
            return AjaxResult.error("修改店家基本資料失敗，請聯絡管理員");
        }
        return AjaxResult.error("請輸入資料");
    }

}
