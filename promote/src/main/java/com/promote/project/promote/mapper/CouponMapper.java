package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.Coupon;

import java.util.List;

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
}