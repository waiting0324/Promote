package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.Coupon;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 抵用券發放記錄檔Mapper介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public interface CouponMapper
{
    /**
     * 查詢抵用券發放記錄檔
     *
     * @param id 抵用券發放記錄檔ID
     * @return 抵用券發放記錄檔
     */
    public Coupon selectCouponById(String id);

    /**
     * 查詢抵用券發放記錄檔列表
     *
     * @param coupon 抵用券發放記錄檔
     * @return 抵用券發放記錄檔集合
     */
    public List<Coupon> selectCouponList(Coupon coupon);

    /**
     * 新增抵用券發放記錄檔
     *
     * @param coupon 抵用券發放記錄檔
     * @return 結果
     */
    public int insertCoupon(Coupon coupon);

    /**
     * 批量新增抵用券
     * @param couponList
     * @return
     */
    public int insertCouponList(List<Coupon> couponList);

    /**
     * 修改抵用券發放記錄檔
     *
     * @param coupon 抵用券發放記錄檔
     * @return 結果
     */
    public int updateCoupon(Coupon coupon);

    /**
     * 刪除抵用券發放記錄檔
     *
     * @param id 抵用券發放記錄檔ID
     * @return 結果
     */
    public int deleteCouponById(String id);

    /**
     * 批量刪除抵用券發放記錄檔
     *
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    public int deleteCouponByIds(String[] ids);

    /**
     * 消費者取得可使用的抵用券
     *
     * @param userId 消費者的user_id
     * @param isUsed 抵用券是否已被使用( 0未使用 1已使用 )
     * @param storeTypes 店家類型
     * @return
     */
    public List<Coupon> getConsumerCoupon(@Param("userId") Long userId,@Param("isUsed") String isUsed,@Param("storeTypes") String[] storeTypes);

    /**
     * 消費者擁有的抵用券總覽
     * @param consumerId
     * @return
     */
    List<Coupon> overviewCoupons(Long consumerId);


    List<Map<String,Object>> getCouponInfo(@Param("userId") Long userId,@Param("storeType") String storeType);
    /**
     * 以證號末四碼及兌換碼查詢抵用券
     * @param indentity
     * @param printCode
     * @return
     */
    List<Map<String, Object>> getPrintCoupon(@Param("indentity") String indentity, @Param("printCode") String printCode);

    int updatePrintCoupon(@Param("printCode") String printCode);


    /**
     * 根據是否已使用 ( 0未使用 1已使用 )查找抵用券發放記錄檔
     *
     * @param isUsed 是否已使用 ( 0未使用 1已使用 )
     * @return
     */
    List<Coupon> getCouponByIsUsed(@Param("isUsed") String isUsed,@Param("isReturn") String isReturn);

    /**
     *取得需要發送推播的抵用券發放記錄檔
     *
     * @param isUsed 是否已使用 ( 0未使用 1已使用 )
     * @param isReturn 是否已回歸總額度
     * @return
     */
    List<Coupon> getNeedRemindCoupon(@Param("isUsed") String isUsed, @Param("isReturn") String isReturn);
}