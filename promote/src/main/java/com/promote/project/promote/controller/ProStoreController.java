package com.promote.project.promote.controller;

import com.promote.common.constant.Constants;
import com.promote.common.exception.CustomException;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.utils.MessageUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.redis.RedisCache;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
     *
     * @param taxNo 統編/身分證字號
     * @return 結果
     */
    @GetMapping("/whitelist/{taxNo}")
    public AjaxResult checkWhitelist(@PathVariable("taxNo") String taxNo) {

        // 未傳入參數
        if (StringUtils.isEmpty(taxNo)) {
            return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
        }

        // 查詢白名單
        ProWhitelist white = whitelistService.selectProWhitelistByTaxNo(taxNo);

        // 沒有找到
        if (StringUtils.isNull(white)) {
            AjaxResult.error(MessageUtils.message("pro.err.data.not.find"));
        }

        // 寫入記錄檔
        //operLogService.insertOperlog("白名單", null, null, ProStoreController.class.getName() + ".checkWhitelist(String taxNo)", ServletUtils.getRequest().getMethod(), null, null, null, ServletUtils.getRequest().getRequestURI(), IpUtils.getIpAddr(ServletUtils.getRequest()), null, null, null, 1, MessageUtils.message("pro.err.data.not.find"));

        return AjaxResult.success(white);
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
     *
     * @param user 使用者資料
     * @return 結果
     */
    @PostMapping("/regist")
    public AjaxResult regist(@RequestBody SysUser user, HttpServletRequest request) {

        Map<String, Object> params = user.getParams();

        // 註冊條款校驗
        String isAgreeTerms = (String) params.get("isAgreeTerms");
        if (StringUtils.isEmpty(isAgreeTerms) || !("1".equals(isAgreeTerms))) {
            return AjaxResult.error(MessageUtils.message("pro.err.terms.not.check"));
        }

        // 必填欄位檢核
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword()) ||
                StringUtils.isEmpty(user.getStoreInfo().getName()) || StringUtils.isEmpty(user.getIdentity()) ||
                StringUtils.isEmpty(user.getMobile()) || StringUtils.isEmpty(user.getStoreInfo().getName()) ||
                StringUtils.isEmpty(user.getStoreInfo().getAddress()) || StringUtils.isEmpty(user.getStoreInfo().getBankAccount()) ||
                StringUtils.isEmpty(user.getStoreInfo().getBankAccountName())) {
            return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
        }

        String userAgent = request.getHeader("User-Agent");
        // User-Agent不以 Mozilla 開頭，則代表是從APP發來的請求
        Boolean isFromWeb = userAgent.startsWith("Mozilla");

        // 圖形驗證碼校驗
        // 不是從APP訪問則需要圖形驗證碼
        if (isFromWeb) {
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

        // 白名單id
        String whitelistId = (String) params.get("whitelistId");

        // 進行註冊
        storeService.regist(user, isAgreeTerms, whitelistId);


        return AjaxResult.success();
    }

    /**
     * 取得店家基本資料
     */
    /*@GetMapping("/getStoreInfo")
    public AjaxResult getStoreInfo() {
        String json = JSON.toJSONString(SecurityUtils.getLoginUser().getUser(), true);
        SysUser user = JSON.parseObject(json, SysUser.class);
        SysUser user = SecurityUtils.getLoginUser().getUser();
        user.setPassword(null);
        return AjaxResult.success(user);
    }*/

    /**
     * 修改店家基本資料
     */
    @PutMapping("/info")
    public AjaxResult updateStoreInfo(@RequestBody SysUser user) {
        if (StringUtils.isEmpty(user.getStoreInfo().getName()) &&
                StringUtils.isEmpty(user.getStoreInfo().getAddress()) &&
                StringUtils.isEmpty(user.getMobile())) {
            return AjaxResult.success();
        }
        tokenService.resetLoginUser(storeService.updateStoreInfo(user));
        return AjaxResult.success();
    }

    /**
     * 當前商家收款紀錄總覽
     */
    @GetMapping("/getRecdMoneyRecord")
    public AjaxResult getRecdMoneyRecord(){
        AjaxResult ajax = new AjaxResult();
        ajax.putAll(storeService.getRecdMoneyRecord());
        return ajax;
    }
}
