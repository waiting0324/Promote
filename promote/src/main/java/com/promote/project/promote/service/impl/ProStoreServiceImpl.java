package com.promote.project.promote.service.impl;

import com.promote.common.constant.RoleConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.EmailUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.mapper.ProWhitelistMapper;
import com.promote.project.promote.service.IProStoreService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.domain.SysUserRole;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 旅宿業者 服務層實現
 *
 * @author ruoyi
 */
@Service
public class ProStoreServiceImpl implements IProStoreService {

    @Autowired
    private ProWhitelistMapper proWhitelistMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;



    /**
     * 店家註冊
     *
     * @param userName        帳號
     * @param password        密碼
     * @param identity        身分證/居留證/統一編號
     * @param name            姓名/商店名稱
     * @param phonenumber     手機號碼
     * @param storeName       商家實際店名
     * @param address         商家地址
     * @param bankAccount     銀行帳戶
     * @param bankAccountName 銀行戶名
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void regist(String id, String userName, String password, String identity, String name, String phonenumber, String storeName, String address, String bankAccount, String bankAccountName) {
        if (StringUtils.isNotNull(userMapper.selectUserByUserName(userName))) {
            throw new CustomException("該帳號已被使用");
        }
        ProWhitelist proWhitelist = proWhitelistMapper.selectProWhitelistById(id);
        if (StringUtils.isNull(proWhitelist)) {
            throw new CustomException("白名單內並無此店家");
        }
        SysUser user = new SysUser();
        user.setUserName(userName);
        user.setPassword(SecurityUtils.encryptPassword(password));
        user.setName(name);
        user.setIdentity(identity);
        user.setPhonenumber(phonenumber.replace("-", ""));
        user.setStoreName(storeName);
        user.setAddress(address);
        user.setBankAccount(bankAccount);
        user.setBankAccountName(bankAccountName);
        user.setIsAgreeTerms(proWhitelist.getIsAgreeTerms());

        user.setBirthday("20200101"); //TODO FIX
        // 插入User表
        userMapper.insertUser(user);
        // 處理角色問題
        List<SysUserRole> userRoleList = new ArrayList<>();
        SysUserRole ur = new SysUserRole();
        ur.setUserId(user.getUserId());
        ur.setRoleId(RoleConstants.STORE_ROLE_ID);
        userRoleList.add(ur);
        userRoleMapper.batchUserRole(userRoleList);

        //將白名單更新為已同意註冊條款
        proWhitelist.setIsAgreeTerms("1");
        //將白名單更新為已註冊
        proWhitelist.setIsRegisted("1");
        proWhitelist.setUpdateTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
        proWhitelistMapper.updateProWhitelist(proWhitelist);
    }

    @Override
    public int updateStoreInfo(SysUser sysUser) {
        return userMapper.updateUser(sysUser);
    }


}
