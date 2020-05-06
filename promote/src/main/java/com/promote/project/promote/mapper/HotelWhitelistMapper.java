package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.HotelWhitelist;
import com.promote.project.promote.domain.ProWhitelist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 旅宿白名單檔 Mapper
 * 
 * @author 曾培晃
 */
public interface HotelWhitelistMapper
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