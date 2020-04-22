package com.promote.project.promote.domain;

import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 抵用券物件 pro_coupon
 * 
 * @author 6550 劉威廷
 * @date 2020-04-22
 */
public class Coupon extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 抵用券序號 */
    private String sn;

    /** 抵用券類型( 0夜市 1餐廳 2商圈 3藝文) */
    @Excel(name = "抵用券類型( 0夜市 1餐廳 2商圈 3藝文)")
    private String storeType;

    /** 使用者ID */
    @Excel(name = "使用者ID")
    private Long userId;

    /** 發放此抵用券的旅宿業者ID */
    @Excel(name = "發放此抵用券的旅宿業者ID")
    private Long hostelId;

    /** 是否已使用 ( 0未使用 1已使用 ) */
    @Excel(name = "是否已使用 ( 0未使用 1已使用 )")
    private String isUsed;

    /** 消費的商家ID */
    @Excel(name = "消費的商家ID")
    private Long storeId;

    /** 消費時間 */
    @Excel(name = "消費時間", width = 30, dateFormat = "yyyy-MM-dd")
    private Date consumeTime;

    /** 提醒到期已推送時間 */
    @Excel(name = "提醒到期已推送時間", width = 30, dateFormat = "yyyy-MM-dd")
    private Date noticeTime;

    public void setSn(String sn) 
    {
        this.sn = sn;
    }

    public String getSn() 
    {
        return sn;
    }
    public void setStoreType(String storeType) 
    {
        this.storeType = storeType;
    }

    public String getStoreType() 
    {
        return storeType;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setHostelId(Long hostelId) 
    {
        this.hostelId = hostelId;
    }

    public Long getHostelId() 
    {
        return hostelId;
    }
    public void setIsUsed(String isUsed) 
    {
        this.isUsed = isUsed;
    }

    public String getIsUsed() 
    {
        return isUsed;
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
    public void setNoticeTime(Date noticeTime) 
    {
        this.noticeTime = noticeTime;
    }

    public Date getNoticeTime() 
    {
        return noticeTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("sn", getSn())
            .append("storeType", getStoreType())
            .append("userId", getUserId())
            .append("hostelId", getHostelId())
            .append("isUsed", getIsUsed())
            .append("storeId", getStoreId())
            .append("consumeTime", getConsumeTime())
            .append("noticeTime", getNoticeTime())
            .append("createTime", getCreateTime())
            .toString();
    }
}
