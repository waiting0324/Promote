package com.promote.project.promote.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 週結明細物件 pro_weekly_settlement_detail
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeeklySettlementDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 抵用券序號 */
    private String couponId;

    /** 消費者的user_id */
    @Excel(name = "消費者的user_id")
    private Long userId;

    /** 商家的user_id */
    @Excel(name = "商家的user_id")
    private Long storeId;

    /** 消費時間 */
    @Excel(name = "消費時間", width = 30, dateFormat = "yyyy-MM-dd")
    private Date consumeTime;

    /** 類別 (類別0夜市 1餐廳 2商圈 3藝文) */
    @Excel(name = "類別 (類別0夜市 1餐廳 2商圈 3藝文)")
    private String storeType;

    /** 主檔流水號 */
    @Excel(name = "主檔流水號")
    private Long mainId;

    public void setCouponId(String couponId)
    {
        this.couponId = couponId;
    }

    public String getCouponId()
    {
        return couponId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setStoreId(Long storeId)
    {
        this.storeId = storeId;
    }

    public Long getStoreId()
    {
        return storeId;
    }
    public void setConsumeTime(Date consumeTime)
    {
        this.consumeTime = consumeTime;
    }

    public Date getConsumeTime()
    {
        return consumeTime;
    }
    public void setStoreType(String storeType)
    {
        this.storeType = storeType;
    }

    public String getStoreType()
    {
        return storeType;
    }
    public void setMainId(Long mainId)
    {
        this.mainId = mainId;
    }

    public Long getMainId()
    {
        return mainId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("couponId", getCouponId())
                .append("userId", getUserId())
                .append("storeId", getStoreId())
                .append("consumeTime", getConsumeTime())
                .append("storeType", getStoreType())
                .append("createTime", getCreateTime())
                .append("mainId", getMainId())
                .toString();
    }
}