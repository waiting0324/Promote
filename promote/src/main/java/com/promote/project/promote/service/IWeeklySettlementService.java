package com.promote.project.promote.service;

import com.promote.project.promote.domain.HotelWhitelist;
import com.promote.project.promote.domain.WeeklySettlement;

import java.util.List;

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

}
