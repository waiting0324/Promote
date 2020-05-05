package com.promote.project.promote.service.impl;

import com.promote.common.constant.RoleConstants;
import com.promote.common.constant.StoreTypeConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.MessageUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.domain.StoreInfo;
import com.promote.project.promote.mapper.ProWhitelistMapper;
import com.promote.project.promote.mapper.StoreInfoMapper;
import com.promote.project.promote.service.IProStoreService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.domain.SysUserRole;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 店家 服務層實現
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
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private RedisCache redisCache;


    /**
     * 店家註冊
     *
     * @param user         商家基本資訊
     * @param isAgreeTerms
     * @param whitelistId  白名單ID
     */
    @Transactional
    @Override
    public void regist(SysUser user, String isAgreeTerms, String whitelistId) {

        if (StringUtils.isNotNull(userMapper.selectUserByUsername(user.getUsername()))) {
            throw new CustomException("該帳號已被使用");
        }

        // 白名單驗證
        ProWhitelist white = proWhitelistMapper.selectProWhitelistById(whitelistId);
        if (StringUtils.isNull(white)) {
            throw new CustomException("白名單內並無此店家");
        }
        if (user.getIdentity().equals(white.getTaxNo())) {
            throw new CustomException("填寫的統編與白名單資料不一致");
        }

        // 處理使用者資訊
        SysUser insertUser = new SysUser();
        insertUser.setUsername(user.getUsername());
        insertUser.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        insertUser.setIdentity(user.getIdentity());
        insertUser.setMobile(user.getMobile().replace("-", ""));
        // TODO EMAIL ??

        // 插入User表
        userMapper.insertUser(insertUser);


        // 處理商家基本資訊
        StoreInfo storeInfo = user.getStoreInfo();
        StoreInfo insertStoreInfo = new StoreInfo();

        insertStoreInfo.setUserId(insertUser.getUserId());
        insertStoreInfo.setName(storeInfo.getName());
        insertStoreInfo.setAddress(storeInfo.getAddress());
        insertStoreInfo.setBankAccountName(storeInfo.getBankAccountName());
        insertStoreInfo.setBankAccount(storeInfo.getBankAccount());
        insertStoreInfo.setAddress(storeInfo.getAddress());
        insertStoreInfo.setIsAgreeTerms(isAgreeTerms);
        insertStoreInfo.setAgreeTime(DateUtils.getNowDate());
        // 狀態 待驗證
        insertStoreInfo.setStatus(StoreTypeConstants.STATUS_UNVERIFIED);
        // 不強制變更密碼
        insertStoreInfo.setPwNeedReset(StoreTypeConstants.UN_FORCE_CHANGE_PWD);

        // 處理商家類型 type
        String type = "";
        // 夜市
        if ("1".equals(white.getIsNMarket())) {
            type += "0,";
        }
        // 餐廳
        if ("1".equals(white.getIsFoodbeverage())) {
            type += "1,";
        }
        // 商圈
        if ("1".equals(white.getIsSightseeing()) || "1".equals(white.getIsTMarket())) {
            type += "2,";
        }
        // 藝文
        if ("1".equals(white.getIsCulture())) {
            type += "3,";
        }
        if (type.endsWith(",")) {
            type = type.substring(0, type.length() - 1);
        }
        insertStoreInfo.setType(type);

        // TODO 經緯度

        // 插入商家資訊表
        storeInfoMapper.insertStoreInfo(insertStoreInfo);


        // 處理角色問題
        List<SysUserRole> userRoleList = new ArrayList<>();
        SysUserRole ur = new SysUserRole();
        ur.setUserId(insertUser.getUserId());
        ur.setRoleId(RoleConstants.STORE_ROLE_ID);
        userRoleList.add(ur);
        userRoleMapper.batchUserRole(userRoleList);

        //將白名單更新為已同意註冊條款
        white.setIsAgreeTerms("1");

        //將白名單更新為已註冊
        white.setIsRegisted("1");
        white.setUpdateTime(DateUtils.getNowDate());
        proWhitelistMapper.updateProWhitelist(white);

    }

    /**
     * 修改店家基本資料
     *
     * @param user 使用者資料
     * @return 結果
     */
    @Transactional
    @Override
    public LoginUser updateStoreInfo(SysUser user) {
//        Long userId = user.getUserId();

//        StoreInfo storeInfo = new StoreInfo();
//        storeInfo.setUserId(userId);
        //從Spring Security取得的資料
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser updUser = loginUser.getUser();
        StoreInfo storeInfo = updUser.getStoreInfo();
        //從Client端取得的資料
        StoreInfo storeInfoTmp = user.getStoreInfo();
        boolean needUpdate = false;

        // 商家名稱
        String name = storeInfoTmp.getName();
        if (StringUtils.isNotEmpty(name)) {
            needUpdate = true;
            storeInfo.setName(name);
        }

        // 商家地址
        String address = storeInfoTmp.getAddress();
        if (StringUtils.isNotEmpty(address)) {
            needUpdate = true;
            storeInfo.setAddress(address);
        }
        if(needUpdate){
            //更新店家基本資料
            int result = storeInfoMapper.updateStoreInfo(storeInfo);
            if (result < 0) {
                throw new CustomException(MessageUtils.message("pro.err.update.store.fail"));
            }
            needUpdate = false;
        }
//        SysUser updUser = new SysUser();
//        updUser.setUserId(userId);
        String mobile = user.getMobile();
        if (StringUtils.isNotEmpty(mobile) && !mobile.contains("*")) {
            needUpdate = true;
            updUser.setMobile(mobile);
        }

        // 密碼
        String password = user.getPassword();
        String confirmPwd = (String) user.getParams().get("confirmPwd");
        if (StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(confirmPwd)) {

            if (!password.equals(confirmPwd)) {
                throw new CustomException("兩次輸入的密碼不一致");
            }
            // 設置密碼屬性
            updUser.setPassword(SecurityUtils.encryptPassword(password));
        }

        // 更新使用者資訊
        if(needUpdate){
            //更新使用者資料
            int result = userMapper.updateUser(updUser);
            if (result < 0) {
                throw new CustomException(MessageUtils.message("pro.err.update.store.fail"));
            }
        }
        return loginUser;
    }

    /**
     * 取得店家基本資料
     *
     * @param userId 商家的user_id
     * @return 店家基本資料
     */
    @Override
    public StoreInfo getStoreInfo(Long userId) {
        return storeInfoMapper.selectStoreInfoById(userId);
    }
}
