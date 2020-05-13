package com.promote.project.promote.service;

import java.util.List;
import java.util.Map;

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

    /**
     *客服查詢旅宿
     *
     * @param username 帳號
     * @param identity 身分證號或居留證號
     * @return 結果
     */
    Map<String, Object> getByUnameIdentity(String username, String identity);

}
