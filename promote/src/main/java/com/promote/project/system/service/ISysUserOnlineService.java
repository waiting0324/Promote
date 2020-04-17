package com.promote.project.system.service;

import com.promote.framework.security.LoginUser;
import com.promote.project.monitor.domain.SysUserOnline;

/**
 * 線上使用者 服務層
 * 
 * @author ruoyi
 */
public interface ISysUserOnlineService
{
    /**
     * 通過登入地址查詢資訊
     * 
     * @param ipaddr 登入地址
     * @param user 使用者資訊
     * @return 線上使用者資訊
     */
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user);

    /**
     * 通過使用者名稱查詢資訊
     * 
     * @param userName 使用者名稱
     * @param user 使用者資訊
     * @return 線上使用者資訊
     */
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user);

    /**
     * 通過登入地址/使用者名稱查詢資訊
     * 
     * @param ipaddr 登入地址
     * @param userName 使用者名稱
     * @param user 使用者資訊
     * @return 線上使用者資訊
     */
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user);

    /**
     * 設定線上使用者資訊
     * 
     * @param user 使用者資訊
     * @return 線上使用者
     */
    public SysUserOnline loginUserToUserOnline(LoginUser user);
}
