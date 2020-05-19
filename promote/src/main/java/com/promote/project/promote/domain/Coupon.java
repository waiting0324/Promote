package com.promote.project.promote.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 抵用券發放記錄檔物件 pro_coupon
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Coupon extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 抵用券序號 */
    private String id;

    /** 補助機構 (S:中企
     T:中辦
     B:商業司
     C:文化部) */
    private String fundType;

    /** 消費者的user_id */
    @Excel(name = "消費者的user_id")
    private Long userId;

    /** 是否已使用 ( 0未使用 1已使用 ) */
    @Excel(name = "是否已使用 ( 0未使用 1已使用 )")
    private String isUsed;

    /** 核發時間 */
    @Excel(name = "核發時間", width = 30, dateFormat = "yyyy-MM-dd")
    private Date issueDate;

    /** 0未印,1已印,16張都為1才能將consumer.coupon_print_type押成1 */
    @Excel(name = "0未印,1已印,16張都為1才能將consumer.coupon_print_type押成1")
    private String status;

    /** 抵用券類型( 0夜市 1餐廳 2商圈 3藝文) */
    @Excel(name = "抵用券類型( 0夜市 1餐廳 2商圈 3藝文)")
    private String storeType;

    /** 抵用券金額 */
    @Excel(name = "抵用券金額")
    private Long amount;

    /** 提醒到期已推送時間 */
    @Excel(name = "提醒到期已推送時間", width = 30, dateFormat = "yyyy-MM-dd")
    private Date noticeTime;


    private String isReturn;


    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setFundType(String fundType)
    {
        this.fundType = fundType;
    }

    public String getFundType()
    {
        return fundType;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setIsUsed(String isUsed)
    {
        this.isUsed = isUsed;
    }

    public String getIsUsed()
    {
        return isUsed;
    }
    public void setIssueDate(Date issueDate)
    {
        this.issueDate = issueDate;
    }

    public Date getIssueDate()
    {
        return issueDate;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setStoreType(String storeType)
    {
        this.storeType = storeType;
    }

    public String getStoreType()
    {
        return storeType;
    }
    public void setAmount(Long amount)
    {
        this.amount = amount;
    }

    public Long getAmount()
    {
        return amount;
    }
    public void setNoticeTime(Date noticeTime)
    {
        this.noticeTime = noticeTime;
    }

    public Date getNoticeTime()
    {
        return noticeTime;
    }

    public String getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(String isReturn) {
        this.isReturn = isReturn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("fundType", getFundType())
                .append("userId", getUserId())
                .append("isUsed", getIsUsed())
                .append("issueDate", getIssueDate())
                .append("status", getStatus())
                .append("storeType", getStoreType())
                .append("amount", getAmount())
                .append("noticeTime", getNoticeTime())
                .append("createTime", getCreateTime())
                .toString();
    }
}