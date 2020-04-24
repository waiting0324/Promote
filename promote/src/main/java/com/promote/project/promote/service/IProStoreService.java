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
     * @param whitelistId 白名單ID
     */
    public void regist(SysUser user, String whitelistId);



}
