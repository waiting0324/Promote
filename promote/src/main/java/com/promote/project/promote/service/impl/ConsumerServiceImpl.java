package com.promote.project.promote.service.impl;

import com.promote.common.constant.RoleConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.project.promote.service.IConsumerService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.domain.SysUserRole;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 6550 劉威廷
 * @date 2020/4/22 下午 02:39
 * @description 消費者 服務層
 */
@Service
public class ConsumerServiceImpl implements IConsumerService {
    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    SysUserMapper userMapper;

    @Override
    public SysUser selectByIdentity(String identity) {

        // 根據身分證查詢消費者
        SysUser sysUser = userMapper.selectConsumerByIdentity(identity);

        if (StringUtils.isNull(sysUser)) {
            throw new CustomException("該消費者尚未進行註冊");
        }

        return sysUser;
    }


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
    @Override
    public void regist(String userName, String password, String name, String identity, String phonenumber, String birthday) {
        if (StringUtils.isNotNull(userMapper.selectUserByUsername(userName))) {
            throw new CustomException("該帳號已被使用");
        }
        SysUser user = new SysUser();
        user.setUsername(userName);
        user.setPassword(SecurityUtils.encryptPassword(password));
        /*user.setName(name);
        user.setIdentity(identity);
        user.setPhonenumber(phonenumber.replace("-", ""));
        user.setBirthday(birthday);
        user.setIsAgreeTerms("1");*/

        // 插入User表
        int result = userMapper.insertUser(user);
        if(result < 0){
            throw new CustomException("註冊失敗，請聯絡管理員");
        }
        // 處理角色問題
        List<SysUserRole> userRoleList = new ArrayList<>();
        SysUserRole ur = new SysUserRole();
        ur.setUserId(user.getUserId());
        ur.setRoleId(RoleConstants.CONSUMER_ROLE_ID);
        userRoleList.add(ur);
        result = userRoleMapper.batchUserRole(userRoleList);
        if(result < 0){
            throw new CustomException("註冊失敗，請聯絡管理員");
        }
    }
}
