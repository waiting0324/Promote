package com.promote.project.promote.service.impl;

import com.promote.common.constant.ConsumerConstants;
import com.promote.common.constant.CouponConstants;
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
import org.springframework.util.CollectionUtils;

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
    public List<SysUser> selectByIdentity(String identity) {

        // 根據身分證查詢消費者
        List<SysUser> sysUsers = userMapper.selectConsumerByIdentity(identity);

        if (CollectionUtils.isEmpty(sysUsers)) {
            throw new CustomException("該消費者尚未進行註冊");
        }

        return sysUsers;
    }


    /**
     * 消費者註冊
     */
    @Override
    @Transactional
    public void regist(SysUser user) {


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
        int result = userMapper.insertUser(insertUser);
        if(result < 0){
            throw new CustomException("註冊失敗，請聯絡管理員");
        }


        // 設定消費者基本資訊
        ConsumerInfo consumerInfo = new ConsumerInfo();
        consumerInfo.setUserId(insertUser.getUserId());
        consumerInfo.setName(user.getConsumerInfo().getName());
        consumerInfo.setIdentity(user.getIdentity());
        consumerInfo.setBirthday(user.getConsumerInfo().getBirthday());
        // 狀態設為註冊
        consumerInfo.setConsumerStat(ConsumerConstants.STAT_REGISTED);
        // 默認紙本列印抵用券
        consumerInfo.setCouponType(CouponConstants.TYPE_PAPAER);
        // 未列印
        consumerInfo.setCouponPrintType(CouponConstants.UN_PRINTED);

        // 插入消費者資訊表
        consumerInfoMapper.insertConsumerInfo(consumerInfo);


        // 處理角色問題
        List<SysUserRole> userRoleList = new ArrayList<>();
        SysUserRole ur = new SysUserRole();
        ur.setUserId(insertUser.getUserId());
        ur.setRoleId(RoleConstants.CONSUMER_ROLE_ID);
        userRoleList.add(ur);
        result = userRoleMapper.batchUserRole(userRoleList);
        if(result < 0){
            throw new CustomException("註冊失敗，請聯絡管理員");
        }

    }
}
