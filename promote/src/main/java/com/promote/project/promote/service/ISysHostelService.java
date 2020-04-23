package com.promote.project.promote.service;

import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.system.domain.SysUser;

/**
 * 旅宿業者 服務層
 *
 * @author 曾培晃
 */
public interface ISysHostelService {

    /**
     * 旅宿業者註冊
     *
     * @param username 帳號
     * @param oldPwd  舊密碼
     * @param newPwd  新密碼
     * @return 結果
     */
    public void regist(String username, String oldPwd, String newPwd);

    /**
     * 取得旅宿業者
     *
     * @param acct 帳號
     * @param pwd  密碼
     * @return 結果
     */
    public ProWhitelist selectProWhitelistByAcctPwd(String acct, String pwd);

    /**
     * 修改旅宿業者密碼
     *
     * @param user   使用者資訊
     * @param newPwd 新密碼
     * @return 結果
     */
    public int resetPwd(SysUser user, String newPwd);


    /**
     * 忘記密碼_發送驗證碼
     *
     * @param sysUser 使用者資料
     * @return 結果
     */
    public String sendOtpEmail(SysUser sysUser);

    /**
     * 旅宿業者 登入
     * @param username 帳號
     * @param password 密碼
     * @param code  驗證碼
     * @param uuid  驗證碼KEY
     * @return  身分令牌
     */
    String login(String username, String password, String code, String uuid);
}
