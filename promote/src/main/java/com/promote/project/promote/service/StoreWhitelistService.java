package com.promote.project.promote.service;

import com.promote.project.promote.domain.StoreWhitelist;

/**
 * 店家白名單檔Service介面
 * 
 * @author 6592 曾培晃
 * @date 2020-05-06
 */
public interface StoreWhitelistService
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
