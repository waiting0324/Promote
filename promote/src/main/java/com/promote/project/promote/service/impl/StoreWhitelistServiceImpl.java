package com.promote.project.promote.service.impl;

import com.promote.project.promote.domain.StoreWhitelist;
import com.promote.project.promote.mapper.StoreWhitelistMapper;
import com.promote.project.promote.service.StoreWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 店家白名單檔Service實作
 *
 * @author 6592 曾培晃
 * @date 2020-05-06
 */
@Service
public class StoreWhitelistServiceImpl implements StoreWhitelistService {

    @Autowired
    StoreWhitelistMapper storeWhitelistMapper;

    /**
     * 根據店家代碼查詢店家白名單檔
     *
     * @param storeUid 店家代碼
     * @return 白名單檔
     */
    @Override
    public StoreWhitelist getStoreWhitelistById(String storeUid) {
        return storeWhitelistMapper.getStoreWhitelistById(storeUid);
    }

    /**
     * 新增店家白名單檔
     *
     * @param storeWhitelist 店家白名單檔
     * @return 結果
     */
    @Override
    public int insertStoreWhitelist(StoreWhitelist storeWhitelist) {
        return storeWhitelistMapper.insertStoreWhitelist(storeWhitelist);
    }


    /**
     * 修改店家白名單檔
     *
     * @param storeWhitelist 店家白名單檔
     * @return 結果
     */
    @Override
    public int updateStoreWhitelist(StoreWhitelist storeWhitelist) {
        return storeWhitelistMapper.updateStoreWhitelist(storeWhitelist);
    }
}
