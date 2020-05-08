package com.promote.project.promote.service;

import com.promote.project.promote.domain.Coupon;
import com.promote.project.promote.domain.HotelWhitelist;
import com.promote.project.promote.domain.ProWhitelist;

import java.util.List;

/**
 * 旅宿白名單檔Service介面
 * 
 * @author 6592 曾培晃
 * @date 2020-05-06
 */
public interface HotelWhitelistService
{
    /**
     * 根據旅宿業者代號查詢旅宿白名單檔
     * 
     * @param dataId 旅宿業者代號
     * @return 白名單檔
     */
    public HotelWhitelist getHotelWhitelistById(String dataId);

    /**
     * 新增旅宿白名單檔
     *
     * @param hotelWhitelist 旅宿白名單檔
     * @return 結果
     */
    public int insertHotelWhitelist(HotelWhitelist hotelWhitelist);

    /**
     * 修改旅宿白名單檔
     *
     * @param hotelWhitelist 旅宿白名單檔
     * @return 結果
     */
    public int updateHotelWhitelist(HotelWhitelist hotelWhitelist);
}
