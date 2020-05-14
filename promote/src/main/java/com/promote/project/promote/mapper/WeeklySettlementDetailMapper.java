package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.WeeklySettlementDetail;

import java.util.*;

/**
 * 週結明細Mapper介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public interface WeeklySettlementDetailMapper
{
    /**
     * 查詢週結明細
     *
     * @param couponId 週結明細ID
     * @return 週結明細
     */
    public WeeklySettlementDetail selectWeeklySettlementDetailById(String couponId);

    /**
     * 查詢週結明細列表
     *
     * @param weeklySettlementDetail 週結明細
     * @return 週結明細集合
     */
    public List<WeeklySettlementDetail> selectWeeklySettlementDetailList(WeeklySettlementDetail weeklySettlementDetail);

    /**
     * 新增週結明細
     *
     * @param weeklySettlementDetail 週結明細
     * @return 結果
     */
    public int insertWeeklySettlementDetail(WeeklySettlementDetail weeklySettlementDetail);

    /**
     * 修改週結明細
     *
     * @param weeklySettlementDetail 週結明細
     * @return 結果
     */
    public int updateWeeklySettlementDetail(WeeklySettlementDetail weeklySettlementDetail);

    /**
     * 刪除週結明細
     *
     * @param couponId 週結明細ID
     * @return 結果
     */
    public int deleteWeeklySettlementDetailById(String couponId);

    /**
     * 批量刪除週結明細
     *
     * @param couponIds 需要刪除的資料ID
     * @return 結果
     */
    public int deleteWeeklySettlementDetailByIds(String[] couponIds);

    /**
     * 週結明細查詢
     *
     * @param weeklySettlementDetail 週結明細
     * @return 週結明細集合
     */
    public List<Map<String, Object>> queryWeeklySettlementDetailList(WeeklySettlementDetail weeklySettlementDetail);
}