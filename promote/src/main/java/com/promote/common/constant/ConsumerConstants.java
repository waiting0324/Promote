package com.promote.common.constant;

/**
 * @author 6550 劉威廷
 * @date 2020/4/28 下午 03:03
 * @description 消費者角色 常量
 */
public class ConsumerConstants {

    /**
     * 狀態: 自行註冊
     */
    public static final String STAT_REGISTED = "0";


    /**
     * 狀態: 旅宿業者代註冊(有手機)
     */
    public static final String STAT_REGISTED_PROXY_MOBILE = "1";


    /**
     * 狀態: 旅宿業者代註冊(無手機)
     */
    public static final String STAT_REGISTED_PROXY_NO_MOBILE = "2";

    /**
     * 狀態: 旅宿業確認 (已發送抵用券驗證碼)
     */
    public static final String STAT_CONFIRM = "2";

    /**
     * 狀態: 已核發抵用券
     */
    public static final String STAT_SEND = "3";

    /**
     * 狀態: 紙本抵用券列印中
     */
    public static final String STAT_PRINTING = "4";

    /**
     * 狀態: 紙本抵用券已列印
     */
    public static final String STAT_PRINT = "5";
}
