package com.promote.project.promote.controller;

import com.promote.common.constant.Constants;
import com.promote.common.utils.MessageUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.service.IProHotelService;
import com.promote.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 旅宿業者 控制層
 *
 * @author 曾培晃
 */
@RestController
@RequestMapping("/hostel")
public class HotelController extends BaseController {

    @Autowired
    private IProHotelService hostelService;

    @Autowired
    private TokenService tokenService;


    /**
     * 旅宿業者註冊
     *
     * @param sysUser 使用者資料
     * @return 結果
     */
    @PostMapping("/regist")
    public AjaxResult regist(@RequestBody SysUser sysUser) {
        //帳號
        String username = sysUser.getUsername();
        Map<String, Object> params = sysUser.getParams();
        //舊密碼
        String oriPwd = (String) params.get("oriPwd");
        //新密碼
        String newPwd = (String) params.get("newPwd");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(oriPwd)
                || StringUtils.isEmpty(newPwd)) {
            return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
        }

        hostelService.regist(username, oriPwd, newPwd);

        return AjaxResult.success();
    }

    /**
     * 旅宿業者 登入
     *
     * @param sysUser 使用者資料
     * @return 結果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody SysUser sysUser) {
        AjaxResult ajax = AjaxResult.success();
        //帳號
        String username = sysUser.getUsername();
        //密碼
        String password = sysUser.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return AjaxResult.error(MessageUtils.message("pro.err.columns.not.enter"));
        }
        Map<String, Object> params = sysUser.getParams();
        String code = (String) params.get("code");
        String uuid = (String) params.get("uuid");
        String token = hostelService.login(username, password, code, uuid);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

}
