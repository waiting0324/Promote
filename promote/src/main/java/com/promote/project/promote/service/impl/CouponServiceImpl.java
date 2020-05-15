package com.promote.project.promote.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.promote.common.constant.Constants;
import com.promote.common.constant.ConsumerConstants;
import com.promote.common.constant.CouponConstants;
import com.promote.common.constant.StoreTypeConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.exception.user.CaptchaExpireException;
import com.promote.common.utils.*;
import com.promote.framework.redis.RedisCache;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.domain.*;
import com.promote.project.promote.mapper.*;
import com.promote.project.promote.service.ICommonService;
import com.promote.project.promote.service.ICouponService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.system.service.ISysConfigService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
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

    @Autowired
    private FundAmountMapper fundAmountMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ICommonService commonService;

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
    public Map<String, Object> applyCoupon(SysUser requestConsumer, String code) {

        // 返回給前端的結果封裝
        Map<String, Object> result = new HashMap<>();
        List<String> msg = new ArrayList<>();
        msg.add("✱提醒您，夜市、餐廳、商圈類抵用券使用有效期限為發放後一個月內！藝文類為二個月內！");
        result.put("msg", msg);


        // 檢驗是否有消費者帳號
        if (StringUtils.isNull(requestConsumer.getUsername())) {
            throw new CustomException("未指定消費者帳號");
        }
        // 從DB中獲取的消費者資料
        SysUser dbConsumer = userMapper.selectUserByUsername(requestConsumer.getUsername());
        if (StringUtils.isNull(dbConsumer)) {
            throw new CustomException("找不到此消費者");
        }
        Long consumerId = dbConsumer.getUserId();
        String consumerStat = consumerInfoMapper.selectConsumerInfoById(consumerId).getConsumerStat();
        ConsumerInfo insertConsumerInfo = new ConsumerInfo();

        // 除了代註冊沒手機的之外，都要校驗是否有輸入手機號碼
        if (StringUtils.isEmpty(requestConsumer.getMobile())
                && !consumerStat.equals(ConsumerConstants.STAT_REGISTED_PROXY_NO_MOBILE)) {
            throw new CustomException("未輸入手機號碼");
        }
        if (ConsumerConstants.STAT_SEND.equals(consumerStat)
                || ConsumerConstants.STAT_PRINT.equals(consumerStat)) {
            throw new CustomException("該消費者已領取過振興券");
        }

        // 驗證OTP驗證碼
        // 從Redis中取出驗證碼
        String verifyKey = Constants.VERI_COUPON_SEND_CODE_KEY + requestConsumer.getUsername();
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);

        // 比對驗證碼
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }

        Date nowDate = DateUtils.getNowDate();

        // 查看預算是否足夠
        // 從Redis中查詢預算
        List<FundAmount> fundAmountList = redisCache.getCacheList(Constants.FOUND_AMOUNT_KEY);
        // Redis中沒有則從DB中查詢
        if (fundAmountList.isEmpty()) {
            fundAmountList = fundAmountMapper.selectFundAmountList(null);
            redisCache.setCacheList(Constants.FOUND_AMOUNT_KEY, fundAmountList);
        }

        // 檢查預算是否足夠
        Map<String, Boolean> fundMap = checkFundEnough(fundAmountList);
        // 夜市預算是否足夠
        boolean isNightMarketFundEnough = fundMap.get(StoreTypeConstants.NIGHT_MARKET);
        // 餐廳預算是否足夠
        boolean isRestaurantFundEnough = fundMap.get(StoreTypeConstants.RESTAURANT);
        // 商圈預算是否足夠
        boolean isShoppingAreaFundEnough = fundMap.get(StoreTypeConstants.SHOPPING_AERA);
        // 藝文預算是否足夠
        boolean isArtFundEnough = fundMap.get(StoreTypeConstants.ART);

        // 抵用券ID
        Map<String, LinkedList<String>> couponIds = IdUtils.generateTicketNo(SecurityUtils.getLoginUser().getUser().getUserId() + ""
                , new String[]{"S", "T", "B", "C"});

        // 消費者選擇使用電子(虛擬)方式發放抵用券
        if (CouponConstants.TYPE_ELEC.equals(requestConsumer.getConsumer().getCouponType())) {

            // 設定抵用券資料，發送抵用券
            List<Coupon> couponList = new ArrayList<>();
            for (int i = 0; i < 16; i++) {

                // 設定抵用券的基本資料
                Coupon coupon = new Coupon();

                coupon.setUserId(consumerId);
                coupon.setIsUsed(CouponConstants.UN_USED);
                coupon.setIssueDate(nowDate);
                coupon.setAmount(CouponConstants.COUPON_AMOUNT);

                // 夜市
                if (i <= 3 && isNightMarketFundEnough) {
                    coupon.setStoreType(StoreTypeConstants.NIGHT_MARKET);
                    // 中辦
                    coupon.setId(couponIds.get(CouponConstants.FUND_TYPE_CENTER_OFFICE).pop());
                    coupon.setFundType(CouponConstants.FUND_TYPE_CENTER_OFFICE);
                }
                // 餐廳
                else if (4 <= i && i <= 7 && isRestaurantFundEnough) {
                    coupon.setStoreType(StoreTypeConstants.RESTAURANT);
                    // 商業司
                    coupon.setId(couponIds.get(CouponConstants.FUND_TYPE_BUSINESS_DEPARTMENT).pop());
                    coupon.setFundType(CouponConstants.FUND_TYPE_BUSINESS_DEPARTMENT);
                }
                // 商圈
                else if (8 <= i && i <= 11 && isShoppingAreaFundEnough) {
                    coupon.setStoreType(StoreTypeConstants.SHOPPING_AERA);
                    // 中企
                    coupon.setId(couponIds.get(CouponConstants.FUND_TYPE_SME).pop());
                    coupon.setFundType(CouponConstants.FUND_TYPE_SME);
                }
                // 藝文
                else if (12 <= i && i <= 15 && isArtFundEnough) {
                    coupon.setStoreType(StoreTypeConstants.ART);
                    // 文化部
                    coupon.setId(couponIds.get(CouponConstants.FUND_TYPE_MINISTRY_CULTURE).pop());
                    coupon.setFundType(CouponConstants.FUND_TYPE_MINISTRY_CULTURE);
                }

                couponList.add(coupon);
            }

            // 寫入抵用券表
            couponMapper.insertCouponList(couponList);

        }
        // 消費者選擇使用紙本方式發放抵用券
        else if (CouponConstants.TYPE_PAPAER.equals(requestConsumer.getConsumer().getCouponType())) {
            // KIOSK兌換碼
            insertConsumerInfo.setPrintCode(RandomUtil.randomNumbers(10));
        } else {
            throw new CustomException("消費者選擇發放抵用券的方式不正確");
        }

        // 處理要更新的消費者資料
        insertConsumerInfo.setUserId(consumerId);
        insertConsumerInfo.setConsumerStat(ConsumerConstants.STAT_SEND);
        insertConsumerInfo.setCouponPrintType(requestConsumer.getConsumer().getCouponType());
        insertConsumerInfo.setHotelId(SecurityUtils.getLoginUser().getUser().getUserId());
        insertConsumerInfo.setIssueDate(nowDate);

        // 更新消費者資料
        if (consumerInfoMapper.updateConsumerInfo(insertConsumerInfo) < 0) {
            throw new CustomException(MessageUtils.message("pro.err.update.consumer.fail"));
        }

        // 更新使用者資料
        SysUser updUser = new SysUser();
        updUser.setUserId(consumerId);
        updUser.setMobile(requestConsumer.getMobile());

        // 旅宿業者代註冊消費者，需要產生隨機密碼
        if (consumerStat.equals(ConsumerConstants.STAT_REGISTED_PROXY_MOBILE)
                || consumerStat.equals(ConsumerConstants.STAT_REGISTED_PROXY_NO_MOBILE)) {

            // 密碼 : 證件號碼末四碼+生日月日共八碼
            String genPwd = StrUtil.sub(dbConsumer.getIdentity(), -1, -5)
                    + new SimpleDateFormat("yyyyMMdd").format(consumerInfoMapper.selectConsumerInfoById(consumerId).getBirthday());

            updUser.setPassword(SecurityUtils.encryptPassword(genPwd));
            System.out.println("PASSWORD: " + genPwd);
            result.put("note", "您的代註冊預設帳號為" + dbConsumer.getUsername() + "，密碼為證件號碼末四碼+生日月日共八碼，首次登入請立即變更密碼");
        }

        // 手機號碼與註冊時不同，需加入訊息
        if (!requestConsumer.getMobile().equals(dbConsumer.getMobile())) {
            msg.add("系統已將您的手機號碼更新為" + commonService.hidePersonalInfo(requestConsumer).getMobile());
        }

        // 更新使用者表
        if (userMapper.updateUser(updUser) < 0) {
            throw new CustomException(MessageUtils.message("pro.err.update.user.fail"));
        }

        // TODO 用MQ更新預算表
        for (FundAmount fundAmount : fundAmountList) {

            // 夜市
            if (isNightMarketFundEnough && StoreTypeConstants.NIGHT_MARKET.equals(fundAmount.getStoreType())) {
                fundAmount.setBalance(fundAmount.getBalance() - fundAmount.getPerFund());
                if (fundAmountMapper.updateFundAmount(fundAmount) == 0) {
                    throw new CustomException("更新夜市預算表失敗");
                }
            }
            // 餐廳
            else if (isRestaurantFundEnough && StoreTypeConstants.RESTAURANT.equals(fundAmount.getStoreType())) {
                fundAmount.setBalance(fundAmount.getBalance() - fundAmount.getPerFund());
                if (fundAmountMapper.updateFundAmount(fundAmount) == 0) {
                    throw new CustomException("更新餐廳預算表失敗");
                }
            }
            // 商圈
            else if (isShoppingAreaFundEnough && StoreTypeConstants.SHOPPING_AERA.equals(fundAmount.getStoreType())) {
                fundAmount.setBalance(fundAmount.getBalance() - fundAmount.getPerFund());
                if (fundAmountMapper.updateFundAmount(fundAmount) == 0) {
                    throw new CustomException("更新商圈預算表失敗");
                }
            }
            // 藝文
            else if (isArtFundEnough && StoreTypeConstants.ART.equals(fundAmount.getStoreType())) {
                fundAmount.setBalance(fundAmount.getBalance() - fundAmount.getPerFund());
                if (fundAmountMapper.updateFundAmount(fundAmount) == 0) {
                    throw new CustomException("更新藝文預算表失敗");
                }
            }

        }
        // 更新預算表的Redis
        redisCache.setCacheList(Constants.FOUND_AMOUNT_KEY, fundAmountList);


        // TODO 抵用券發送成功簡訊
        // 自行註冊的消費者
        /*if (consumerStat.equals(ConsumerConstants.STAT_REGISTED)) {
            // 簡訊模板
            String template = configService.selectConfigByKey("hostel.sendCoupon.template");
            if (StringUtils.isEmpty(template)) {
                throw new CustomException("尚未指定簡訊模板，請聯絡管理員，模板KEY: hostel.sendCoupon.template");
            }

            // 要發送的訊息
            String msg = template;
            System.out.println("自行註冊的消費者簡訊: " + msg);
        }
        // 旅宿業者代註冊的消費者
        else if (consumerStat.equals(ConsumerConstants.STAT_REGISTED_PROXY_MOBILE)
                || consumerStat.equals(ConsumerConstants.STAT_REGISTED_PROXY_NO_MOBILE)) {

            // 簡訊模板
            String template = configService.selectConfigByKey("hostel.sendCoupon.proxy.template");
            if (StringUtils.isEmpty(template)) {
                throw new CustomException("尚未指定簡訊模板，請聯絡管理員，模板KEY: hostel.sendCoupon.proxy.template");
            }

            // 要發送的訊息
            String msg = StrUtil.format(template, requestConsumer.getUsername(), genPwd);
            System.out.println("代註冊的消費者簡訊: " + msg);
        }*/

        return result;
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
        /*if (StringUtils.isNull(consumerCouponList) || consumerCouponList.size() == 0) {
            throw new CustomException("無可使用的抵用券");
        }*/



        List<Map> couponTypeInfoList = new ArrayList();


        for (int i = 0; i < 4; i++) {
            Map<String, Object> couponTypeInfo = new HashMap<>();

            if (i == 0) {
                couponTypeInfo.put("type", "0");
                couponTypeInfo.put("typeName", "夜市");
            } else if (i == 1) {
                couponTypeInfo.put("type", "1");
                couponTypeInfo.put("typeName", "餐廳");
            } else if (i == 2) {
                couponTypeInfo.put("type", "2");
                couponTypeInfo.put("typeName", "商圈");
            } else if (i == 3) {
                couponTypeInfo.put("type", "3");
                couponTypeInfo.put("typeName", "藝文");
            }

            couponTypeInfo.put("couponInfo", new ArrayList<>());
            couponTypeInfoList.add(couponTypeInfo);
        }

        for (Coupon coupon : consumerCouponList) {
            Integer storeType = Integer.parseInt(coupon.getStoreType());
            List<Map> couponInfoList = (List) couponTypeInfoList.get(storeType).get("couponInfo");

            Map<String, Object> couponInfo = new HashMap<>();
            couponInfo.put("couponId", coupon.getId());
            couponInfo.put("amount", coupon.getAmount());
            couponInfoList.add(couponInfo);
        }


        /*couponTypeInfo.put("0", new ArrayList());
        couponTypeInfo.put("1", new ArrayList());
        couponTypeInfo.put("2", new ArrayList());
        couponTypeInfo.put("3", new ArrayList());*/
        /*for (Coupon coupon : consumerCouponList) {
            String storeType = coupon.getStoreType();
            couponTypeInfo.get(storeType).add(coupon);
        }*/

        Map<String, Object> result = new HashMap<>();
        result.put("couponTypeInfo", couponTypeInfoList);

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
    public Integer postiveScan(List<String> couponIds, Long storeId) {

        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();

        Integer amount = 0;

        for (String couponId : couponIds) {
            Coupon coupon = couponMapper.selectCouponById(couponId);
            if (StringUtils.isNull(coupon)) {
                throw new CustomException(MessageUtils.message("pro.err.coupon.not.exist"));
            }
            if (!coupon.getUserId().equals(userId)) {
                throw new CustomException("該抵用券不屬於當前消費者");
            }
            if ("1".equals(coupon.getIsUsed())) {
                throw new CustomException(MessageUtils.message("pro.err.coupon.used"), 1001);
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

            amount += coupon.getAmount().intValue();
        }

        return amount;
    }

    /**
     * 反掃(商家掃消費者)
     *
     * @param id      組抵用券序號
     */
    @Override
    @Transactional
    public Long reverseScan(String id) {

        Coupon coupon = couponMapper.selectCouponById(id);

        if (StringUtils.isNull(coupon)) {
            throw new CustomException(MessageUtils.message("pro.err.coupon.not.exist"));
        }
        if ("1".equals(coupon.getIsUsed())) {
            throw new CustomException(MessageUtils.message("pro.err.coupon.used"), 1001);
        }
        StoreInfo storeInfo =  SecurityUtils.getLoginUser().getUser().getStore();
        if (StringUtils.isNull(storeInfo)) {
            throw new CustomException(MessageUtils.message("pro.err.store.not.find"));
        }
        //店家基本資料-商家類型
        String storeTypes = storeInfo.getType();
        //抵用券發放記錄檔-類別
        String storeType = coupon.getStoreType();
        if (!storeTypes.contains(storeType)) {
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
        couponConsume.setAmount(coupon.getAmount());
        result = couponConsumeMapper.insertCouponConsume(couponConsume);
        if (result < 0) {
            throw new CustomException("新增消費記錄檔失敗，請聯絡管理員");
        }

        return coupon.getAmount();
    }

    @Override
    public List<CouponConsume> consumption() {

        // 構建查詢條件
        CouponConsume couponConsume = new CouponConsume();
        couponConsume.setConsumerId(SecurityUtils.getLoginUser().getUser().getUserId());

        ConsumerInfo consumerInfo = SecurityUtils.getLoginUser().getUser().getConsumer();


        List<CouponConsume> list = couponConsumeMapper.selectConsumptionList(SecurityUtils.getLoginUser().getUser().getUserId());

        return list;
    }

    /**
     * 抵用券發放紀錄查詢
     *
     * @return 結果
     */
    @Override
    public Map<String, Object> overviewCoupons(Long consumerId) {
        AjaxResult ajax = AjaxResult.success();
        // 可用總餘額
        int balance = 0;
        // 已消費總金額
        int consumed = 0;
        Map<String, Object> couponOverview = consumerInfoMapper.getConsumerInfoById(consumerId);
        if(StringUtils.isEmpty(couponOverview)){
            throw new CustomException("尚未申請抵用券");
        }
        Date applyTime = (Date)couponOverview.get("applyTime");
        if(StringUtils.isNotNull(applyTime)){
            couponOverview.put("applyTime",DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss",applyTime));
        }
        Date printTime = (Date)couponOverview.get("printTime");
        if(StringUtils.isNotNull(printTime)){
            couponOverview.put("printTime",DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss",printTime));
        }

        //取得該使用者的coupon(pro_coupon)
        List<Coupon> couponList = couponMapper.overviewCoupons(consumerId);
//        if (couponList.isEmpty()) {
//            throw new CustomException("您尚未擁有振興券");
//        }
        // 初始化返回結果
        List<Map<String, Object>> couponTypeInfo = new ArrayList<>(); //couponTypeInfo
        String[] typeName = {"夜市類", "餐廳類", "商圈類", "藝文類"};
        for (int i = 0; i < 4; i++) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("fundType", i + ""); //0夜市 1餐廳 2商圈 3藝文
            map.put("typeName", typeName[i]);
            map.put("consumed", 0);
            map.put("balance", 0);
            List<Map<String, Object>> couponInfo = couponMapper.getCouponInfo(consumerId,i + "");
            for(Map<String, Object> detail :couponInfo){
                Date consumeTime = (Date)detail.get("consumeTime");
                if(StringUtils.isNotNull(consumeTime)){
                    detail.put("consumeTime",DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss",consumeTime));
                }
            }
            map.put("couponInfo",couponInfo);
            couponTypeInfo.add(map);
        }


        // 統計抵用券相關訊息
        for (Coupon coupon : couponList) {
            // 抵用券類型
            Integer storeType = Integer.parseInt(coupon.getStoreType());
            // storeType 類型的抵用券紀錄
            Map<String, Object> map = couponTypeInfo.get(storeType);
            // 抵用券已使用
            if (CouponConstants.USED.equals(coupon.getIsUsed())) {
                // 已消費總金額
                consumed += coupon.getAmount();
                // storeType 類型的抵用券已消費金額
                map.put("consumed", (int) (Integer.parseInt(map.get("consumed").toString()) + coupon.getAmount()));
            } else {
                // 抵用券未使用
                // 可用總餘額
                balance += coupon.getAmount();
                // storeType 類型的抵用券總餘額
                map.put("balance", (int) (Integer.parseInt(map.get("balance").toString()) + coupon.getAmount()));
            }
        }
        couponOverview.put("consumed",consumed);
        couponOverview.put("balance",balance);
        couponOverview.put("couponTypeInfo",couponTypeInfo);
        return couponOverview;
    }

    /**
     * 取得時間範圍內的消費記錄檔
     *
     * @param beginDate 開始時間
     * @param endDate   結束時間
     * @return 結果
     */
    @Override
    public List<Map<String, Object>> getTotalAmtByStoreId(String beginDate, String endDate) {
        return couponConsumeMapper.getTotalAmtByStoreId(beginDate, endDate);
    }

    /**
     * 檢查預算是否足夠
     */
    private Map checkFundEnough(List<FundAmount> fundAmountList) {

        Map<String, Boolean> result = new HashMap<>();

        // 檢測預算是否足夠
        for (FundAmount fundAmount : fundAmountList) {
            // 夜市
            if (fundAmount.getStoreType().equals(StoreTypeConstants.NIGHT_MARKET)) {
                Boolean isNightMarketFundEnough = fundAmount.getBalance() - fundAmount.getPerFund() > 0;
                result.put(StoreTypeConstants.NIGHT_MARKET, isNightMarketFundEnough);
            }
            // 餐廳
            else if (fundAmount.getStoreType().equals(StoreTypeConstants.RESTAURANT)) {
                Boolean isRestaurantFundEnough = fundAmount.getBalance() - fundAmount.getPerFund() > 0;
                result.put(StoreTypeConstants.RESTAURANT, isRestaurantFundEnough);
            }
            // 商圈
            else if (fundAmount.getStoreType().equals(StoreTypeConstants.SHOPPING_AERA)) {
                Boolean isShoppingAreaFundEnough = fundAmount.getBalance() - fundAmount.getPerFund() > 0;
                result.put(StoreTypeConstants.SHOPPING_AERA, isShoppingAreaFundEnough);
            }
            // 藝文
            else if (fundAmount.getStoreType().equals(StoreTypeConstants.ART)) {
                Boolean isArtFundEnough = fundAmount.getBalance() - fundAmount.getPerFund() > 0;
                result.put(StoreTypeConstants.ART, isArtFundEnough);
            }
        }

        return result;
    }

    /**
     * 重置列印狀態
     */
    public int updateConsumerStart(ConsumerInfo consumerInfo) {

        int sum = consumerInfoMapper.updateConsumerInfo(consumerInfo);

        return sum;
    }

    /**
     * 抵用券消費記錄查詢(WEB介面用)
     *
     * @param id        使用者id
     * @param storeType 抵用券類型
     * @param startDate 查詢起日
     * @param endDate   查詢迄日
     * @param rows      每頁筆數
     * @param page      要查詢的頁數
     * @return 結果
     */
    @Override
    public Map<String, Object> transactionHistory(Long id, String role, String storeType, String startDate, String endDate, String rows, String page) {
        List<Map<String, Object>> historyList = null;
        Map<String, Object> map = null;
        startDate = startDate.replaceAll("/", "-");
        startDate += " 00:00:00";
        endDate = endDate.replaceAll("/", "-");
        endDate += " 23:59:59";
        if ("S".equals(role)) {
            //查店家
            historyList = couponConsumeMapper.transactionHistory(startDate, endDate, "-1".equals(storeType) ? null : storeType, null, id);
        } else {
            //查消費者
            historyList = couponConsumeMapper.transactionHistory(startDate, endDate, "-1".equals(storeType) ? null : storeType, id, null);
        }
        if (StringUtils.isNotNull(historyList) && historyList.size() > 0) {
            map = new LinkedHashMap<String, Object>();
            List<Map<String, Object>> couponInfoList = new ArrayList<Map<String, Object>>();
            //總筆數
            int totalCount = historyList.size();
            //每頁筆數
            int rowsPerPage = Integer.parseInt(rows);
            //總頁數
            int pageSize = totalCount % rowsPerPage == 0 ? totalCount / rowsPerPage : (totalCount / rowsPerPage) + 1;
            //目標頁數
            int targetPage = Integer.parseInt(page);
            int startCount = (targetPage - 1) * rowsPerPage;
            int endCount = (targetPage * rowsPerPage) > totalCount ? totalCount : targetPage * rowsPerPage;
            for (int i = startCount; i < endCount; i++) {
                Map<String, Object> dataMap = historyList.get(i);
                Date date = (Date) dataMap.get("consumeTime");
                dataMap.put("consumeTime", DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss", date));
                couponInfoList.add(historyList.get(i));
            }
            map.put("couponInfo", couponInfoList);
            map.put("totalPage", pageSize);
            map.put("page", targetPage);
            map.put("totalRecords", totalCount);
        }
        return map;
    }

    /**
     * 以證號末四碼及兌換碼查詢抵用券
     *
     * @param indentity 身分證號或居留證號末四碼
     * @param printCode 紙本兌換碼
     * @return 結果
     */
    @Override
    public List<Map<String, Object>> getPrintCoupon(@Param("indentity") String indentity, @Param("printCode") String printCode) {
        List<Map<String, Object>> resultList = couponMapper.getPrintCoupon(indentity, printCode);
        return resultList;
    }

    /**
     * 以證號末四碼及兌換碼查詢抵用券
     *
     * @param printCode 紙本兌換碼
     * @return 結果
     */
    @Override
    public int updatePrintCoupon(@Param("printCode") String printCode) {
        int sum = couponMapper.updatePrintCoupon(printCode);
        return sum;
    }

}
