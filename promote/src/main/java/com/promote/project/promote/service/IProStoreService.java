package com.promote.project.promote.service;

import com.promote.framework.security.LoginUser;
import com.promote.project.promote.domain.StoreInfo;
import com.promote.project.system.domain.SysUser;

import java.util.List;
import java.util.Map;


/**
 * 店家 服務層
 *
 * @author 曾培晃
 */
public interface IProStoreService {


    /**
     * 商家註冊
     *  @param user         商家基本資訊
     * @param whitelistId  白名單ID
     */
    public void regist(SysUser user, String whitelistId);


    /**
     * 修改店家基本資料
     *
     * @param sysUser 使用者資料
     * @return 結果
     */
    LoginUser updateStoreInfo(SysUser sysUser);

    /**
     * 取得店家基本資料
     *
     * @param userId 商家的user_id
     * @return 店家基本資料
     */
    StoreInfo getStoreInfo(Long userId);


    /**
     * 當前商家收款紀錄總覽
     *
     * @return
     */
    Map<String, Object> getRecdMoneyRecord();

    /**
     *客服查詢店家
     *
     * @param username 帳號
     * @param identity 身分證號或居留證號
     * @return 結果
     */
    List<Map<String, Object>> getByUnameIdentity(String username, String identity);


    /**
     *店家查詢自己
     *
     * @param username 帳號
     * @return 結果
     */
    Map<String, Object> getByUsername(String username);

    /**
     * 修改店家基本資料
     *
     * @param storeInfo 店家基本資料物件
     * @return 結果
     */
    void updateStoreInfo(StoreInfo storeInfo);
}
