package com.promote.project.promote.service.impl;

import com.promote.common.utils.DateUtils;
import com.promote.project.promote.domain.HotelWhitelist;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.mapper.HotelWhitelistMapper;
import com.promote.project.promote.mapper.ProWhitelistMapper;
import com.promote.project.promote.service.HotelWhitelistService;
import com.promote.project.promote.service.IProWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 旅宿白名單檔Service實作
 *
 * @author 6592 曾培晃
 * @date 2020-05-06
 */
@Service
public class HotelWhitelistServiceImpl implements HotelWhitelistService {

    @Autowired
    HotelWhitelistMapper hotelWhitelistMapper;

    /**
     * 根據旅宿業者代號查詢旅宿白名單檔
     *
     * @param dataId 旅宿業者代號
     * @return 白名單檔
     */
    @Override
    public HotelWhitelist getHotelWhitelistById(String dataId) {
        return hotelWhitelistMapper.getHotelWhitelistById(dataId);
    }

    /**
     * 新增旅宿白名單檔
     *
     * @param hotelWhitelist 旅宿白名單檔
     * @return 結果
     */
    @Override
    public int insertHotelWhitelist(HotelWhitelist hotelWhitelist) {
        return hotelWhitelistMapper.insertHotelWhitelist(hotelWhitelist);
    }

    /**
     * 修改旅宿白名單檔
     *
     * @param hotelWhitelist 旅宿白名單檔
     * @return 結果
     */
    @Override
    public int updateHotelWhitelist(HotelWhitelist hotelWhitelist) {
        return hotelWhitelistMapper.updateHotelWhitelist(hotelWhitelist);
    }


}
