package com.promote.project.promote.domain;

import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 每日消費統計檔物件 pro_daily_consume
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public class DailyConsume extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序列號 */
    private Long id;

    /** 使用日期 */
    @Excel(name = "使用日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date consumeTime;

    /** 補助機構(S:中企
     T:中辦
     B:商業司
     C:文化部) */
    private String fundType;

    /** 商家的user_id */
    @Excel(name = "商家的user_id")
    private Long storeId;

    /** 消費金額 */
    @Excel(name = "消費金額")
    private Long consumeAmount;

    /** 類別(0夜市 1餐廳 2商圈 3藝文) */
    @Excel(name = "類別(0夜市 1餐廳 2商圈 3藝文)")
    private String storeType;

    /** 抵用券金額 */
    @Excel(name = "抵用券金額")
    private Long couponAmount;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setConsumeTime(Date consumeTime)
    {
        this.consumeTime = consumeTime;
    }

    public Date getConsumeTime()
    {
        return consumeTime;
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
    public void setConsumeAmount(Long consumeAmount)
    {
        this.consumeAmount = consumeAmount;
    }

    public Long getConsumeAmount()
    {
        return consumeAmount;
    }
    public void setStoreType(String storeType)
    {
        this.storeType = storeType;
    }

    public String getStoreType()
    {
        return storeType;
    }
    public void setCouponAmount(Long couponAmount)
    {
        this.couponAmount = couponAmount;
    }

    public Long getCouponAmount()
    {
        return couponAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("consumeTime", getConsumeTime())
                .append("fundType", getFundType())
                .append("storeId", getStoreId())
                .append("consumeAmount", getConsumeAmount())
                .append("storeType", getStoreType())
                .append("couponAmount", getCouponAmount())
                .toString();
    }
}