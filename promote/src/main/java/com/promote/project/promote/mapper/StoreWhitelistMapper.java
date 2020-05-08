package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.StoreWhitelist;

/**
 * 店家白名單檔 Mapper
 * 
 * @author 曾培晃
 */
public interface StoreWhitelistMapper
{
    /**
     * 根據店家代碼查詢店家白名單檔
     *
     * @param storeUid 店家代碼
     * @return 白名單檔
     */
    public StoreWhitelist getStoreWhitelistById(String storeUid);

    /**
     * 新增店家白名單檔
     *
     * @param storeWhitelist 店家白名單檔
     * @return 結果
     */
    public int insertStoreWhitelist(StoreWhitelist storeWhitelist);

    /**
     * 修改店家白名單檔
     *
     * @param storeWhitelist 店家白名單檔
     * @return 結果
     */
    public int updateStoreWhitelist(StoreWhitelist storeWhitelist);

}