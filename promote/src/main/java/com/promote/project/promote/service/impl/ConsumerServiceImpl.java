package com.promote.project.promote.service.impl;

import com.promote.common.constant.RoleConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.project.promote.domain.ConsumerInfo;
import com.promote.project.promote.mapper.ConsumerInfoMapper;
import com.promote.project.promote.service.IConsumerService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.domain.SysUserRole;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ConsumerInfoMapper consumerInfoMapper;

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
     * @param name 姓名
     * @param birthday 生日
     */
    @Override
    @Transactional
    public void regist(SysUser user, String name, String birthday) {


        if (StringUtils.isNotNull(userMapper.selectUserByUsername(user.getUsername()))) {
            throw new CustomException("該帳號已被使用");
        }

        // 設定使用者資訊
        SysUser insertUser = new SysUser();
        insertUser.setUsername(user.getUsername());
        insertUser.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        insertUser.setMobile(user.getMobile().replace("-", ""));
        insertUser.setIdentity(user.getIdentity());

        // 插入User表
        int result = userMapper.insertUser(user);
        if(result < 0){
            throw new CustomException("註冊失敗，請聯絡管理員");
        }


        // 設定消費者基本資訊
        ConsumerInfo consumerInfo = new ConsumerInfo();
        consumerInfo.setUserId(user.getUserId());
        consumerInfo.setName(name);
        consumerInfo.setIdentity(user.getIdentity());
        consumerInfo.setBirthday(birthday);
        // 狀態設為註冊
        consumerInfo.setConsumerStat("1");
        // 默認紙本列印抵用券
        consumerInfo.setCouponType("P");
        // 未列印
        consumerInfo.setCouponPrintType("0");

        // 插入消費者資訊表
        consumerInfoMapper.insertConsumerInfo(consumerInfo);



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
