package com.promote.project.system.service.impl;

import com.promote.common.exception.CustomException;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.project.system.domain.ProWhitelist;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.mapper.ProWhitelistMapper;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.system.service.ISysHostelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            proWhitelist.setWhitelistPwd(SecurityUtils.encryptPassword(pwd));
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
     * @param user 使用者資訊
     * @param newPwd 新密碼
     * @return 結果
     */
    @Override
    @Transactional
    public int resetPwd(SysUser user, String newPwd) {
        Long userId= user.getUserId();
        String birthDay = user.getBirthday();
        return userMapper.resetPwd(userId,birthDay,newPwd);
    }
}
