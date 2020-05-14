package com.promote.common.utils;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 6550 劉威廷
 * @date 2020/5/13 下午 05:03
 * @description 簡訊工具類
 */
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsUtil {

    private static String username;

    private static String password;

    public static void sendSms(String mobile, String msg) {
        String result = HttpUtil.get("http://smexpress.mitake.com.tw:9600/SmSendGet.asp?" +
                "username=" + username + "&password=" + password + "&dstaddr=" + mobile
                + "&smbody=" + URLUtil.encode(msg)
        );
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
