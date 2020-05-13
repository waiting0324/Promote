package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.CouponConsume;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 消費記錄檔Mapper介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public interface CouponConsumeMapper {
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

    /**
     * 取得時間範圍內的消費記錄檔
     *
     * @param beginDate 開始時間
     * @param endDate   結束時間
     * @return 結果
     */
    List<Map<String,Object>> getTotalAmtByStoreId(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 抵用券消費記錄查詢(WEB介面用)
     *
     * @param storeType 抵用券類型
     * @param startDate 查詢起日
     * @param endDate 查詢迄日
     * @return 結果
     */
    List<Map<String,Object>> transactionHistory(@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("storeType") String storeType,@Param("consumerId") Long consumerId,@Param("storeId") Long storeId);
}