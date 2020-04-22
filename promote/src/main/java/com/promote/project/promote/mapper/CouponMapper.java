package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.Coupon;

import java.util.List;

/**
 * 抵用券Mapper介面
 * 
 * @author 6550 劉威廷
 * @date 2020-04-22
 */
public interface CouponMapper 
{
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
     * 刪除抵用券
     * 
     * @param sn 抵用券ID
     * @return 結果
     */
    public int deleteCouponById(String sn);

    /**
     * 批量刪除抵用券
     * 
     * @param sns 需要刪除的資料ID
     * @return 結果
     */
    public int deleteCouponByIds(String[] sns);

    /**
     * 批量新增抵用券
     * @param couponList
     * @return
     */
    public int insertCouponList(List<Coupon> couponList);
}
