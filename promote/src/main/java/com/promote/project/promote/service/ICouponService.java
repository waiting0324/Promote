package com.promote.project.promote.service;

import com.promote.project.promote.domain.ConsumerInfo;
import com.promote.project.promote.domain.Coupon;
import com.promote.project.promote.domain.CouponConsume;
import com.promote.project.system.domain.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

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
    Map<String, Object> applyCoupon(SysUser user, String code);

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
    Integer postiveScan(List<String> couponIds, Long type);

    /**
     * 反掃(商家掃消費者)
     *
     * @param id      組抵用券序號
     */
    Long reverseScan(String id);

    /**
     * 查詢當前消費者的消費紀錄
     *
     * @return 消費紀錄列表
     */
    List<CouponConsume> consumption();

    /**
     * 抵用券發放紀錄查詢
     *
     * @return 結果
     */
    Map<String, Object> overviewCoupons(Long consumerId);

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

    /**
     * 以證號末四碼及兌換碼查詢抵用券
     *
     * @param indentity 身分證號或居留證號末四碼
     * @param printCode 紙本兌換碼
     * @return 結果
     */
    public List<Map<String, Object>> getPrintCoupon(@Param("indentity") String indentity, @Param("printCode") String printCode);

    /**
     * 以證號末四碼及兌換碼查詢抵用券
     *
     * @param printCode 紙本兌換碼
     * @return 結果
     */
    public int updatePrintCoupon(@Param("printCode") String printCode);

    /**
     * 根據是否已使用 ( 0未使用 1已使用 )查找抵用券發放記錄檔
     *
     * @param isUsed 是否已使用 ( 0未使用 1已使用 )
     * @return
     */
    List<Coupon> getCouponByIsUsed(String isUsed,String isReturn);

    /**
     *更新補助額度檔
     *
     * @param sTyepAmt 中企回歸金額
     * @param tTyepAmt 中辦回歸金額
     * @param bTyepAmt 商業司回歸金額
     * @param cTyepAmt 文化部回歸金額
     */
    void updateProFundAmount(List<Coupon> expiredCoupons,int sTyepAmt,int tTyepAmt,int bTyepAmt,int cTyepAmt);
}
