package com.promote.project.promote.service;

import com.promote.project.system.domain.SysUser;

import java.util.List;

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
    public void regist(SysUser user);

}
