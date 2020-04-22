package com.promote.project.promote.service.impl;

import com.promote.common.constant.RoleConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.EmailUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.security.service.SysLoginService;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.mapper.ProWhitelistMapper;
import com.promote.project.promote.service.IProStoreService;
import com.promote.project.promote.service.ISysHostelService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.domain.SysUserRole;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
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

    @Autowired
    private SysLoginService loginService;

    /**
     * 旅宿業者註冊
     *
     * @param username 帳號
     * @param oldPwd   舊密碼
     * @param newPwd   新密碼
     * @return 結果
     */
    @Override
    public void regist(String username, String oldPwd, String newPwd) {

        // 用預設帳號密碼從白名單中取出資料
        ProWhitelist white = proWhitelistMapper.selectProWhitelistByUsernameAndPwd(username, oldPwd);

        if (StringUtils.isNull(white)) {
            throw new CustomException("預設帳號或預設密碼不正確");
        }

        if ("1".equals(white.getIsRegisted())) {
            throw new CustomException("此預設帳號已經被註冊過");
        }

        if (StringUtils.isNotNull(userMapper.selectUserByUserName(username))) {
            throw new CustomException("該帳號在使用者表中已經存在");
        }

        if (newPwd.equals(oldPwd)) {
            throw new CustomException("新密碼不可使用預設密碼");
        }

        // 將白名單資料轉為使用者資料
        SysUser user = new SysUser();
        user.setUserName(username);
        user.setIdentity(white.getTaxNo());
        user.setPassword(SecurityUtils.encryptPassword(newPwd));
        user.setName(white.getName());
        user.setBirthday("20200101");
        user.setAddress(white.getAddress());
        user.setPhonenumber(white.getPhonenumber().replace("-", ""));
        user.setEmail(white.getEmail());
        user.setIsAgreeTerms(white.getIsAgreeTerms());

        // 插入User表
        userMapper.insertUser(user);

        // 處理角色問題
        List<SysUserRole> userRoleList = new ArrayList<>();
        SysUserRole ur = new SysUserRole();
        ur.setUserId(user.getUserId());
        ur.setRoleId(RoleConstants.HOSTEL_ROLE_ID);
        userRoleList.add(ur);
        userRoleMapper.batchUserRole(userRoleList);

        // 將白名單更新為已註冊
        white.setIsRegisted("1");
        proWhitelistMapper.updateProWhitelist(white);
    }

    @Override
    public String login(String username, String password, String code, String uuid) {

        // 先查詢是否是第一次登入
        ProWhitelist white = proWhitelistMapper.selectProWhitelistByUsernameAndPwd(username, password);

        if (StringUtils.isNotNull(white) && !"1".equals(white.getIsRegisted())) {
            throw new CustomException("旅宿業者第一次登入，請先跳轉到旅宿業者註冊頁面");
        } else if (StringUtils.isNotNull(white) && "1".equals(white.getIsRegisted())) {
            throw new CustomException("此預設帳號密碼已經完成註冊，請使用新的密碼進行登入");
        }

        // 非第一次登入，則正常進行登入
        String token = loginService.login(username, password, code, uuid);

        return token;
    }

    /**
     * 店家註冊
     *
     * @param userName 帳號
     * @param password 密碼
     * @param name 姓名/商店名稱
     * @param identity 身分證/居留證/統一編號
     * @param phonenumber 手機號碼
     * @param storeName 商家實際店名
     * @param address 商家地址
     * @param bankAccount 銀行帳戶
     * @param bankAccountName 銀行戶名
     */
    @Override
    public void regist(String userName, String password, String name, String identity, String phonenumber, String storeName, String address, String bankAccount, String bankAccountName) {

        if (StringUtils.isNotNull(userMapper.selectUserByUserName(userName))) {
            throw new CustomException("該帳號在使用者表中已經存在");
        }
        SysUser user = new SysUser();
        user.setUserName(userName);
        user.setPassword(SecurityUtils.encryptPassword(password));
        user.setName(name);
        user.setIdentity(identity);
        user.setPhonenumber(phonenumber.replace("-", ""));
        
        user.setBirthday("20200101");
//        user.setAddress(white.getAddress());
//        ;
//        user.setEmail(white.getEmail());
//        user.setIsAgreeTerms(white.getIsAgreeTerms());


    }
}
