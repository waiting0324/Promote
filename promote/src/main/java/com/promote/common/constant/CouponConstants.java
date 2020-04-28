package com.promote.common.constant;

/**
 * @author 6550 劉威廷
 * @date 2020/4/23 上午 08:34
 * @description
 */
public class CouponConstants {

    /**
     * 抵用券未發放
     */
    public static final String IS_NOT_PROVIDE = "0";

    /**
     * 抵用券已發放
     */
    public static final String IS_PROVIDE = "1";

    /**
     * 抵用券未列印
     */
    public static final String UN_PRINTED = "0";

    /**
     * 抵用券已列印
     */
    public static final String PRINTED = "1";


    /**
     * 抵用券使用紙本方式發放
     */
    public static final String TYPE_PAPAER = "P";

    /**
     * 抵用券使用電子(虛擬)方式發放
     */
    public static final String TYPE_ELEC = "E";

    /**
     * 抵用券金額
     */
    public static final Long COUPON_AMOUNT = 50l;


    /**
     * 補助機構: 中小企業處
     */
    public static final String FUND_TYPE_SME = "S";

    /**
     * 補助機構: 中部辦公室
     */
    public static final String FUND_TYPE_CENTER_OFFICE = "T";

    /**
     * 補助機構: 商業司
     */
    public static final String FUND_TYPE_BUSINESS_DEPARTMENT = "B";

    /**
     * 補助機構: 商業司
     */
    public static final String FUND_TYPE_MINISTRY_CULTURE = "C";
}
