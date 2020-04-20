package com.promote.project.promote.service.impl;

import com.promote.common.exception.CustomException;
import com.promote.common.utils.EmailUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.system.domain.SysUser;
import com.promote.project.promote.mapper.ProWhitelistMapper;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.promote.service.ISysHostelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

/**
 * 旅宿業者 服務層實現
 *
 * @author ruoyi
 */
@Service
public class SysHostelServiceImpl implements ISysHostelService {
    @Autowired
    ProWhitelistMapper hostelMapper;

    @Autowired
    SysUserMapper userMapper;

    /**
     * 旅宿業者註冊
     *
     * @param acct 帳號
     * @param pwd  密碼
     * @return 結果
     */
    @Override
    @Transactional
    public int regist(String acct, String pwd) {
        ProWhitelist proWhitelist = selectProWhitelistByAcctPwd(acct, pwd);
        if (StringUtils.isNotNull(proWhitelist)) {
            proWhitelist.setPassword(SecurityUtils.encryptPassword(pwd));
            return userMapper.insertUserByProWhitelist(proWhitelist);
        }
        throw new CustomException("查無此帳號");
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
            return hostelMapper.selectProWhitelistByAcctPwd(acct, pwd);
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
