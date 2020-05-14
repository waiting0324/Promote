package com.promote.project.promote.service;

import org.apache.ibatis.annotations.Param;

import java.util.*;


/**
 * 每日消費統計檔 Service介面
 *
 * @author 6592 曾培晃
 */
public interface IDailyConsumeService {

    /**
     * 新增每日消費統計檔
     *
     * @param consumeTime  使用日期
     * @param storeId      商家的user_id
     * @param couponAmount 抵用券總金額
     * @return 結果
     */
    public int insertDailyConsume(Date consumeTime, Long storeId, Long couponAmount);

    /**
     * 週結交易查詢
     *
     * @param params  Map條件
     * @return 結果
     */
    public List<Map<String, Object>> queryDailyConsumeForDay(@Param("storeId") Long storeId, @Param("beginDay") String beginDay, @Param("endDay") String endDay);

}
