package com.promote.project.promote.service.impl;

import com.promote.common.constant.StoreTypeConstants;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.framework.redis.RedisCache;
import com.promote.project.promote.domain.Coupon;
import com.promote.project.promote.mapper.CouponMapper;
import com.promote.project.promote.service.ICouponService;
import com.promote.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 抵用券Service業務層處理
 * 
 * @author 6550 劉威廷
 * @date 2020-04-22
 */
@Service
public class CouponServiceImpl implements ICouponService 
{
    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查詢抵用券
     * 
     * @param sn 抵用券ID
     * @return 抵用券
     */
    @Override
    public Coupon selectCouponById(String sn)
    {
        return couponMapper.selectCouponById(sn);
    }

    /**
     * 查詢抵用券列表
     * 
     * @param coupon 抵用券
     * @return 抵用券
     */
    @Override
    public List<Coupon> selectCouponList(Coupon coupon)
    {
        return couponMapper.selectCouponList(coupon);
    }

    /**
     * 新增抵用券
     * 
     * @param coupon 抵用券
     * @return 結果
     */
    @Override
    public int insertCoupon(Coupon coupon)
    {
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
    public int updateCoupon(Coupon coupon)
    {
        return couponMapper.updateCoupon(coupon);
    }

    /**
     * 批量刪除抵用券
     * 
     * @param sns 需要刪除的抵用券ID
     * @return 結果
     */
    @Override
    public int deleteCouponByIds(String[] sns)
    {
        return couponMapper.deleteCouponByIds(sns);
    }

    /**
     * 刪除抵用券資訊
     * 
     * @param sn 抵用券ID
     * @return 結果
     */
    @Override
    public int deleteCouponById(String sn)
    {
        return couponMapper.deleteCouponById(sn);
    }

    @Override
    public void sendCoupon(SysUser user, String code) {

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
            // 商圈
            else if (12 <= i && i <= 15) {
                coupon.setStoreType(StoreTypeConstants.ART);
            }

            couponList.add(coupon);
        }

        // 寫入抵用券表
        couponMapper.insertCouponList(couponList);

    }
}
