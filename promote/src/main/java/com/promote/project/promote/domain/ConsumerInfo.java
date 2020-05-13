package com.promote.project.promote.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 消費者基本資料物件 pro_consumer_info
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class    ConsumerInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消費者的user_id */
    private Long userId;

    /** 消費者姓名 */
    @Excel(name = "消費者姓名")
    private String name;


    /** 生日 */
    @Excel(name = "生日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date birthday;

    /** 消費者狀態
     1. 註冊
     2. 旅宿業確認(於畫面班認身分資料)
     3. 核發(登錄驗證碼，驗證正確時)
     4. 已列印(紙本) */
    private String consumerStat;


    /** 抵用券類型 ( P:紙本 E:電子 ) */
    @Excel(name = "抵用券類型 ( P:紙本 E:電子 )")
    private String couponType;

    /** 抵用券列印狀態 (0:未印 1:已印) */
    @Excel(name = "抵用券列印狀態 (0:未印 1:已印)")
    private String couponPrintType;

    /** 紙本兌換碼 */
    @Excel(name = "紙本兌換碼")
    private String printCode;

    /** 發放抵用券的旅宿業者 */
    @Excel(name = "發放抵用券的旅宿業者")
    private Long hotelId;

    /** 抵用券發放時間 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "抵用券發放時間", width = 30, dateFormat = "yyyy-MM-dd")
    private Date issueDate;

    /** 領取時間 ( 紙本才有) */
    @Excel(name = "領取時間 ( 紙本才有)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveDate;


    //----- 傳參用
    private String identity;

    private String mobile;

    private String username;

    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setConsumerStat(String consumerStat)
    {
        this.consumerStat = consumerStat;
    }

    public String getConsumerStat()
    {
        return consumerStat;
    }

    public void setCouponType(String couponType)
    {
        this.couponType = couponType;
    }

    public String getCouponType()
    {
        return couponType;
    }
    public void setCouponPrintType(String couponPrintType)
    {
        this.couponPrintType = couponPrintType;
    }

    public String getCouponPrintType()
    {
        return couponPrintType;
    }
    public void setPrintCode(String printCode)
    {
        this.printCode = printCode;
    }

    public String getPrintCode()
    {
        return printCode;
    }
    public void setHotelId(Long hotelId)
    {
        this.hotelId = hotelId;
    }

    public Long getHotelId()
    {
        return hotelId;
    }
    public void setIssueDate(Date issueDate)
    {
        this.issueDate = issueDate;
    }

    public Date getIssueDate()
    {
        return issueDate;
    }
    public void setReceiveDate(Date receiveDate)
    {
        this.receiveDate = receiveDate;
    }

    public Date getReceiveDate()
    {
        return receiveDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("name", getName())
                .append("birthday", getBirthday())
                .append("consumerStat", getConsumerStat())
                .append("couponType", getCouponType())
                .append("couponPrintType", getCouponPrintType())
                .append("printCode", getPrintCode())
                .append("hostelId", getHotelId())
                .append("issueDate", getIssueDate())
                .append("receiveDate", getReceiveDate())
                .append("updateTime", getUpdateTime())
                .append("createTime", getCreateTime())
                .toString();
    }
}