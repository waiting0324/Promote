package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.StoreInfo;

import java.util.List;

/**
 * 店家基本資料Mapper介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public interface StoreInfoMapper
{
    /**
     * 查詢店家基本資料
     *
     * @param userId 店家基本資料ID
     * @return 店家基本資料
     */
    public StoreInfo selectStoreInfoById(Long userId);

    /**
     * 查詢店家基本資料列表
     *
     * @param storeInfo 店家基本資料
     * @return 店家基本資料集合
     */
    public List<StoreInfo> selectStoreInfoList(StoreInfo storeInfo);

    /**
     * 新增店家基本資料
     *
     * @param storeInfo 店家基本資料
     * @return 結果
     */
    public int insertStoreInfo(StoreInfo storeInfo);

    /**
     * 修改店家基本資料
     *
     * @param storeInfo 店家基本資料
     * @return 結果
     */
    public int updateStoreInfo(StoreInfo storeInfo);

    /**
     * 刪除店家基本資料
     *
     * @param userId 店家基本資料ID
     * @return 結果
     */
    public int deleteStoreInfoById(Long userId);

    /**
     * 批量刪除店家基本資料
     *
     * @param userIds 需要刪除的資料ID
     * @return 結果
     */
    public int deleteStoreInfoByIds(Long[] userIds);
}