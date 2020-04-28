package com.promote.project.promote.service;

import com.promote.project.promote.domain.Coupon;
import com.promote.project.system.domain.SysUser;

import java.util.List;

/**
 * 抵用券Service介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-22
 */
public interface ICouponService
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
     * @param user 要發放抵用券的使用者
     * @param code 簡訊驗證碼
     * @return
     */
    int sendCoupon(SysUser user, String code);

    /**
     *正掃(消費者掃商家)
     *
     * @param couponIds 抵用券序號
     * @param type 商家類型
     * @param sysUser 使用者資料(消費者)
     */
    void postiveScan(String[] couponIds,Long type,SysUser sysUser);

    /**
     * 反掃(商家掃消費者)
     *
     * @param id 組抵用券序號
     * @param sysUser 使用者資料(店家)
     */
    void reverseScan(String id,SysUser sysUser);
}
