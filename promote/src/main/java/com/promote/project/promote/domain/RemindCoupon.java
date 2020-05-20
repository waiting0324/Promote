package com.promote.project.promote.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 需發推播的抵用券發放記錄檔物件
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemindCoupon extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消費者的user_id */
    private Long userId;

    /** 夜市類抵用券 */
    private List<Coupon> typeZeroCoupon;

    /** 餐廳類抵用券 */
    private List<Coupon> typeOneCoupon;

    /** 商圈類抵用券 */
    private List<Coupon> typeTwoCoupon;

    /** 藝文類抵用券 */
    private List<Coupon> typeThreeCoupon;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Coupon> getTypeZeroCoupon() {
        return typeZeroCoupon;
    }

    public void setTypeZeroCoupon(List<Coupon> typeZeroCoupon) {
        this.typeZeroCoupon = typeZeroCoupon;
    }

    public List<Coupon> getTypeOneCoupon() {
        return typeOneCoupon;
    }

    public void setTypeOneCoupon(List<Coupon> typeOneCoupon) {
        this.typeOneCoupon = typeOneCoupon;
    }

    public List<Coupon> getTypeTwoCoupon() {
        return typeTwoCoupon;
    }

    public void setTypeTwoCoupon(List<Coupon> typeTwoCoupon) {
        this.typeTwoCoupon = typeTwoCoupon;
    }

    public List<Coupon> getTypeThreeCoupon() {
        return typeThreeCoupon;
    }

    public void setTypeThreeCoupon(List<Coupon> typeThreeCoupon) {
        this.typeThreeCoupon = typeThreeCoupon;
    }
}