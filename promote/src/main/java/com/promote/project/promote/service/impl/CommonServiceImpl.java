package com.promote.project.promote.service.impl;

import cn.hutool.core.util.StrUtil;
import com.promote.common.constant.Constants;
import com.promote.common.constant.ConsumerConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.exception.user.CaptchaExpireException;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.manager.AsyncManager;
import com.promote.framework.manager.factory.AsyncFactory;
import com.promote.framework.redis.RedisCache;
import com.promote.project.promote.domain.ConsumerInfo;
import com.promote.project.promote.domain.StoreInfo;
import com.promote.project.promote.mapper.ConsumerInfoMapper;
import com.promote.project.promote.service.ICommonService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ConsumerInfoMapper consumerInfoMapper;

    @Autowired
    private ISysConfigService configService;


    @Override
    public int forgetPwd(String code, String username, String newPwd) {

        // 確認驗證碼是否正確
        String verifyKey = Constants.VERI_CODE_KEY + username;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }

        // 更新密碼
        return userMapper.resetUserPwd(username, SecurityUtils.encryptPassword(newPwd));
    }


    /**
     * 資料遮罩
     *
     * @param sysUser 使用者資料
     * @return 遮罩後的使用者資料
     */
    @Override
    public SysUser hidePersonalInfo(SysUser sysUser) {
        if (sysUser != null) {
            //消費者基本資料
            ConsumerInfo consumerInfo = sysUser.getConsumer();
            if (consumerInfo != null) {
                //姓名
                String nameTmp = consumerInfo.getName();
                if (StringUtils.isNotEmpty(nameTmp)) {
                    //取第一個字
                    StringBuilder name = new StringBuilder(nameTmp.substring(0, 1));
                    int length = nameTmp.length();
                    //把中間字遮罩
                    for (int i = 1; i < length; i++) {
                        if (i == length - 1) {
                            //最後一個字
                            if (length == 2) {
                                //單名 ex.楊過
                                name.append("○");
                            } else {
                                name.append(nameTmp.substring(nameTmp.length() - 1));
                            }
                            break;
                        }
                        name.append("○");
                    }
                    consumerInfo.setName(name.toString());
                }
                //生日
                /*String birthdayTmp = consumerInfo.getBirthday();
                if(StringUtils.isNotEmpty(birthdayTmp)){
                    StringBuilder birthday = new StringBuilder(birthdayTmp.substring(0, birthdayTmp.length() - 2)).append("**");
                    consumerInfo.setBirthday(birthday.toString());
                }*/
            }
            //身分證字號,統編,居留證號
            String identityTmp = sysUser.getIdentity();
            if (StringUtils.isNotEmpty(identityTmp)) {
                StringBuilder identity = new StringBuilder(identityTmp.substring(0, 3));
                int length = identityTmp.length() - 6;
                for (int i = 0; i < length; i++) {
                    identity.append("*");
                }
                identity.append(identityTmp.substring(3 + length));
                sysUser.setIdentity(identity.toString());
            }
            //手機,電話
            String mobileTmp = sysUser.getMobile();
            if (StringUtils.isNotEmpty(mobileTmp)) {
                StringBuilder mobile = new StringBuilder(mobileTmp.substring(0, 4));
                int length = mobileTmp.length() - 7;
                for (int i = 0; i < length; i++) {
                    mobile.append("*");
                }
                mobile.append(mobileTmp.substring(4 + length));
                sysUser.setMobile(mobile.toString());
            }
            //店家基本資料
            StoreInfo storeInfo = sysUser.getStore();
            if (storeInfo != null) {
                //銀行帳號
                String bankAccountTmp = storeInfo.getBankAccount();
                if(StringUtils.isNotEmpty(bankAccountTmp)){
                    StringBuilder bankAccount = new StringBuilder(bankAccountTmp.substring(0, 6)).append("*****").append(bankAccountTmp.substring(11));
                    storeInfo.setBankAccount(bankAccount.toString());
                }
            }
            //電子郵件
            String emailTmp = sysUser.getEmail();
            if (StringUtils.isNotEmpty(emailTmp)) {
                StringBuilder email = new StringBuilder(emailTmp.substring(0, 2));
                int index = emailTmp.indexOf("@");
                for (int i = 0; i < index - 2; i++) {
                    email.append("*");
                }
                email.append(emailTmp.substring(index));
                sysUser.setEmail(email.toString());
            }
            //密碼
            String passwordTmp = sysUser.getPassword();
            if(StringUtils.isNotEmpty(passwordTmp)){
                StringBuilder password = new StringBuilder("");
                for(int i = 0 ; i < passwordTmp.length(); i++){
                    password.append("*");
                }
                sysUser.setPassword(password.toString());
            }
        }
        return sysUser;
    }

    @Override
    @Transactional
    public String sendCaptcha(String username, String type, String method, String mobile) {

        // 返回給前端的資訊
        String result = "";

        // 驗證碼
        String verifyCode = String.format("%04d", new Random().nextInt(9999));

        // 驗證碼模板
        String template = configService.selectConfigByKey("common.sendCaptcha.template");
        if (StringUtils.isEmpty(template)) {
            throw new CustomException("尚未指定驗證碼模板，請聯絡管理員，模板KEY: common.sendCaptcha.template");
        }

        // 要發送的訊息
        String msg = StrUtil.format(template, verifyCode);

        // 取得使用者資訊
        SysUser user = userMapper.selectUserByUsername(username);
        if (StringUtils.isNull(user)) {
            throw new CustomException("該帳號找不到對應的使用者");
        }

        // 忘記密碼，使用Email方式驗證
        if (Constants.VERI_TYPE_FORGET_PWD.equals(type) && Constants.VERI_METHOD_EMAIL.equals(method)) {
            if (StringUtils.isEmpty(user.getEmail())) {
                throw new CustomException("您的帳號未留存email,請使用其他驗證方式", 200);
            }
            AsyncManager.me().execute(AsyncFactory.sendEmail(user.getEmail(), "振興券 - 重設密碼", msg));

            // 驗證碼存入Redis
            String verifyKey = Constants.VERI_CODE_KEY + username;
            redisCache.setCacheObject(verifyKey, verifyCode, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        }
        // 忘記密碼，使用OTP方式驗證
        else if (Constants.VERI_TYPE_FORGET_PWD.equals(type) && Constants.VERI_METHOD_SMS.equals(method)) {
            // TODO 處理OTP
            AsyncManager.me().execute(AsyncFactory.sendSms(mobile, msg));
        }
        // 旅宿業者發送抵用券 OTP 方式驗證
        else if (Constants.VERI_TYPE_SEND_COUPON.equals(type) && Constants.VERI_METHOD_SMS.equals(method)) {

            // 校驗消費者狀態
            ConsumerInfo consumerInfo = consumerInfoMapper.selectConsumerInfoById(user.getUserId());
            if (ConsumerConstants.STAT_SEND.equals(consumerInfo.getConsumerStat())
                    || ConsumerConstants.STAT_PRINT.equals(consumerInfo.getConsumerStat())) {
                throw new CustomException("此消費者已領過抵用券");
            }

            // TODO 處理OTP
            AsyncManager.me().execute(AsyncFactory.sendSms(mobile, msg));

            // 驗證碼存入Redis
            String verifyKey = Constants.VERI_COUPON_SEND_CODE_KEY + username;
            redisCache.setCacheObject(verifyKey, verifyCode, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

            // 更新此消費者狀態
            /*consumerInfo.setConsumerStat(ConsumerConstants.STAT_CONFIRM);
            consumerInfoMapper.updateConsumerInfo(consumerInfo);*/
        } else {
            throw new CustomException("驗證方式選擇錯誤");
        }

        if (Constants.VERI_METHOD_EMAIL.equals(method)) {
            hidePersonalInfo(user);
            result = "驗證碼已發送到" + user.getEmail() + "，驗證碼:" + verifyCode + "(在SMTP能使用後需移除此資訊)";
        } else if (Constants.VERI_METHOD_SMS.equals(method)) {
            user.setMobile(mobile);
            hidePersonalInfo(user);
            result = "驗證碼已發送到" + user.getMobile() + "，驗證碼:" + verifyCode + "(在簡訊API能使用後需移除此資訊)";
        }

        return result;
    }
}