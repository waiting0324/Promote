package com.promote.project.system.mapper;

import com.promote.project.system.domain.ProWhitelist;
import com.promote.project.system.domain.SysConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 白名單 資料層
 * 
 * @author 曾培晃
 */
public interface ProWhitelistMapper
{
    /**
     * 根據帳號,密碼取得旅宿業者資料
     *
     * @param acct 帳號
     * @param pwd 密碼
     * @return 結果
     */
    public ProWhitelist selectProWhitelistByAcctPwd(@Param("acct") String acct, @Param("pwd") String pwd);


}