package com.promote.project.promote.service;

/**
 * 旅宿業者 服務層
 *
 * @author 曾培晃
 */
public interface IProHotelService {

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
     * 旅宿業者 登入
     * @param username 帳號
     * @param password 密碼
     * @param code  驗證碼
     * @param uuid  驗證碼KEY
     * @return  身分令牌
     */
    String login(String username, String password, String code, String uuid);


}
