package com.promote.project.promote.service.impl;

import com.promote.project.promote.domain.WeeklySettlement;
import com.promote.project.promote.mapper.WeeklySettlementMapper;
import com.promote.project.promote.service.IWeeklySettlementService;
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


    @Override
    public int updateWeeklySettlement(List<WeeklySettlement> weeklySettlementList) {
        int sum = 0;
        for(int i =0; i< weeklySettlementList.size();i++){
            sum = weeklySettlementMapper.updateWeeklySettlement(weeklySettlementList.get(i));
        }

        return sum;
    }
}