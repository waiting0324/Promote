package com.promote.project.promote.controller;

import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.ServletUtils;
import com.promote.common.utils.StringUtils;
import com.promote.common.utils.ip.IpUtils;
import com.promote.framework.security.LoginUser;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.monitor.domain.SysOperLog;
import com.promote.project.monitor.service.ISysOperLogService;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.service.IProWhitelistService;
import com.promote.project.promote.service.ISysHostelService;
import com.promote.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 店家 控制層
 *
 * @author 曾培晃
 */
@RestController
@RequestMapping("/store")
public class SysStoreController extends BaseController {
    @Autowired
    private IProWhitelistService proWhitelistService;

    @Autowired
    private ISysOperLogService operLogService;


    @Autowired
    private TokenService tokenService;


    /**
     * 店家白名單檢核
     */
    @GetMapping("/checkWhitelist")
    public AjaxResult regist(String taxNo) {
        if (StringUtils.isNotEmpty(taxNo)) {
            ProWhitelist proWhitelist = proWhitelistService.selectProWhitelistByTaxNo(taxNo);
            if (proWhitelist != null) {
                return AjaxResult.success(proWhitelist);
            }
            operLogService.insertOperlog("白名單", null, null, SysStoreController.class.getName() + ".regist(String taxNo)", ServletUtils.getRequest().getMethod(), null, null, null, ServletUtils.getRequest().getRequestURI(), IpUtils.getIpAddr(ServletUtils.getRequest()), null, null, null, 1, "白名單內查無資料");
            return AjaxResult.error("白名單內查無資料");
        }
        return AjaxResult.error("未輸入任何值");
    }

    /**
     * 店家是否同意註冊條款
     */
    @PutMapping("/updIsAgreeTerms")
    public AjaxResult updIsAgreeTerms(String id, String agreeTermsFlg){
        if(StringUtils.isNotEmpty(agreeTermsFlg)){
            ProWhitelist proWhitelist = new ProWhitelist();
            proWhitelist.setId(id);
            proWhitelist.setIsAgreeTerms(agreeTermsFlg);
            if(proWhitelistService.updateProWhitelist(proWhitelist) > 0){
                return AjaxResult.success();
            }
            return AjaxResult.error("更新白名單是否同意註冊條款失敗");
        }
        operLogService.insertOperlog("白名單", null, null, SysStoreController.class.getName() + ".updIsAgreeTerms(String agreeTermsFlg)", ServletUtils.getRequest().getMethod(), null, null, null, ServletUtils.getRequest().getRequestURI(), IpUtils.getIpAddr(ServletUtils.getRequest()), null, null, null, 1, "店家不同意註冊條款");
        return AjaxResult.error("店家不同意註冊條款");
    }

}
