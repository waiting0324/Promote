package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.CouponConsume;

import java.util.List;

/**
 * 消費記錄檔Mapper介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public interface CouponConsumeMapper
{
    /**
     * 查詢消費記錄檔
     *
     * @param couponId 消費記錄檔ID
     * @return 消費記錄檔
     */
    public CouponConsume selectCouponConsumeById(String couponId);

    /**
     * 查詢消費記錄檔列表
     *
     * @param couponConsume 消費記錄檔
     * @return 消費記錄檔集合
     */
    public List<CouponConsume> selectCouponConsumeList(CouponConsume couponConsume);

    /**
     * 新增消費記錄檔
     *
     * @param couponConsume 消費記錄檔
     * @return 結果
     */
    public int insertCouponConsume(CouponConsume couponConsume);

    /**
     * 修改消費記錄檔
     *
     * @param couponConsume 消費記錄檔
     * @return 結果
     */
    public int updateCouponConsume(CouponConsume couponConsume);

    /**
     * 刪除消費記錄檔
     *
     * @param couponId 消費記錄檔ID
     * @return 結果
     */
    public int deleteCouponConsumeById(String couponId);

    /**
     * 批量刪除消費記錄檔
     *
     * @param couponIds 需要刪除的資料ID
     * @return 結果
     */
    public int deleteCouponConsumeByIds(String[] couponIds);

    public List<CouponConsume> selectConsumptionList(Long consumerId);
}