package com.promote.project.promote.domain;

import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 店家基本資料物件 pro_store_info
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public class StoreInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商家的user_id */
    private Long userId;

    /** 商家名稱 */
    @Excel(name = "商家名稱")
    private String name;

    /** 銀行帳戶 */
    @Excel(name = "銀行帳戶")
    private String bankAccount;

    /** ACH提回行代號 */
    @Excel(name = "ACH提回行代號")
    private String bankAchCode;

    /** 商家地址 */
    @Excel(name = "商家地址")
    private String address;

    /** 商家經度 */
    @Excel(name = "商家經度")
    private Long latitude;

    /** 商家緯度 */
    @Excel(name = "商家緯度")
    private Long longtitude;

    /** 是否同意註冊條款 ( 0不同意 1同意 ) */
    @Excel(name = "是否同意註冊條款 ( 0不同意 1同意 )")
    private String isAgreeTerms;

    /** 同意條款時間 */
    @Excel(name = "同意條款時間", width = 30, dateFormat = "yyyy-MM-dd")
    private Date agreeTime;

    /** 帳號狀態 ( 0正常 1停用 2未驗證 ) */
    @Excel(name = "帳號狀態 ( 0正常 1停用 2未驗證 )")
    private String status;

    /** 是否強制變更密碼( 0否 1是 ) */
    @Excel(name = "是否強制變更密碼( 0否 1是 )")
    private String pwNeedReset;

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setBankAccount(String bankAccount)
    {
        this.bankAccount = bankAccount;
    }

    public String getBankAccount()
    {
        return bankAccount;
    }
    public void setBankAchCode(String bankAchCode)
    {
        this.bankAchCode = bankAchCode;
    }

    public String getBankAchCode()
    {
        return bankAchCode;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setLatitude(Long latitude)
    {
        this.latitude = latitude;
    }

    public Long getLatitude()
    {
        return latitude;
    }
    public void setLongtitude(Long longtitude)
    {
        this.longtitude = longtitude;
    }

    public Long getLongtitude()
    {
        return longtitude;
    }
    public void setIsAgreeTerms(String isAgreeTerms)
    {
        this.isAgreeTerms = isAgreeTerms;
    }

    public String getIsAgreeTerms()
    {
        return isAgreeTerms;
    }
    public void setAgreeTime(Date agreeTime)
    {
        this.agreeTime = agreeTime;
    }

    public Date getAgreeTime()
    {
        return agreeTime;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setPwNeedReset(String pwNeedReset)
    {
        this.pwNeedReset = pwNeedReset;
    }

    public String getPwNeedReset()
    {
        return pwNeedReset;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("name", getName())
                .append("bankAccount", getBankAccount())
                .append("bankAchCode", getBankAchCode())
                .append("address", getAddress())
                .append("latitude", getLatitude())
                .append("longtitude", getLongtitude())
                .append("isAgreeTerms", getIsAgreeTerms())
                .append("agreeTime", getAgreeTime())
                .append("status", getStatus())
                .append("pwNeedReset", getPwNeedReset())
                .append("updateTime", getUpdateTime())
                .append("createTime", getCreateTime())
                .toString();
    }
}