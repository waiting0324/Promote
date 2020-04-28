package com.promote.project.promote.service;

import com.promote.project.system.domain.SysUser;


/**
 * 店家 服務層
 *
 * @author 曾培晃
 */
public interface IProStoreService {


    /**
     * 商家註冊
     * @param user 商家基本資訊
     * @param isAgreeTerms 是否同意註冊條款
     * @param whitelistId 白名單ID
     */
    public void regist(SysUser user, String isAgreeTerms, String whitelistId);


    /**
     * 修改店家基本資料
     *
     * @param sysUser 使用者資料
     * @return 結果
     */
    int updateStoreInfo(SysUser sysUser);
}
