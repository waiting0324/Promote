package com.promote.project.promote.service;


import com.promote.project.system.domain.SysUser;

import javax.mail.MessagingException;


/**
 * @author 6550 劉威廷
 * @date 2020/4/23 下午 02:07
 * @description 通用驗證服務層接口
 */
public interface ICommonService {

    /**
     * 忘記密碼，驗證並更新密碼
     *
     * @param code     驗證碼
     * @param username 帳號
     * @param newPwd   新密碼
     * @return
     */
    int forgetPwd(String code, String username, String newPwd);

    /**
     * 資料遮罩
     *
     * @param sysUser    使用者資料
     * @return 遮罩後的使用者資料
     */
    SysUser hidePersonalInfo(SysUser sysUser);

    /**
     * 發送驗證碼
     *  @param username 帳號
     * @param type      驗證類型 (1:忘記密碼，2:旅宿業者發抵用券驗證)
     * @param method    發送方式 (1:Email,2:手機)
     * @param mobile    手機號碼
     */
    String sendOtp(String username, String type, String method, String mobile) throws MessagingException;

}
