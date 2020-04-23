package com.promote.project.promote.service;

import javax.mail.MessagingException;

/**
 * @author 6550 劉威廷
 * @date 2020/4/23 下午 02:07
 * @description 通用驗證服務層接口
 */
public interface ICommonService {

    /**
     * 忘記密碼，驗證並更新密碼
     * @param code 驗證碼
     * @param username 帳號
     * @param newPwd 新密碼
     * @return
     */
    int forgetPwd(String code, String username, String newPwd);

    /**
     * 發送驗證碼
     * @param username 帳號
     * @param type 驗證類型 ( 1:Email 2:簡訊OTP )
     */
    void sendCaptcha(String username, String type) throws MessagingException;
}
