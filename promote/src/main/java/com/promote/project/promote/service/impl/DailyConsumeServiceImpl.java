package com.promote.project.promote.service.impl;

import com.promote.common.utils.DateUtils;
import com.promote.project.promote.domain.DailyConsume;
import com.promote.project.promote.domain.ProNews;
import com.promote.project.promote.mapper.DailyConsumeMapper;
import com.promote.project.promote.mapper.ProNewsMapper;
import com.promote.project.promote.service.IDailyConsumeService;
import com.promote.project.promote.service.IProNewsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 每日消費統計檔 Service介面實作
 *
 * @author 6592 曾培晃
 */
@Service
public class DailyConsumeServiceImpl implements IDailyConsumeService {

    @Autowired
    DailyConsumeMapper dailyConsumeMapper;

    /**
     * 新增每日消費統計檔
     *
     * @param consumeTime  使用日期
     * @param storeId      商家的user_id
     * @param couponAmount 抵用券總金額
     * @return 結果
     */
    @Override
    public int insertDailyConsume(Date consumeTime, Long storeId, Long couponAmount) {
        DailyConsume dailyConsume = new DailyConsume();
        dailyConsume.setConsumeTime(consumeTime);
        dailyConsume.setStoreId(storeId);
        dailyConsume.setCouponAmount(couponAmount);
        return dailyConsumeMapper.insertDailyConsume(dailyConsume);
    }

    /**
     * 週結交易查詢
     *
     * @param params  Map條件
     * @return 結果
     */
    @Override
    public List<Map<String, Object>> queryDailyConsumeForDay(@Param("storeId") Long storeId, @Param("beginDay") String beginDay, @Param("endDay") String endDay) {

        List<Map<String, Object>> resultsList = dailyConsumeMapper.queryDailyConsumeForDay(storeId, beginDay, endDay);
        return resultsList;
    }

    /**
     * 新增每日消費統計檔
     *
     * @param
     * @return 結果
     */
    @Override
    public int insertDailyConsume(DailyConsume dailyConsume) {
        return dailyConsumeMapper.insertDailyConsume(dailyConsume);
    }
}
