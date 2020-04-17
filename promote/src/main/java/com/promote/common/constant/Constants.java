package com.promote.common.constant;

import io.jsonwebtoken.Claims;

/**
 * 通用常量資訊
 * 
 * @author ruoyi
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";
    
    /**
     * 通用成功標識
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失敗標識
     */
    public static final String FAIL = "1";

    /**
     * 登入成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 登出
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登入失敗
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 驗證碼 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登入使用者 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 驗證碼有效期（分鐘）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌字首
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌字首
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 使用者ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 使用者名稱
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 使用者頭像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 建立時間
     */
    public static final String JWT_CREATED = "created";

    /**
     * 使用者許可權
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 資源對映路徑 字首
     */
    public static final String RESOURCE_PREFIX = "/profile";
}
