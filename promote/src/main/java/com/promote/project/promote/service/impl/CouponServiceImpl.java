package com.promote.project.promote.service.impl;

import com.promote.common.constant.CouponConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.MessageUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.redis.RedisCache;
import com.promote.project.promote.domain.Coupon;
import com.promote.project.promote.domain.CouponConsume;
import com.promote.project.promote.domain.StoreInfo;
import com.promote.project.promote.mapper.CouponConsumeMapper;
import com.promote.project.promote.mapper.CouponMapper;
import com.promote.project.promote.mapper.StoreInfoMapper;
import com.promote.project.promote.service.ICouponService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public int sendCoupon(SysUser user, String code) {

        // 檢驗是否有消費者ID
        if (StringUtils.isNull(user.getUserId())) {
            throw new CustomException("未指定消費者ID");
        }

        // 查詢該消費者是否已經發過抵用券
        /*if (CouponConstants.IS_PROVIDE.equals(userMapper.selectUserById(user.getUserId()).getCouponProvideType())) {
            throw new CustomException("該消費者已經領過抵用券");
        }*/

        // 驗證OTP驗證碼
        // 從Redis中取出驗證碼
        /*String verifyKey = Constants.OTP_CODE_KEY + user.getUserId();
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        // 比對驗證碼
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }*/

        // 消費者選擇使用電子(虛擬)方式發放抵用券
        /*if (CouponConstants.COUPON_TYPE_VIRTUAL.equals(user.getCouponType())) {

            // 設定抵用券資料，發送抵用券
            List<Coupon> couponList = new ArrayList<>();
            for (int i = 0; i < 16; i++) {

                // 設定抵用券的基本資料
                Coupon coupon = new Coupon();
                coupon.setSn(LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd")) + UUID.randomUUID());
                coupon.setUserId(user.getUserId());
                coupon.setHostelId(SecurityUtils.getLoginUser().getUser().getUserId());
                coupon.setIsUsed("0");

                // 夜市
                if (i <= 3) {
                    coupon.setStoreType(StoreTypeConstants.NIGHT_MARKET);
                }
                // 餐廳
                else if (4 <= i && i <= 7) {
                    coupon.setStoreType(StoreTypeConstants.RESTAURANT);
                }
                // 商圈
                else if (8 <= i && i <= 11) {
                    coupon.setStoreType(StoreTypeConstants.SHOPPING_AERA);
                }
                // 藝文
                else if (12 <= i && i <= 15) {
                    coupon.setStoreType(StoreTypeConstants.ART);
                }

                couponList.add(coupon);
            }

            // 寫入抵用券表
            couponMapper.insertCouponList(couponList);

        }
        // 消費者選擇使用紙本方式發放抵用券
        else if (CouponConstants.COUPON_TYPE_PAPER.equals(user.getCouponType())) {
            // TODO 連接KIOSK
        } else {
            throw new CustomException("消費者選擇發放抵用券的方式不正確");
        }*/


        // 處理要更新的消費者資料
        SysUser updateUser = new SysUser();
        updateUser.setUserId(user.getUserId());
        // 電子或紙本
        /*updateUser.setCouponType(user.getCouponType());
        updateUser.setPhonenumber(user.getPhonenumber());
        // 是否已發送
        updateUser.setCouponProvideType(CouponConstants.IS_PROVIDE);
        updateUser.setCouponProvideTime(DateUtils.getNowDate());*/

        // 更新消費者資料
        return userMapper.updateUser(updateUser);

    }

    /**
     * 正掃(消費者掃商家)
     *
     * @param couponIds 抵用券序號
     * @param storeId   商家的user_id
     * @param sysUser      使用者資料
     */
    @Override
    @Transactional
    public void postiveScan(String[] couponIds, Long storeId, SysUser sysUser) {
        Long userId = sysUser.getUserId();
        for (String couponId : couponIds) {
            Coupon coupon = couponMapper.selectCouponById(couponId);
            if (StringUtils.isNull(coupon)) {
                throw new CustomException(MessageUtils.message("pro.err.coupon.not.exist"));
            }
            if (!coupon.getUserId().equals(userId.toString())) {
                throw new CustomException("該抵用券不屬於當前消費者");
            }
            if ("1".equals(coupon.getIsUsed())) {
                throw new CustomException(MessageUtils.message("pro.err.coupon.used"));
            }
            //店家基本資料
            StoreInfo storeInfo = storeInfoMapper.selectStoreInfoById(storeId);
            if(StringUtils.isNull(storeInfo)){
                throw new CustomException(MessageUtils.message("pro.err.store.not.find"));
            }
            //店家基本資料-商家類型
            String storeTypes = storeInfo.getType();
            //抵用券發放記錄檔-類別
            String storeType = coupon.getStoreType();
            if(storeTypes.indexOf(storeType) == -1){
                throw new CustomException(MessageUtils.message("pro.err.coupon.not.match"));
            }
            //抵用券發放記錄檔設為已使用
            coupon.setIsUsed("1");
            //更新抵用券發放記錄檔
            int result = updateCoupon(coupon);
            if(result < 0){
                throw new CustomException("更新抵用券發放記錄檔失敗，請聯絡管理員");
            }
            //建立消費記錄檔
            CouponConsume couponConsume = new CouponConsume();
            couponConsume.setCouponId(couponId);
            couponConsume.setFundType(coupon.getFundType());
            couponConsume.setStoreId(storeId);
            couponConsume.setConsumeTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
            couponConsume.setStoreType(storeType);
            couponConsume.setAmount(CouponConstants.COUPON_AMOUNT);
            result = couponConsumeMapper.insertCouponConsume(couponConsume);
            if(result < 0){
                throw new CustomException("新增消費記錄檔失敗，請聯絡管理員");
            }
        }
    }

    /**
     * 反掃(商家掃消費者)
     *
     * @param id 組抵用券序號
     * @param sysUser 使用者資料(店家)
     */
    @Override
    public void reverseScan(String id, SysUser sysUser) {
        Coupon coupon = couponMapper.selectCouponById(id);
        if(StringUtils.isNull(coupon)){
            throw new CustomException(MessageUtils.message("pro.err.coupon.not.exist"));
        }
        if ("1".equals(coupon.getIsUsed())) {
            throw new CustomException(MessageUtils.message("pro.err.coupon.used"));
        }
        StoreInfo storeInfo = sysUser.getStoreInfo();
        if(StringUtils.isNull(storeInfo)){
            throw new CustomException(MessageUtils.message("pro.err.store.not.find"));
        }
        //店家基本資料-商家類型
        String storeTypes = storeInfo.getType();
        //抵用券發放記錄檔-類別
        String storeType = coupon.getStoreType();
        if(storeTypes.indexOf(storeType) == -1){
            throw new CustomException(MessageUtils.message("pro.err.coupon.not.match"));
        }
        //抵用券發放記錄檔設為已使用
        coupon.setIsUsed("1");
        //更新抵用券發放記錄檔
        int result = updateCoupon(coupon);
        if(result < 0){
            throw new CustomException("更新抵用券發放記錄檔失敗，請聯絡管理員");
        }
        //建立消費記錄檔
        CouponConsume couponConsume = new CouponConsume();
        couponConsume.setCouponId(id);
        couponConsume.setFundType(coupon.getFundType());
        couponConsume.setStoreId(storeInfo.getUserId());
        couponConsume.setConsumeTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
        couponConsume.setStoreType(storeType);
        couponConsume.setAmount(CouponConstants.COUPON_AMOUNT);
        result = couponConsumeMapper.insertCouponConsume(couponConsume);
        if(result < 0){
            throw new CustomException("新增消費記錄檔失敗，請聯絡管理員");
        }

    }
}
