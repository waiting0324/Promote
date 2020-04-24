package com.promote.project.promote.service.impl;

import com.promote.common.constant.RoleConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void regist(SysUser user, String whitelistId) {

        if (StringUtils.isNotNull(userMapper.selectUserByUsername(user.getUsername()))) {
            throw new CustomException("該帳號已被使用");
        }

        // 白名單驗證
        ProWhitelist white = proWhitelistMapper.selectProWhitelistById(whitelistId);
        if (StringUtils.isNull(white)) {
            throw new CustomException("白名單內並無此店家");
        }
        if (user.getIdentity().equals(white.getTaxNo())) {
            throw new CustomException("填寫的統編與白名單資料不一致");
        }

        SysUser insertUser = new SysUser();
        insertUser.setUsername(user.getUsername());
        insertUser.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        insertUser.setName(user.getName());
        insertUser.setIdentity(user.getIdentity());
        insertUser.setPhonenumber(user.getPhonenumber().replace("-", ""));
        insertUser.setStoreName(user.getStoreName());
        insertUser.setAddress(user.getAddress());
        insertUser.setBankAccount(user.getBankAccount());
        insertUser.setBankAccountName(user.getBankAccountName());
        insertUser.setIsAgreeTerms(user.getIsAgreeTerms());

        // insertUser.setBirthday("20200101"); //TODO FIX
        // 插入User表
        userMapper.insertUser(insertUser);

        // 處理角色問題
        List<SysUserRole> userRoleList = new ArrayList<>();
        SysUserRole ur = new SysUserRole();
        ur.setUserId(insertUser.getUserId());
        ur.setRoleId(RoleConstants.STORE_ROLE_ID);
        userRoleList.add(ur);
        userRoleMapper.batchUserRole(userRoleList);

        //將白名單更新為已同意註冊條款
        white.setIsAgreeTerms("1");

        //將白名單更新為已註冊
        white.setIsRegisted("1");
        white.setUpdateTime(DateUtils.getNowDate());
        proWhitelistMapper.updateProWhitelist(white);
    }

    @Override
    public int updateStoreInfo(SysUser sysUser) {
        return userMapper.updateUser(sysUser);
    }


}
