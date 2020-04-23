package com.promote.project.promote.service.impl;

import cn.hutool.core.util.StrUtil;
import com.promote.common.constant.Constants;
import com.promote.common.exception.CustomException;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.exception.user.CaptchaExpireException;
import com.promote.common.utils.EmailUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.redis.RedisCache;
import com.promote.project.promote.service.ICommonService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private ISysConfigService configService;

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

    @Override
    public void sendCaptcha(String username, String type) throws MessagingException {

        // 驗證碼
        String verifyCode = String.format("%06d", new Random().nextInt(999999));

        // 驗證碼模板
        String template = configService.selectConfigByKey("common.sendCaptcha.template");
        if (StringUtils.isEmpty(template)) {
            throw new CustomException("尚未指定驗證碼模板，請聯絡管理員，模板KEY: common.sendCaptcha.template");
        }

        // 要發送的訊息
        String msg = StrUtil.format(template, verifyCode);

        // 取得使用者資訊
        SysUser user = userMapper.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            throw new CustomException("該帳號找不到對應的使用者");
        }

        // 使用Email方式驗證
        if (Constants.VERI_CODE_TYPE_EMAIL.equals(type)) {
            if (StringUtils.isEmpty(user.getEmail())) {
                throw new CustomException("您尚未設定Email，故不能使用Email進行重設密碼操作");
            }
            EmailUtils.sendEmail(user.getEmail(), "振興券 - 重設密碼", msg);

            String verifyKey = Constants.VERIFICATION_CODE_KEY + username;
            redisCache.setCacheObject(verifyKey, verifyCode, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        }
        // 使用OTP方式驗證
        else if (Constants.VERI_CODE_TYPE_OTP.equals(type)) {
            // TODO 處理OTP
        } else {
            throw new CustomException("驗證方式選擇錯誤");
        }
    }
}
