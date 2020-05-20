package com.promote.project.promote.service.impl;

import com.promote.common.constant.RoleConstants;
import com.promote.common.constant.StoreTypeConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.MessageUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.redis.RedisCache;
import com.promote.framework.security.LoginUser;
import com.promote.project.promote.domain.DailyConsume;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.domain.StoreHisMail;
import com.promote.project.promote.domain.StoreInfo;
import com.promote.project.promote.mapper.DailyConsumeMapper;
import com.promote.project.promote.mapper.ProStoreHisMailMapper;
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

import java.util.*;

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

    @Autowired
    private DailyConsumeMapper dailyConsumeMapper;

    @Autowired
    private ProStoreHisMailMapper proStoreHisMailMapper;

    /**
     * 店家註冊
     *  @param user         商家基本資訊
     * @param whitelistId  白名單ID
     */
    @Transactional
    @Override
    public void regist(SysUser user, String whitelistId) {

        if (StringUtils.isNotNull(userMapper.selectUserByUsername(user.getUsername()))) {
            throw new CustomException("該帳號已被使用");
        }

        // 白名單驗證
        ProWhitelist white = proWhitelistMapper.selectProWhitelistById(whitelistId);
        if (StringUtils.isNull(white)) {
            throw new CustomException("白名單內並無此店家");
        }
        if (!user.getStore().getIdentity().equals(white.getTaxNo())) {
            throw new CustomException("填寫的統編與白名單資料不一致");
        }

        // 處理使用者資訊
        SysUser insertUser = new SysUser();
        insertUser.setUsername(user.getUsername());
        insertUser.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        insertUser.setIdentity(user.getStore().getIdentity());
        insertUser.setMobile(user.getStore().getMobile());
        insertUser.setEmail(user.getEmail());

        // 插入User表
        userMapper.insertUser(insertUser);


        // 處理商家基本資訊
        StoreInfo storeInfo = user.getStore();
        StoreInfo insertStoreInfo = new StoreInfo();

        insertStoreInfo.setUserId(insertUser.getUserId());
        insertStoreInfo.setName(storeInfo.getName());
        insertStoreInfo.setAddress(storeInfo.getAddress());
        insertStoreInfo.setBankAccountName(storeInfo.getBankAccountName());
        insertStoreInfo.setBankAccount(storeInfo.getBankAccount());
        insertStoreInfo.setAddress(storeInfo.getAddress());
        insertStoreInfo.setIsAgreeTerms("1");
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
        //從Spring Security取得的資料
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser updUser = loginUser.getUser();
        StoreInfo storeInfo = updUser.getStore();
        //從Client端取得的資料
        StoreInfo storeInfoTmp = user.getStore();
        //判斷是否要做update
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
        if (needUpdate) {
            //更新店家基本資料
            int result = storeInfoMapper.updateStoreInfo(storeInfo);
            if (result < 0) {
                throw new CustomException(MessageUtils.message("pro.err.update.store.fail"));
            }
            needUpdate = false;
        }
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
            needUpdate = true;
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

    /**
     * 當前商家收款紀錄總覽
     *
     * @return 結果
     */
    @Override
    public Map<String, Object> getRecdMoneyRecord() {
        StoreInfo storeInfo = SecurityUtils.getLoginUser().getUser().getStore();
        Map<String, Object> recdMoneyRecordMap = new HashMap<String, Object>();
        if (storeInfo != null) {
            //店家id
            List<Map<String, Object>> dayList = new ArrayList<Map<String, Object>>();
            recdMoneyRecordMap.put("days", dayList);
            Long storeId = storeInfo.getUserId();
            String[] weeks = {"(一)", "(二)", "(三)", "(四)", "(五)", "(六)", "(日)"};
            //取今天是星期幾
            int weekDay = getWeekDay(null);
            Calendar begin = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            //換算開始日期
            if (weekDay == 1) {
                begin.add(Calendar.DATE, -7);
            } else if (weekDay == 2) {
                begin.add(Calendar.DATE, -1);
            } else {
                begin.add(Calendar.DATE, weekDay - (weekDay * 2 - 1));
            }
            //換算結束日期
            end.add(Calendar.DATE, -1);
            //開始日期
            String beginDate = DateUtils.parseDateToStr("yyyy-MM-dd", begin.getTime());
            //結束日期
            String endDate = DateUtils.parseDateToStr("yyyy-MM-dd", end.getTime());
            //取出每日日期及其收入
            List<DailyConsume> recdMoneyList = dailyConsumeMapper.getRecdMoneyByTimeSpan(storeId, beginDate, endDate);
            for (DailyConsume dailyConsume : recdMoneyList) {
                weekDay = getWeekDay(dailyConsume.getConsumeTime());
                Map<String, Object> dayAmountMap = new HashMap<String, Object>();
                dayAmountMap.put(DateUtils.parseDateToStr("yyyy/MM/dd", dailyConsume.getConsumeTime()) + weeks[weekDay - 1], dailyConsume.getCouponAmount());
                dayList.add(dayAmountMap);
            }
            //取得店家總收入
            Map<String, Object> totalAmtMap = dailyConsumeMapper.getTotalAmtByStoreId(storeId);
            Long totalAmt = 0L;
            if (StringUtils.isNotNull(totalAmtMap)) {
                totalAmt = (Long) totalAmtMap.get("totalAmt");
            }
            recdMoneyRecordMap.put("totalAmt", totalAmt);
        }
        return recdMoneyRecordMap;
    }


    /**
     *客服查詢店家
     *
     * @param username 帳號
     * @param identity 身分證號或居留證號
     * @return 結果
     */
    @Override
    public List<Map<String, Object>> getByUnameIdentity(String username, String identity) {
        return storeInfoMapper.getByUnameIdentity(username, identity);
    }

    /**
     *店家查詢自己
     *
     * @param username 帳號
     * @return 結果
     */
    @Override
    public Map<String, Object> getByUsername(String username) {
        return storeInfoMapper.getByUsername(username).get(0);
    }

    /**
     * 店家基本資料修改
     *
     * @param storeInfo 店家基本資料
     * @return 結果
     */
    @Transactional
    @Override
    public void updateStoreInfo(StoreInfo storeInfo) {
        //更新StoreInfo
        StoreInfo storeInfoTmp =  new StoreInfo();
        storeInfoTmp.setUserId(storeInfo.getUserId());
        storeInfoTmp.setName(storeInfo.getName());
        storeInfoTmp.setOwner(storeInfo.getOwner());
        storeInfoTmp.setBankAccountName(storeInfo.getBankAccountName());
        storeInfoTmp.setBankAccount(storeInfo.getBankAccount());
        storeInfoTmp.setBankAchCode(storeInfo.getBankAchCode());
        storeInfoTmp.setAddress(storeInfo.getAddress());
        int result = storeInfoMapper.updateStoreInfo(storeInfoTmp);
        if(result < 0){
            throw new CustomException("更新失敗");
        }
        //更新SysUser
        SysUser sysUser = new SysUser();
        sysUser.setUserId(storeInfo.getUserId());
        sysUser.setUsername(storeInfo.getUsername());
        sysUser.setMobile(storeInfo.getMobile());
        result = userMapper.updateUser(sysUser);
        if(result < 0){
            throw new CustomException("更新失敗");
        }
    }

    /**
     * 申請歷史明細(APP專用)
     *
     * @param storeHisMail
     * @return
     */
    @Override
    public int mailTxHistory(StoreHisMail storeHisMail) {
        int sum = proStoreHisMailMapper.insertProStoreHisMail(storeHisMail);
        return sum;
    }

    /**
     * 查詢店家查詢明細郵件表(未處理)
     *
     * @param
     * @return
     */
    @Override
    public List<Map<String, Object>> queryProStoreHisMailToStatusForZeroList() {
        List<Map<String, Object>> resultsList = proStoreHisMailMapper.queryProStoreHisMailToStatusForZeroList();
        return resultsList;
    }

    /**
     * 取得星期幾
     *
     * @param date 日期
     * @return
     */
    private int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
        if (StringUtils.isNotNull(date)) {
            calendar.setTime(date);
        }
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if (isFirstSunday) {
            weekDay = weekDay - 1;
            if (weekDay == 0) {
                weekDay = 7;
            }
        }
        return weekDay;
    }
}
