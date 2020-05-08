package com.promote.project.promote.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 週結檔物件 pro_weekly_settlement
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeeklySettlement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 商家的user_id */
    @Excel(name = "商家的user_id")
    private Long storeId;

    /** 週結起日 */
    @Excel(name = "週結起日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date weekStart;

    /** 週結迄日 */
    @Excel(name = "週結迄日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date weekEnd;

    /** 週結總金額 */
    @Excel(name = "週結總金額")
    private Long amount;

    /** 是否已確認週結 ( 0否 1是 ) */
    @Excel(name = "是否已確認週結 ( 0否 1是 )")
    private String isConfirm;

    /** 是否已送撥款 ( 0否 1是 ) */
    @Excel(name = "是否已送撥款 ( 0否 1是 )")
    private String isBatch;

    /** batch狀態 (0未執行 1已送付款) */
    @Excel(name = "batch狀態 (0未執行 1已送付款)")
    private String batchStatus;

    /** batch執行時間 */
    @Excel(name = "batch執行時間", width = 30, dateFormat = "yyyy-MM-dd")
    private Date batchTime;

    /** (0未銷帳 1已銷帳) */
    @Excel(name = "(0未銷帳 1已銷帳)")
    private String paymentStatus;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setStoreId(Long storeId)
    {
        this.storeId = storeId;
    }

    public Long getStoreId()
    {
        return storeId;
    }
    public void setWeekStart(Date weekStart)
    {
        this.weekStart = weekStart;
    }

    public Date getWeekStart()
    {
        return weekStart;
    }
    public void setWeekEnd(Date weekEnd)
    {
        this.weekEnd = weekEnd;
    }

    public Date getWeekEnd()
    {
        return weekEnd;
    }
    public void setAmount(Long amount)
    {
        this.amount = amount;
    }

    public Long getAmount()
    {
        return amount;
    }
    public void setIsConfirm(String isConfirm)
    {
        this.isConfirm = isConfirm;
    }

    public String getIsConfirm()
    {
        return isConfirm;
    }
    public void setIsBatch(String isBatch)
    {
        this.isBatch = isBatch;
    }

    public String getIsBatch()
    {
        return isBatch;
    }
    public void setBatchStatus(String batchStatus)
    {
        this.batchStatus = batchStatus;
    }

    public String getBatchStatus()
    {
        return batchStatus;
    }
    public void setBatchTime(Date batchTime)
    {
        this.batchTime = batchTime;
    }

    public Date getBatchTime()
    {
        return batchTime;
    }
    public void setPaymentStatus(String paymentStatus)
    {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus()
    {
        return paymentStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeId", getStoreId())
                .append("weekStart", getWeekStart())
                .append("weekEnd", getWeekEnd())
                .append("amount", getAmount())
                .append("isConfirm", getIsConfirm())
                .append("isBatch", getIsBatch())
                .append("batchStatus", getBatchStatus())
                .append("batchTime", getBatchTime())
                .append("paymentStatus", getPaymentStatus())
                .toString();
    }
}