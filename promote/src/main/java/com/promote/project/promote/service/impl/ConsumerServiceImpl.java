package com.promote.project.promote.service.impl;

import com.promote.common.constant.ConsumerConstants;
import com.promote.common.constant.CouponConstants;
import com.promote.common.constant.RoleConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.MessageUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.security.LoginUser;
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
import java.util.Map;

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
    public void regist(SysUser user, Boolean isProxy) {


        // 旅宿業者代註冊，將身分證當成帳號
        if (isProxy) {
            user.setUsername(user.getIdentity());
        }

        // 檢核帳號是否重複
        if (StringUtils.isNotNull(userMapper.selectUserByUsername(user.getUsername()))) {
            throw new CustomException("該帳號已被使用");
        }

        // 檢核身分證與生日是否重複
        if (StringUtils.isNotNull(userMapper.selectUserByIdentityAndBirthday(user.getIdentity(),
                user.getConsumer().getBirthday()))) {
            throw new CustomException("該身分證已重複註冊");
        }


        // 設定使用者資訊
        SysUser insertUser = new SysUser();
        insertUser.setUsername(user.getUsername());
        // 非代註冊才需要密碼
        if (!isProxy) {
            insertUser.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        }
        insertUser.setMobile(user.getConsumer().getMobile());
        insertUser.setIdentity(user.getConsumer().getIdentity());

        // 插入User表
        int result = userMapper.insertUser(insertUser);
        if(result < 0){
            throw new CustomException("註冊失敗，請聯絡管理員");
        }


        // 設定消費者基本資訊
        ConsumerInfo consumerInfo = new ConsumerInfo();
        consumerInfo.setUserId(insertUser.getUserId());
        consumerInfo.setName(user.getConsumer().getName());
        consumerInfo.setBirthday(user.getConsumer().getBirthday());
        consumerInfo.setHotelId(user.getConsumer().getHotelId());

        // 非旅宿業者代註冊
        if (!isProxy) {
            // 狀態設為 一般註冊
            consumerInfo.setConsumerStat(ConsumerConstants.STAT_REGISTED);
            // 默認紙本列印抵用券
            consumerInfo.setCouponType(CouponConstants.TYPE_ELEC);
        }
        // 旅宿業者代註冊
        else {
            // 狀態設為 旅宿業者代註冊(無手機)
            if (user.getMobile() == null) {
                consumerInfo.setConsumerStat(ConsumerConstants.STAT_REGISTED_PROXY_NO_MOBILE);
                // 默認紙本列印抵用券
                consumerInfo.setCouponType(CouponConstants.TYPE_PAPAER);
            }
            // 旅宿業者代註冊(有手機)
            else {
                consumerInfo.setConsumerStat(ConsumerConstants.STAT_REGISTED_PROXY_MOBILE);
                // 默認紙本列印抵用券
                consumerInfo.setCouponType(CouponConstants.TYPE_ELEC);
            }
        }

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

    /**
     * 修改消費者基本資料
     *
     * @param user 使用者資料
     */
    @Override
    @Transactional
    public LoginUser updateConsumerInfo(SysUser user) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser updUser = loginUser.getUser();
//        SysUser updUser = new SysUser();
//        updUser.setUserId(user.getUserId());
        boolean needUpdate = false;
        String password = user.getPassword();
        if(StringUtils.isNotEmpty(password)){
            needUpdate = true;
            updUser.setPassword(SecurityUtils.encryptPassword(password));
        }
        String mobile = user.getMobile();
        if(StringUtils.isNotEmpty(mobile) && mobile.indexOf("*") == -1){
            needUpdate = true;
            updUser.setMobile(mobile);
        }
        if(needUpdate){
            int result = userMapper.updateUser(updUser);
            if(result < 0){
                throw new CustomException(MessageUtils.message("pro.err.update.consumer.fail"));
            }
        }
        return loginUser;
    }


    /**
     *客服查詢消費者
     *
     * @param username 帳號
     * @param identity 身分證號或居留證號
     * @return 結果
     */
    @Override
    public List<Map<String, Object>> getByUnameIdentity(String username, String identity) {
        return consumerInfoMapper.getByUnameIdentity(username,identity);
    }

    /**
     *旅宿業者查消費者
     *
     * @param identity 身分證號或居留證號
     * @return 結果
     */
    @Override
    public List<Map<String, Object>> getByIdentity(String identity) {
        return consumerInfoMapper.getByIdentity(identity);
    }

    /**
     *消費者查自己
     *
     * @param username 帳號
     * @return 結果
     */
    @Override
    public Map<String, Object> getByUsername(String username) {
        return consumerInfoMapper.getByUsername(username).get(0);
    }

    /**
     * 修改消費者基本資料
     *
     * @param consumer 消費者基本資料
     */
    @Transactional
    @Override
    public void updateConsumerInfo(ConsumerInfo consumer) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(consumer.getUserId());
        sysUser.setUsername(consumer.getUsername());
        sysUser.setEmail(consumer.getEmail());
        sysUser.setMobile(consumer.getMobile());
        int result = userMapper.updateUser(sysUser);
        if(result < 0){
            throw new CustomException("更新失敗");
        }
    }
}
