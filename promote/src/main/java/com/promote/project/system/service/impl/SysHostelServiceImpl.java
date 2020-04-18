package com.promote.project.system.service.impl;

import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.project.system.domain.ProWhitelist;
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
        if (StringUtils.isNotEmpty(acct) && StringUtils.isNotEmpty(pwd)) {
            ProWhitelist proWhitelist = selectProWhitelistByAcctPwd(acct, pwd);
            if (StringUtils.isNotNull(proWhitelist)) {
                proWhitelist.setWhitelistPwd(SecurityUtils.encryptPassword(pwd));
                return userMapper.insertUserByProWhitelist(proWhitelist);
            }
        }
        return 0;
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
}
