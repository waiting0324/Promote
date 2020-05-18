package com.promote.project.promote.service;

import com.promote.project.promote.domain.HotelWhitelist;
import com.promote.project.promote.domain.WeeklySettlement;
import com.promote.project.promote.domain.WeeklySettlementDetail;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 旅宿白名單檔Service介面
 * 
 * @author 6382 卓奕昌
 * @date 2020-0512
 */
public interface IWeeklySettlementService {
    /**
     * 重置列印狀態
     * 
     * @param weeklySettlementList 周結檔是否周結
     * @return 白名單檔
     */
    public int updateWeeklySettlement(List<WeeklySettlement> weeklySettlementList);

    /**
     * 週結交易查詢
     *
     * @param weeklySettlement 周結檔是否周結
     * @return 白名單檔
     */
    public List<Map<String, Object>> queryWeeklySettlementList(@Param("storeId") Long storeId);

    /**
     * 週結明細查詢
     *
     * @param weeklySettlementDteail 周結檔是否周結
     * @return 白名單檔
     */
    public List<Map<String, Object>> queryWeeklySettlementDetailList(WeeklySettlementDetail weeklySettlementDteail);

    /**
     * 新增周結明細檔
     *
     * @param weeklySettlementDetail
     * @return
     */
    public int insertWeeklySettlementDetail(WeeklySettlementDetail weeklySettlementDetail);
}
