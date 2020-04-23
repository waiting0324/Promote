package com.promote.project.promote.service;

import com.promote.project.system.domain.SysUser;
import org.springframework.transaction.annotation.Transactional;

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
    SysUser selectByIdentity(String identity);

    /**
     * 消費者註冊
     *
     * @param userName 帳號
     * @param password 密碼
     * @param name 姓名
     * @param identity 身分證/居留證/統一編號
     * @param phonenumber 手機號碼
     * @param birthday 生日
     */
    public void regist(String userName, String password, String name, String identity, String phonenumber, String birthday);
}
