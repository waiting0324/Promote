package com.promote.project.promote.service;

import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.domain.ConsumerInfo;
import com.promote.project.promote.domain.Coupon;
import com.promote.project.promote.domain.CouponConsume;
import com.promote.project.system.domain.SysUser;

import java.util.List;
import java.util.Map;

/**
 * 抵用券Service介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-22
 */
public interface ICouponService {
    /**
     * 查詢抵用券
     *
     * @param sn 抵用券ID
     * @return 抵用券
     */
    public Coupon selectCouponById(String sn);

    /**
     * 查詢抵用券列表
     *
     * @param coupon 抵用券
     * @return 抵用券集合
     */
    public List<Coupon> selectCouponList(Coupon coupon);

    /**
     * 新增抵用券
     *
     * @param coupon 抵用券
     * @return 結果
     */
    public int insertCoupon(Coupon coupon);

    /**
     * 修改抵用券
     *
     * @param coupon 抵用券
     * @return 結果
     */
    public int updateCoupon(Coupon coupon);

    /**
     * 批量刪除抵用券
     *
     * @param sns 需要刪除的抵用券ID
     * @return 結果
     */
    public int deleteCouponByIds(String[] sns);

    /**
     * 刪除抵用券資訊
     *
     * @param sn 抵用券ID
     * @return 結果
     */
    public int deleteCouponById(String sn);

    /**
     * 旅宿業者發送抵用券給消費者
     *
     * @param user 要發放抵用券的使用者
     * @param code 簡訊驗證碼
     * @return
     */
    void sendCoupon(SysUser user, String code);

    /**
     * 消費者取得可使用的抵用券
     *
     * @param storeId 商家的user_id
     * @param sysUser 使用者(消費者)資料
     * @return
     */
    Map getConsumerCoupon(Long storeId, SysUser sysUser);

    /**
     * 正掃(消費者掃商家)
     *
     * @param couponIds 抵用券序號
     * @param type      商家類型
     */
    void postiveScan(List<String> couponIds, Long type);

    /**
     * 反掃(商家掃消費者)
     *
     * @param id      組抵用券序號
     * @param sysUser 使用者資料(店家)
     */
    void reverseScan(String id, SysUser sysUser);

    /**
     * 查詢當前消費者的消費紀錄
     *
     * @return 消費紀錄列表
     */
    List<CouponConsume> consumption();

    /**
     * 抵用券總攬
     *
     * @return
     */
    AjaxResult overviewCoupons();

    /**
     * 取得時間範圍內的消費記錄檔
     *
     * @param beginDate 開始時間
     * @param endDate   結束時間
     * @return 結果
     */
    List<Map<String,Object>> getTotalAmtByStoreId(String beginDate, String endDate);

    /**
     * 重置列印狀態
     *
     * @return 結果
     */
    int updateConsumerStart(ConsumerInfo consumerInfo);

    /**
     * 抵用券消費記錄查詢(WEB介面用)
     *
     * @param id 使用者id
     * @param storeType 抵用券類型
     * @param startDate 查詢起日
     * @param endDate 查詢迄日
     * @param rows 每頁筆數
     * @param page 要查詢的頁數
     * @return 結果
     */
    Map<String,Object> transactionHistory(Long id,String role,String storeType,String startDate,String endDate,String rows,String page);
}
