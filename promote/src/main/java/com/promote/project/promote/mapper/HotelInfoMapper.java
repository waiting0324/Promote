package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.HotelInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 旅宿業基本資料Mapper介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public interface HotelInfoMapper
{
    /**
     * 查詢旅宿業基本資料
     *
     * @param userId 旅宿業基本資料ID
     * @return 旅宿業基本資料
     */
    public HotelInfo selectHotelInfoById(Long userId);

    /**
     * 查詢旅宿業基本資料列表
     *
     * @param hotelInfo 旅宿業基本資料
     * @return 旅宿業基本資料集合
     */
    public List<HotelInfo> selectHotelInfoList(HotelInfo hotelInfo);

    /**
     * 新增旅宿業基本資料
     *
     * @param hotelInfo 旅宿業基本資料
     * @return 結果
     */
    public int insertHotelInfo(HotelInfo hotelInfo);

    /**
     * 修改旅宿業基本資料
     *
     * @param hotelInfo 旅宿業基本資料
     * @return 結果
     */
    public int updateHotelInfo(HotelInfo hotelInfo);

    /**
     * 刪除旅宿業基本資料
     *
     * @param userId 旅宿業基本資料ID
     * @return 結果
     */
    public int deleteHotelInfoById(Long userId);

    /**
     * 批量刪除旅宿業基本資料
     *
     * @param userIds 需要刪除的資料ID
     * @return 結果
     */
    public int deleteHotelInfoByIds(Long[] userIds);

    /**
     * 客服查詢旅宿
     *
     * @param username 帳號
     * @param identity 身分證號或居留證號
     * @return 結果
     */
    List<Map<String, Object>> getByUnameIdentity(@Param("username") String username, @Param("identity") String identity);
}