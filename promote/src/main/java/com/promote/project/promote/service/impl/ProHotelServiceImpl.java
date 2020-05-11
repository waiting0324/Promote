package com.promote.project.promote.service.impl;

import com.promote.common.constant.Constants;
import com.promote.common.constant.RoleConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.security.LoginUser;
import com.promote.framework.security.service.SysLoginService;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.domain.HotelInfo;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.mapper.ConsumerInfoMapper;
import com.promote.project.promote.mapper.HotelInfoMapper;
import com.promote.project.promote.mapper.ProWhitelistMapper;
import com.promote.project.promote.service.IProHotelService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.domain.SysUserRole;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 旅宿業者 服務層實現
 *
 * @author ruoyi
 */
@Service
public class ProHotelServiceImpl implements IProHotelService {

    @Autowired
    private ProWhitelistMapper proWhitelistMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private HotelInfoMapper hotelInfoMapper;

    @Autowired
    private ConsumerInfoMapper consumerInfoMapper;

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private TokenService tokenService;

    /**
     * 旅宿業者註冊
     *
     * @param username 帳號
     * @param oldPwd   舊密碼
     * @param newPwd   新密碼
     * @return 結果
     */
    @Override
    @Transactional
    public void regist(String username, String oldPwd, String newPwd) {

        Date nowDate = DateUtils.getNowDate();

        // 用預設帳號密碼從白名單中取出資料
        ProWhitelist white = proWhitelistMapper.selectProWhitelistByUsernameAndPwd(username, oldPwd);

        if (StringUtils.isNull(white)) {
            throw new CustomException("預設帳號或預設密碼不正確");
        }

        if ("1".equals(white.getIsRegisted())) {
            throw new CustomException("此預設帳號已經被註冊過，請使用新密碼進行登入");
        }

        if (StringUtils.isNotNull(userMapper.selectUserByUsername(username))) {
            throw new CustomException("該帳號在使用者表中已經存在");
        }

        if (newPwd.equals(oldPwd)) {
            throw new CustomException("新密碼不可使用預設密碼");
        }

        // 將白名單資料轉為使用者資料
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setIdentity(white.getTaxNo());
        user.setPassword(SecurityUtils.encryptPassword(newPwd));
        user.setEmail(white.getEmail());
        user.setMobile(white.getPhonenumber().replace("-", ""));

        // 插入User表
        userMapper.insertUser(user);

        HotelInfo hotelInfo = new HotelInfo();
        hotelInfo.setUserId(user.getUserId().intValue());
        hotelInfo.setName(white.getName());
        hotelInfo.setAddress(white.getAddress());
        hotelInfo.setLatitude(white.getLatitude());
        hotelInfo.setLongitude(white.getLongitude());
        hotelInfo.setAgreeTime(new Timestamp(nowDate.getTime()));
        hotelInfo.setIsAgreeTerms("1");
        hotelInfo.setIsSupportCoupon("1");
        // TODO isSupportCoupon
        // TODO isAgreeTerms

        // 插入旅宿業者資訊表
        hotelInfoMapper.insertHotelInfo(hotelInfo);


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
        AjaxResult ajax = AjaxResult.success();
        LoginUser loginUser = loginService.login(username, password, "", "");
        String token = tokenService.createToken(loginUser);
        ajax.put(Constants.TOKEN, token);

        return token;
    }


}
