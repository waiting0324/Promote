package com.promote.project.promote.service.impl;

import com.promote.common.constant.Constants;
import com.promote.common.constant.ConsumerConstants;
import com.promote.common.constant.CouponConstants;
import com.promote.common.constant.StoreTypeConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.exception.user.CaptchaExpireException;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.MessageUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.redis.RedisCache;
import com.promote.framework.security.LoginUser;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.domain.ConsumerInfo;
import com.promote.project.promote.domain.Coupon;
import com.promote.project.promote.domain.CouponConsume;
import com.promote.project.promote.domain.StoreInfo;
import com.promote.project.promote.mapper.ConsumerInfoMapper;
import com.promote.project.promote.mapper.CouponConsumeMapper;
import com.promote.project.promote.mapper.CouponMapper;
import com.promote.project.promote.mapper.StoreInfoMapper;
import com.promote.project.promote.service.ICouponService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 抵用券Service業務層處理
 *
 * @author 6550 劉威廷
 * @date 2020-04-22
 */
@Service
public class CouponServiceImpl implements ICouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ConsumerInfoMapper consumerInfoMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private CouponConsumeMapper couponConsumeMapper;


    /**
     * 查詢抵用券
     *
     * @param sn 抵用券ID
     * @return 抵用券
     */
    @Override
    public Coupon selectCouponById(String sn) {
        return couponMapper.selectCouponById(sn);
    }

    /**
     * 查詢抵用券列表
     *
     * @param coupon 抵用券
     * @return 抵用券
     */
    @Override
    public List<Coupon> selectCouponList(Coupon coupon) {
        return couponMapper.selectCouponList(coupon);
    }

    /**
     * 新增抵用券
     *
     * @param coupon 抵用券
     * @return 結果
     */
    @Override
    public int insertCoupon(Coupon coupon) {
        coupon.setCreateTime(DateUtils.getNowDate());
        return couponMapper.insertCoupon(coupon);
    }

    /**
     * 修改抵用券
     *
     * @param coupon 抵用券
     * @return 結果
     */
    @Override
    public int updateCoupon(Coupon coupon) {
        coupon.setUpdateTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
        return couponMapper.updateCoupon(coupon);
    }

    /**
     * 批量刪除抵用券
     *
     * @param sns 需要刪除的抵用券ID
     * @return 結果
     */
    @Override
    public int deleteCouponByIds(String[] sns) {
        return couponMapper.deleteCouponByIds(sns);
    }

    /**
     * 刪除抵用券資訊
     *
     * @param sn 抵用券ID
     * @return 結果
     */
    @Override
    public int deleteCouponById(String sn) {
        return couponMapper.deleteCouponById(sn);
    }

    @Override
    @Transactional
    public void sendCoupon(SysUser user, String code) {

        // 檢驗是否有消費者ID
        if (StringUtils.isNull(user.getUsername())) {
            throw new CustomException("未指定消費者帳號");
        }
        Long userId = userMapper.selectUserByUsername(user.getUsername()).getUserId();
        String consumerStat = consumerInfoMapper.selectConsumerInfoById(userId).getConsumerStat();

        //校驗是否有輸入手機號碼
        if (StringUtils.isEmpty(user.getMobile())) {
            throw new CustomException("未輸入手機號碼");
        }
        // 校驗是否可以發抵用券
        if (ConsumerConstants.STAT_REGISTED.equals(consumerStat)) {
            throw new CustomException("請先發送OTP驗證碼給該消費者");
        }
        if (ConsumerConstants.STAT_SEND.equals(consumerStat)
            || ConsumerConstants.STAT_PRINT.equals(consumerStat)) {
            throw new CustomException("該消費者已領取過振興券");
        }

        // 驗證OTP驗證碼
        // 從Redis中取出驗證碼
        String verifyKey = Constants.VERI_COUPON_SEND_CODE_KEY + user.getUsername();
        String captcha = redisCache.getCacheObject(verifyKey);
        // redisCache.deleteObject(verifyKey);
        // 比對驗證碼
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }

        Date nowDate = DateUtils.getNowDate();

        // 消費者選擇使用電子(虛擬)方式發放抵用券
        if (CouponConstants.TYPE_ELEC.equals(user.getConsumerInfo().getCouponType())) {

            // 設定抵用券資料，發送抵用券
            List<Coupon> couponList = new ArrayList<>();
            for (int i = 0; i < 16; i++) {

                // 設定抵用券的基本資料
                Coupon coupon = new Coupon();
                coupon.setId(LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd")) + UUID.randomUUID());
                coupon.setUserId(userId);
                coupon.setIsUsed("0");
                coupon.setIssueDate(nowDate);
                coupon.setAmount(CouponConstants.COUPON_AMOUNT);

                // 夜市
                if (i <= 3) {
                    coupon.setStoreType(StoreTypeConstants.NIGHT_MARKET);
                    // 中辦
                    coupon.setFundType(CouponConstants.FUND_TYPE_CENTER_OFFICE);
                }
                // 餐廳
                else if (4 <= i && i <= 7) {
                    coupon.setStoreType(StoreTypeConstants.RESTAURANT);
                    // 商業司
                    coupon.setFundType(CouponConstants.FUND_TYPE_BUSINESS_DEPARTMENT);
                }
                // 商圈
                else if (8 <= i && i <= 11) {
                    coupon.setStoreType(StoreTypeConstants.SHOPPING_AERA);
                    // 中企
                    coupon.setFundType(CouponConstants.FUND_TYPE_SME);
                }
                // 藝文
                else if (12 <= i && i <= 15) {
                    coupon.setStoreType(StoreTypeConstants.ART);
                    // 文化部
                    coupon.setFundType(CouponConstants.FUND_TYPE_MINISTRY_CULTURE);
                }

                couponList.add(coupon);
            }

            // 寫入抵用券表
            couponMapper.insertCouponList(couponList);

        }
        // 消費者選擇使用紙本方式發放抵用券
        else if (CouponConstants.TYPE_PAPAER.equals(user.getConsumerInfo().getCouponType())) {
            // TODO 連接KIOSK
        } else {
            throw new CustomException("消費者選擇發放抵用券的方式不正確");
        }

        // 處理要更新的消費者資料
        ConsumerInfo consumerInfo = new ConsumerInfo();
        consumerInfo.setUserId(userId);
        consumerInfo.setConsumerStat(ConsumerConstants.STAT_SEND);
        consumerInfo.setCouponPrintType(user.getConsumerInfo().getCouponType());
        consumerInfo.setHostelId(SecurityUtils.getLoginUser().getUser().getUserId());
        consumerInfo.setIssueDate(nowDate);

        // 更新消費者資料
        int result = consumerInfoMapper.updateConsumerInfo(consumerInfo);
        if(result < 0){
            throw new CustomException(MessageUtils.message("pro.err.update.consumer.fail"));
        }
        //更新使用者資料
        SysUser updUser = new SysUser();
        updUser.setUserId(userId);
        updUser.setMobile(user.getMobile());
        result = userMapper.updateUser(updUser);
        if(result < 0){
            throw new CustomException(MessageUtils.message("pro.err.update.user.fail"));
        }
    }

    /**
     * 消費者取得可使用的抵用券
     *
     * @param storeId 商家的user_id
     * @param sysUser 使用者(消費者)資料
     * @return
     */
    @Override
    public Map getConsumerCoupon(Long storeId, SysUser sysUser) {

        //店家基本資料
        StoreInfo storeInfo = storeInfoMapper.selectStoreInfoById(storeId);
        if (StringUtils.isNull(storeInfo)) {
            throw new CustomException(MessageUtils.message("pro.err.store.not.find"));
        }

        //店家類型
        String[] storeTypes = storeInfo.getType().split(",");
        List<Coupon> consumerCouponList = couponMapper.getConsumerCoupon(sysUser.getUserId(), "0", storeTypes);
        if (StringUtils.isNull(consumerCouponList) || consumerCouponList.size() == 0) {
            throw new CustomException("無可使用的抵用券");
        }


        Map<String, List> result = new HashMap<>();
        result.put("0", new ArrayList());
        result.put("1", new ArrayList());
        result.put("2", new ArrayList());
        result.put("3", new ArrayList());
        for (Coupon coupon : consumerCouponList) {
            String storeType = coupon.getStoreType();
            result.get(storeType).add(coupon);
        }

        return result;
    }

    /**
     * 正掃(消費者掃商家)
     *
     * @param couponIds 抵用券序號
     * @param storeId   商家的user_id
     */
    @Override
    @Transactional
    public void postiveScan(List<String> couponIds, Long storeId) {

        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();

        for (String couponId : couponIds) {
            Coupon coupon = couponMapper.selectCouponById(couponId);
            if (StringUtils.isNull(coupon)) {
                throw new CustomException(MessageUtils.message("pro.err.coupon.not.exist"));
            }
            if (!coupon.getUserId().equals(userId)) {
                throw new CustomException("該抵用券不屬於當前消費者");
            }
            if ("1".equals(coupon.getIsUsed())) {
                throw new CustomException(MessageUtils.message("pro.err.coupon.used"));
            }
            //店家基本資料
            StoreInfo storeInfo = storeInfoMapper.selectStoreInfoById(storeId);
            if (StringUtils.isNull(storeInfo)) {
                throw new CustomException(MessageUtils.message("pro.err.store.not.find"));
            }
            //店家基本資料-商家類型
            String storeTypes = storeInfo.getType();
            //抵用券發放記錄檔-類別
            String storeType = coupon.getStoreType();
            if (storeTypes.indexOf(storeType) == -1) {
                throw new CustomException(MessageUtils.message("pro.err.coupon.not.match"));
            }
            //抵用券發放記錄檔設為已使用
            coupon.setIsUsed("1");
            //更新抵用券發放記錄檔
            int result = updateCoupon(coupon);
            if (result < 0) {
                throw new CustomException("更新抵用券發放記錄檔失敗，請聯絡管理員");
            }
            //建立消費記錄檔
            CouponConsume couponConsume = new CouponConsume();
            couponConsume.setCouponId(couponId);
            couponConsume.setConsumerId(userId);
            couponConsume.setFundType(coupon.getFundType());
            couponConsume.setStoreId(storeId);
            couponConsume.setConsumeTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
            couponConsume.setStoreType(storeType);
            couponConsume.setAmount(CouponConstants.COUPON_AMOUNT);
            result = couponConsumeMapper.insertCouponConsume(couponConsume);
            if (result < 0) {
                throw new CustomException("新增消費記錄檔失敗，請聯絡管理員");
            }
        }
    }

    /**
     * 反掃(商家掃消費者)
     *
     * @param id      組抵用券序號
     * @param sysUser 使用者資料(店家)
     */
    @Override
    @Transactional
    public void reverseScan(String id, SysUser sysUser) {
        Coupon coupon = couponMapper.selectCouponById(id);
        if (StringUtils.isNull(coupon)) {
            throw new CustomException(MessageUtils.message("pro.err.coupon.not.exist"));
        }
        if ("1".equals(coupon.getIsUsed())) {
            throw new CustomException(MessageUtils.message("pro.err.coupon.used"));
        }
        StoreInfo storeInfo = sysUser.getStoreInfo();
        if (StringUtils.isNull(storeInfo)) {
            throw new CustomException(MessageUtils.message("pro.err.store.not.find"));
        }
        //店家基本資料-商家類型
        String storeTypes = storeInfo.getType();
        //抵用券發放記錄檔-類別
        String storeType = coupon.getStoreType();
        if (storeTypes.indexOf(storeType) == -1) {
            throw new CustomException(MessageUtils.message("pro.err.coupon.not.match"));
        }
        //抵用券發放記錄檔設為已使用
        coupon.setIsUsed("1");
        //更新抵用券發放記錄檔
        int result = updateCoupon(coupon);
        if (result < 0) {
            throw new CustomException("更新抵用券發放記錄檔失敗，請聯絡管理員");
        }
        //建立消費記錄檔
        CouponConsume couponConsume = new CouponConsume();
        couponConsume.setCouponId(id);
        couponConsume.setConsumerId(coupon.getUserId());
        couponConsume.setFundType(coupon.getFundType());
        couponConsume.setStoreId(storeInfo.getUserId());
        couponConsume.setConsumeTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
        couponConsume.setStoreType(storeType);
        couponConsume.setAmount(CouponConstants.COUPON_AMOUNT);
        result = couponConsumeMapper.insertCouponConsume(couponConsume);
        if (result < 0) {
            throw new CustomException("新增消費記錄檔失敗，請聯絡管理員");
        }
    }

    @Override
    public List<CouponConsume> consumption() {

        // 構建查詢條件
        CouponConsume couponConsume = new CouponConsume();
        couponConsume.setConsumerId(SecurityUtils.getLoginUser().getUser().getUserId());

        List<CouponConsume> list = couponConsumeMapper.selectConsumptionList(SecurityUtils.getLoginUser().getUser().getUserId());

        return list;
    }

    @Override
    public AjaxResult overviewCoupons() {

        AjaxResult ajax = AjaxResult.success();

        // 可用總餘額
        int balance = 0;
        // 已消費總金額
        int consumed = 0;

        Long consumerId = SecurityUtils.getLoginUser().getUser().getUserId();
        List<Coupon> couponList = couponMapper.overviewCoupons(consumerId);
        List<CouponConsume> couponConsumeList = couponConsumeMapper.selectConsumptionList(consumerId);

        if (couponList.isEmpty()) {
            throw new CustomException("您尚未擁有振興券");
        }

        // 初始化返回結果
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("consumed", 0);
            map.put("balance", 0);
            map.put("type", i + "");
            map.put("consumedCoupons", new ArrayList<>());
            result.add(map);
        }


        // 統計抵用券相關訊息
        for (Coupon coupon : couponList) {
            // 抵用券類型
            Integer storeType = Integer.parseInt(coupon.getStoreType());
            // storeType 類型的抵用券紀錄
            Map<String, Object> map = result.get(storeType);
            // 抵用券已使用
            if (CouponConstants.USED.equals(coupon.getIsUsed())) {
                // 已消費總金額
                consumed += coupon.getAmount();
                // storeType 類型的抵用券已消費金額
                map.put("consumed", (int) (Integer.parseInt(map.get("consumed").toString()) + coupon.getAmount()));
                /*List<Coupon> consumedCoupons = (List<Coupon>) map.get("consumedCoupons");
                consumedCoupons.add(coupon);*/
            }
            // 抵用券未使用
            else {
                // 可用總餘額
                balance += coupon.getAmount();
                // storeType 類型的抵用券總餘額
                map.put("balance", (int) (Integer.parseInt(map.get("balance").toString()) + coupon.getAmount()));
            }
        }

        // 處理消費紀錄
        for (CouponConsume couponConsume : couponConsumeList) {
            int i = Integer.parseInt(couponConsume.getStoreType());
            List<CouponConsume> consumedCoupons = (List<CouponConsume>)result.get(i).get("consumedCoupons");
            consumedCoupons.add(couponConsume);
        }

        // 處理返回結果
        ajax.put("balance", balance);
        ajax.put("consumed", consumed);
        ajax.put("couponTypes", result);

        return ajax;
    }

    /**
     * 取得時間範圍內的消費記錄檔
     *
     * @param beginDate 開始時間
     * @param endDate   結束時間
     * @return 結果
     */
    @Override
    public List<Map<String,Object>> getTotalAmtByStoreId(String beginDate, String endDate) {
        return couponConsumeMapper.getTotalAmtByStoreId(beginDate,endDate);
    }
}
