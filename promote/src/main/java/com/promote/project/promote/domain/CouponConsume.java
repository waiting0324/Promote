package com.promote.project.promote.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 消費記錄檔物件 pro_coupon_consume
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public class CouponConsume extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 抵用券序號 */
    private String couponId;

    /** 消費者 ID */
    private Long consumerId;

    /** 補助機構 (S:中企
     T:中辦
     B:商業司
     C:文化部) */
    private String fundType;

    /** 商家的user_id */
    @Excel(name = "商家的user_id")
    private Long storeId;

    private String storeName;

    /** 商家類型(0夜市 1餐廳 2商圈 3藝文) */
    @Excel(name = "商家類型(0夜市 1餐廳 2商圈 3藝文)")
    private String storeType;

    /** 消費時間 */
    @Excel(name = "消費時間", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date consumeTime;

    /** 抵用券金額 */
    @Excel(name = "抵用券金額")
    private Long amount;

    public void setCouponId(String couponId)
    {
        this.couponId = couponId;
    }

    public String getCouponId()
    {
        return couponId;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setFundType(String fundType)
    {
        this.fundType = fundType;
    }

    public String getFundType()
    {
        return fundType;
    }
    public void setStoreId(Long storeId)
    {
        this.storeId = storeId;
    }

    public Long getStoreId()
    {
        return storeId;
    }
    public void setStoreType(String storeType)
    {
        this.storeType = storeType;
    }

    public String getStoreType()
    {
        return storeType;
    }
    public void setConsumeTime(Date consumeTime)
    {
        this.consumeTime = consumeTime;
    }

    public Date getConsumeTime()
    {
        return consumeTime;
    }
    public void setAmount(Long amount)
    {
        this.amount = amount;
    }

    public Long getAmount()
    {
        return amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("couponId", getCouponId())
                .append("fundType", getFundType())
                .append("storeId", getStoreId())
                .append("storeType", getStoreType())
                .append("consumeTime", getConsumeTime())
                .append("amount", getAmount())
                .toString();
    }
}