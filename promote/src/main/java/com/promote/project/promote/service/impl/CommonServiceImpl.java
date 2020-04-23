package com.promote.project.promote.service.impl;

import com.promote.common.constant.Constants;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.exception.user.CaptchaExpireException;
import com.promote.common.utils.SecurityUtils;
import com.promote.framework.redis.RedisCache;
import com.promote.project.promote.service.ICommonService;
import com.promote.project.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 6550 劉威廷
 * @date 2020/4/23 下午 02:10
 * @description 通用功能模塊 服務層
 */
@Service
public class CommonServiceImpl implements ICommonService {


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public int forgetPwd(String code, String username, String newPwd) {

        // 確認驗證碼是否正確
        String verifyKey = Constants.VERIFICATION_CODE_KEY + username;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }

        // 更新密碼
        return userMapper.resetUserPwd(username, SecurityUtils.encryptPassword(newPwd));
    }
}
