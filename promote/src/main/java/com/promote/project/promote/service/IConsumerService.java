package com.promote.project.promote.service;

import com.promote.framework.security.LoginUser;
import com.promote.project.system.domain.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @author 6550 劉威廷
 * @date 2020/4/22 下午 02:38
 * @description 消費者服務層接口
 */
public interface IConsumerService {

    /**
     * 根據身分證查詢消費者
     * @param identity 身分證
     * @return 使用者Bean
     */
    List<SysUser> selectByIdentity(String identity);

    /**
     * 消費者註冊
     */
    public void regist(SysUser user, Boolean isProxy);

    /**
     * 修改消費者基本資料
     *
     * @param user 使用者資料
     */
    LoginUser updateConsumerInfo(SysUser user);

    /**
     *客服查詢消費者
     *
     * @param username 帳號
     * @param identity 身分證號或居留證號
     * @return 結果
     */
    List<Map<String, Object>> getByUnameIdentity(String username, String identity);


    /**
     *旅宿業者查消費者
     *
     * @param identity 身分證號或居留證號
     * @return 結果
     */
    List<Map<String, Object>> getByIdentity(String identity);

    /**
     *消費者查自己
     *
     * @param username 帳號
     * @return 結果
     */
    Map<String, Object> getByUsername(String username);
}
