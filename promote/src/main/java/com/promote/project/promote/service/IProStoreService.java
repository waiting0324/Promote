package com.promote.project.promote.service;

import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.system.domain.SysUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * 店家 服務層
 *
 * @author 曾培晃
 */
public interface IProStoreService {

    /**
     * 店家註冊
     *
     * @param userName 帳號
     * @param password 密碼
     * @param name 姓名/商店名稱
     * @param identity 身分證/居留證/統一編號
     * @param phonenumber 手機號碼
     * @param storeName 商家實際店名
     * @param address 商家地址
     * @param bankAccount 銀行帳戶
     * @param bankAccountName 銀行戶名
     */
    @Transactional(rollbackFor = Exception.class)
    public void regist(String id, String userName, String password, String identity, String name, String phonenumber, String storeName, String address, String bankAccount, String bankAccountName);



}
