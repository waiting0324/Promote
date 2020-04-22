package com.promote.project.promote.service.impl;

import com.promote.common.constant.RoleConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.EmailUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.mapper.ProWhitelistMapper;
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
public class SysHostelServiceImpl implements ISysHostelService {

    @Autowired
    ProWhitelistMapper proWhitelistMapper;

    @Autowired
    SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    /**
     * 旅宿業者註冊
     *
     * @param username 帳號
     * @param oldPwd   舊密碼
     * @param newPwd   新密碼
     * @return 結果
     */
    @Override
    public int regist(String username, String oldPwd, String newPwd) {

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

        // 將白名單資料轉為使用者資料
        SysUser user = new SysUser();
        user.setUserName(username);
        user.setIdentity(white.getTaxNo());
        user.setPassword(SecurityUtils.encryptPassword(newPwd));
        user.setName(white.getName());
        user.setBirthday("20200101");
        user.setAddress(white.getAddress());
        user.setPhonenumber(white.getPhonenumber());
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

        return 1;
    }

    /**
     * 取得旅宿業者
     *
     * @param acct 帳號
     * @param pwd  密碼
     * @return 結果
     */
    @Override
    public ProWhitelist selectProWhitelistByAcctPwd(String acct, String pwd) {
        if (StringUtils.isNotEmpty(acct) && StringUtils.isNotEmpty(pwd)) {
            return proWhitelistMapper.selectProWhitelistByUsernameAndPwd(acct, pwd);
        }
        return null;
    }


    /**
     * 修改旅宿業者密碼
     *
     * @param user   使用者資訊
     * @param newPwd 新密碼
     * @return 結果
     */
    @Override
    @Transactional
    public int resetPwd(SysUser user, String newPwd) {
        Long userId = user.getUserId();
        String birthDay = user.getBirthday();
        return userMapper.resetPwd(userId, birthDay, newPwd);
    }


    /**
     * 忘記密碼_發送驗證碼
     *
     * @param sysUser 使用者資料
     * @return 結果
     */
    @Override
    public String sendOtpEmail(SysUser sysUser) {
        String validCode = createValidNo(4);
        sysUser = userMapper.selectUserByIdBirthday(sysUser);
        if (sysUser != null) {
            String email = sysUser.getEmail();
            String phonenumber = sysUser.getPhonenumber();
            if (StringUtils.isEmpty(email) && StringUtils.isEmpty(phonenumber)) {
                throw new CustomException("查無email及電話");
            }
            if (StringUtils.isNotEmpty(email)) {
                String content = "您已經請求重置密碼,確認碼為: " + validCode;
                try {
                    EmailUtils.sendEmail(email, "忘記密碼", content);
                } catch (MessagingException e) {
                    throw new CustomException("信箱發送失敗");
                }
            }
            if (StringUtils.isNotEmpty(phonenumber)) {
                //TODO 發OTP到電話號碼
            }
            return validCode;
        }
        throw new CustomException("查無此人");
    }


    /**
     * 創造驗證碼
     *
     * @param digit 位數
     * @return 結果
     */
    private String createValidNo(int digit) {
        String number = "";
        for (int i = 0; i < digit; i++) {
            number += ((int) (Math.random() * 10));
        }
        return number;
    }
}
