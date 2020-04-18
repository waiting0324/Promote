package com.promote.project.system.service;

import com.promote.project.system.domain.ProWhitelist;
import com.promote.project.system.domain.SysConfig;

import java.util.List;

/**
 * 旅宿業者 服務層
 * 
 * @author 曾培晃
 */
public interface ISysHostelService
{
    /**
     * 旅宿業者註冊
     *
     * @param acct 帳號
     * @param pwd 密碼
     * @return 結果
     */
    public int regist(String acct, String pwd);

    /**
     * 取得旅宿業者
     *
     * @param acct 帳號
     * @param pwd 密碼
     * @return 結果
     */
    public ProWhitelist selectProWhitelistByAcctPwd(String acct, String pwd);

}
