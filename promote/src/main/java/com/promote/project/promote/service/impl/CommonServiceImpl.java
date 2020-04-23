package com.promote.project.promote.service.impl;

import com.promote.common.constant.Constants;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.exception.user.CaptchaExpireException;
import com.promote.common.utils.SecurityUtils;
import com.promote.framework.redis.RedisCache;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.service.ICommonService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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


    /**
     * 資料遮罩
     *
     * @param sysUser 使用者資料
     * @param extraField 額外需遮罩的欄位
     * @return 遮罩後的使用者資料
     */
    @Override
    public SysUser hidePersonalInfo(SysUser sysUser,String[] extraField) {
        if(sysUser != null){
            Class c = SysUser.class;
            Field[] fields = c.getDeclaredFields();
            List<String> columnNameList = new ArrayList<String>();
            //預設需遮罩的欄位
            String[] defaultField = {"identity","birthday","userName","password","name","email","phonenumber","bankAccount","bankAccountName"};
            for (Field field : fields) {
                columnNameList.add(field.getName());
            }
        }
        return sysUser;
    }
}
