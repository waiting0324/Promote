package com.promote.project.promote.controller;

import com.promote.common.constant.Constants;
import com.promote.common.constant.RoleConstants;
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
import com.promote.project.promote.domain.ConsumerInfo;
import com.promote.project.promote.domain.StoreInfo;
import com.promote.project.promote.service.ICommonService;
import com.promote.project.promote.service.IConsumerService;
import com.promote.project.promote.service.IProHotelService;
import com.promote.project.promote.service.IProStoreService;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.service.*;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    private IProHotelService hotelService;

    @Autowired
    private IProStoreService storeService;

    @Autowired
    private IConsumerService consumerService;

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IProWhitelistService whitelistService;

    /**
     * 發送驗證碼
     */
    @PostMapping("/common/sendOtp")
    public AjaxResult sendOtp(@RequestBody Map<String, String> request) throws MessagingException {

        // 帳號
        String username = request.get("username");
        // 驗證類型 (1:忘記密碼，2:旅宿業者發抵用券驗證)
        String type = request.get("type");
        // 發送方式 (1:Email,2:手機)
        String method = request.get("method");
        // 手機號碼
        String mobile = request.get("mobile");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(type) || StringUtils.isEmpty(method)) {
            return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
        }
        String msg = commonService.sendOtp(username, type, method, mobile);


        return AjaxResult.success(msg);
    }

    @PostMapping("/regist")
    public AjaxResult regist(@RequestBody SysUser user, HttpServletRequest request) {

        AjaxResult ajax = AjaxResult.success();

        // 旅宿業者註冊
        if ("hotel".equals(user.getRole())) {
            //帳號
            String username = user.getUsername();
            //舊密碼
            String oriPwd = user.getHotel().getOriPwd();
            //新密碼
            String newPwd = user.getPassword();
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(oriPwd)
                    || StringUtils.isEmpty(newPwd)) {
                return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
            }
            hotelService.regist(username, oriPwd, newPwd);
        }
        // 商家
        else if ("store".equals(user.getRole())) {

            // 必填欄位檢核
            if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword()) ||
                    StringUtils.isEmpty(user.getStore().getName()) || StringUtils.isEmpty(user.getStore().getIdentity()) ||
                    StringUtils.isEmpty(user.getStore().getMobile()) || StringUtils.isEmpty(user.getStore().getName()) ||
                    StringUtils.isEmpty(user.getStore().getAddress()) || StringUtils.isEmpty(user.getStore().getBankAccount()) ||
                    StringUtils.isEmpty(user.getStore().getBankAccountName())) {
                return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
            }

            String userAgent = request.getHeader("User-Agent");
            // User-Agent不以 Mozilla 開頭，則代表是從APP發來的請求
            Boolean isFromWeb = userAgent.startsWith("Mozilla");

            // 圖形驗證碼校驗
            // 不是從APP訪問則需要圖形驗證碼
            if (isFromWeb) {
                String uuid = user.getUuid();
                String verifyKey = Constants.CAPTCHA_CODE_KEY + (StringUtils.isNotEmpty(uuid) ? uuid : "");
                String captcha = redisCache.getCacheObject(verifyKey);
                String code = user.getCode();
                if (StringUtils.isEmpty(code)) {
                    throw new CustomException(MessageUtils.message("user.jcaptcha.not.exist"));
                }
                if (!code.equalsIgnoreCase(captcha)) {
                    throw new CaptchaException();
                }
            }

            // 白名單id
            String whitelistId = user.getStore().getWhitelistId();

            // 進行註冊
            storeService.regist(user, whitelistId);

            ajax.put("latitude", user.getStore().getLatitude());
            ajax.put("longitude", user.getStore().getLongitude());
        }
        // 消費者
        else if ("consumer".equals(user.getRole())) {

            Boolean isProxy = user.getConsumer().getHotelId() != null;
            // 正常註冊(非代註冊)才需檢核欄位
            if (!isProxy) {

                // 帳號、密碼、手機
                if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword()) ||
                        StringUtils.isEmpty(user.getConsumer().getMobile())) {
                    return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
                }

                // 密碼規則檢核
                if (!user.getPassword().matches(Constants.PASSWORD_REGEX)) {
                    AjaxResult.error("密碼不符合8-20字元的英數字規則");
                }
            }

            // 必填欄位檢核，姓名、身分證、生日
            if (StringUtils.isEmpty(user.getConsumer().getName()) || StringUtils.isEmpty(user.getConsumer().getIdentity()) ||
                    StringUtils.isNull(user.getConsumer().getBirthday())) {
                return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
            }

            consumerService.regist(user, isProxy);
        } else {
            return AjaxResult.error("角色指定不正確");
        }

        return ajax;
    }


    /**
     * 登入方法
     */
    @PostMapping("/common/login")
    public AjaxResult login(@RequestBody SysUser user, HttpServletRequest request) {

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


        String username = user.getUsername();
        String password = user.getPassword();

        // 旅宿業者第一次登入
        ProWhitelist whitelist = whitelistService.selectProWhitelistByUsernameAndPwd(username, password);
        if (StringUtils.isNotNull(whitelist)) {
            ajax.put("whitelistId", whitelist.getId());
            if (!"1".equals(whitelist.getIsRegisted())) {
                hotelService.regist(username, password, password);
            }
        }


        // 生成令牌
        LoginUser loginUser = loginService.login(username, password);
        String token = tokenService.createToken(loginUser);
        ajax.put(Constants.TOKEN, token);

        // 角色
        Long roleId = loginUser.getUser().getRoles().get(0).getRoleId();
        if (RoleConstants.HOTEL_ROLE_ID.equals(roleId)) {
            ajax.put("role", "H");
        } else if (RoleConstants.STORE_ROLE_ID.equals(roleId)) {
            ajax.put("role", "S");
        } else if (RoleConstants.CONSUMER_ROLE_ID.equals(roleId)) {
            ajax.put("role", "C");
        }

        // 是否強制更改密碼
        if ("0".equals(loginUser.getUser().getPwNeedReset())) {
            ajax.put("forceChgPwd", "N");
        } else {
            ajax.put("forceChgPwd", "Y");
        }

        return ajax;
    }


    /**
     * 忘記密碼，驗證並更新密碼
     *
     * @param sysUser 使用者資料
     * @return 結果
     */
    @PostMapping("/common/forgetPwd")
    public AjaxResult forgetPwd(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        //新密碼
        String newPwd = request.get("newPwd");
        //確認新密碼
        String checkNewPwd = request.get("checkNewPwd");
        //驗證碼
        String code = request.get("code");

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
     *
     * @return 結果
     */
    @PostMapping("/common/resetPwd")
    public AjaxResult resetPwd(@RequestBody Map<String, String> request) {

        // String oldPassword = request.get("oldPwd");
        String newPwd = request.get("newPwd");
        String checkNewPwd = request.get("checkNewPwd");

        if (!newPwd.equals(checkNewPwd)) {
            return AjaxResult.error("兩次輸入的密碼不一致");
        }

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String username = loginUser.getUsername();
        /* String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return AjaxResult.error("修改密碼失敗，舊密碼錯誤");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return AjaxResult.error("新密碼不能與舊密碼相同");
        }*/
        if (userService.resetUserPwd(username, SecurityUtils.encryptPassword(newPwd)) > 0) {
            // 更新快取使用者密碼
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPwd));
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

    /**
     * 基本資料查詢
     *
     * @return 結果
     */
    @PostMapping("/getUserProfile")
    public AjaxResult getUserProfile(@RequestBody(required = false) Map<String, Object> request) {
        AjaxResult ajax = AjaxResult.success();
        String userType = null;
        String username = null;
        String identity = null;
        if(request != null){
            userType = (String)request.get("userType");
            username = (String)request.get("username");
            identity = (String)request.get("identity");
        }
        SysUser user = SecurityUtils.getLoginUser().getUser();
        //判斷角色
        String role = user.getRoles().get(0).getRoleKey();
//        role = "customerService";
        if("customerService".equals(role)){
            //客服
            if(StringUtils.isEmpty(userType)){
                return AjaxResult.error("資料類型需輸入");
            }
            if(StringUtils.isEmpty(username) && StringUtils.isEmpty(identity)){
                return AjaxResult.error("帳號,統編/身分證號/居留證號至少需輸入一項");
            }
            if("S".equalsIgnoreCase(userType)){
                //查店家
                List<Map<String, Object>> list = storeService.getByUnameIdentity(username,identity);
                if(StringUtils.isNull(list) || list.size() == 0){
                    return AjaxResult.success("查無資料");
                }
                ajax.put("store",list);
                return ajax;
            }else if("C".equalsIgnoreCase(userType)){
                //查消費者
                List<Map<String, Object>> list = consumerService.getByUnameIdentity(username,identity);
                if(StringUtils.isNull(list) || list.size() == 0){
                    return AjaxResult.success("查無資料");
                }
                ajax.put("consumer",list);
                return ajax;
            }else if("H".equalsIgnoreCase(userType)){
                //查旅宿業者
                Map<String, Object> map = hotelService.getByUnameIdentity(username,identity);
                if(StringUtils.isNull(map)){
                    return AjaxResult.success("查無資料");
                }
                ajax.put("hotel",map);
                return ajax;
            }
        }
        if("hostel".equals(role)){
            //旅宿業者查消費者
            if(StringUtils.isEmpty(identity)){
                return AjaxResult.error("身分證號/居留證號需輸入");
            }
            //查消費者
            List<Map<String, Object>> list = consumerService.getByIdentity(identity);
            if(StringUtils.isNull(list) || list.size() == 0){
                return AjaxResult.success("查無資料");
            }
            ajax.put("consumer",list);
            return ajax;
        }
        if("store".equals(role)){
            //店家
            ajax.put("store",storeService.getByUsername(user.getUsername()));
            return ajax;
        }
        if("consumer".equals(role)){
            //消費者
            ajax.put("consumer",consumerService.getByUsername(user.getUsername()));
            return ajax;
        }
        return AjaxResult.error("目前登入者無權進行基本資料查詢");
    }

    /**
     *基本資料修改
     *
     * @param sysUser 請求
     * @return
     */
    @PostMapping("/updateProfile")
    public AjaxResult updateProfile(@RequestBody SysUser sysUser) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        //判斷角色
        String role = user.getRoles().get(0).getRoleKey();
        //登入者id
        Long userId = user.getUserId();
        //登入者帳號
        String username = user.getUsername();
        //資料類型
        String userType = null;
        if("customerService".equals(role)){
            //客服
            userType = sysUser.getUserType();
            if(StringUtils.isEmpty(userType)){
                return AjaxResult.error("需輸入資料類型");
            }
        }
        if("hostel".equals(role)){
            //SA奕凱:目前不處理旅宿
        }else if("store".equals(role) || ("customerService".equals(role) && "S".equals(userType))){
            //店家
            StoreInfo storeInfo = sysUser.getStore();
            if("customerService".equals(role)){
                if(StringUtils.isNull(storeInfo.getUserId()) || StringUtils.isEmpty(storeInfo.getUsername())){
                    return AjaxResult.error("需輸入userId及帳號");
                }
                userId = storeInfo.getUserId();
                username = storeInfo.getUsername();
            }
            storeInfo.setUserId(userId);
            storeInfo.setUsername(username);
            storeService.updateStoreInfo(storeInfo);
            return AjaxResult.success("更新成功");
        }else if("consumer".equals(role) || ("customerService".equals(role) && "C".equals(userType))){
            //消費者
            ConsumerInfo consumer = sysUser.getConsumer();
            if("customerService".equals(role)){
                if(StringUtils.isNull(consumer.getUserId()) || StringUtils.isEmpty(consumer.getUsername())){
                    return AjaxResult.error("需輸入userId及帳號");
                }
                userId = consumer.getUserId();
                username = consumer.getUsername();
            }
            consumer.setUserId(userId);
            consumer.setUsername(username);
            consumerService.updateConsumerInfo(consumer);
            return AjaxResult.success("更新成功");
        }
        return AjaxResult.error("目前登入者無權進行基本資料修改");
    }
}
