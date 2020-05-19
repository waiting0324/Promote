package com.promote.project.promote.service.impl;

import com.promote.project.promote.domain.WeeklySettlement;
import com.promote.project.promote.domain.WeeklySettlementDetail;
import com.promote.project.promote.mapper.WeeklySettlementDetailMapper;
import com.promote.project.promote.mapper.WeeklySettlementMapper;
import com.promote.project.promote.service.IWeeklySettlementService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @author 6382 卓奕昌
 * @date 2020/5/12 下午 18:30
 * @description 通用功能模塊 服務層
 */
@Service
public class WeeklySettlementServiceImpl implements IWeeklySettlementService {


    @Autowired
    private WeeklySettlementMapper weeklySettlementMapper;

    @Autowired
    private WeeklySettlementDetailMapper weeklySettlementDetailMapper;

    @Override
    public int updateWeeklySettlement(List<WeeklySettlement> weeklySettlementList) {
        int sum = 0;
        for(int i =0; i< weeklySettlementList.size();i++){
            sum = weeklySettlementMapper.updateWeeklySettlement(weeklySettlementList.get(i));
        }

        return sum;
    }

    @Override
    public List<Map<String, Object>> queryWeeklySettlementList(@Param("storeId") Long storeId) {

        List<Map<String, Object>> resultList = weeklySettlementMapper.queryWeeklySettlementList(storeId);
        return resultList;
    }

    @Override
    public List<Map<String, Object>> queryWeeklySettlementDetailList(WeeklySettlementDetail weeklySettlementDteail) {

        List<Map<String, Object>> queryWeeklySettlementDetailList = weeklySettlementDetailMapper.queryWeeklySettlementDetailList(weeklySettlementDteail);
        return queryWeeklySettlementDetailList;
    }

    @Override
    public int insertWeeklySettlementDetail(WeeklySettlementDetail weeklySettlementDetail){
        int sum = weeklySettlementDetailMapper.insertWeeklySettlementDetail(weeklySettlementDetail);
        return sum;
    }

    @Override
    public List<Map<String, Object>> queryLastWeekSettlementDetailList(@Param("beginTime") String beginTime, @Param("endTime") String endTime){
        List<Map<String, Object>> resultsList = weeklySettlementDetailMapper.queryLastWeekSettlementDetailList(beginTime, endTime);
        return resultsList;
    }

    @Override
    public int insertWeeklySettlement(WeeklySettlement weeklySettlement){
        int sum = weeklySettlementMapper.insertWeeklySettlement(weeklySettlement);
        return sum;
    }
}