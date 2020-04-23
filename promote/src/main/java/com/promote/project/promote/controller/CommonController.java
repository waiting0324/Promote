package com.promote.project.promote.controller;

import com.promote.common.constant.Constants;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 6550 劉威廷
 * @date 2020/4/23 下午 01:58
 * @description 通用功能模塊
 */
@RestController
@RequestMapping("/common")
public class CommonController extends BaseController {

    @Autowired
    ICommonService commonService;


    /**
     * 忘記密碼，驗證並更新密碼
     * @param code 驗證碼
     * @param username 帳號
     * @param newPwd 新密碼
     * @param checkNewPwd 確認新密碼
     */
    @GetMapping("/forgetPwd")
    public AjaxResult forgetPwd(String code, String username, String newPwd, String checkNewPwd) {

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
