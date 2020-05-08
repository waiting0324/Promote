package com.promote.project.promote.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 旅宿業基本資料物件 pro_hostel_info
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HostelInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 旅宿業者的user_id */
    private Long userId;

    /** 旅宿業者名稱 */
    @Excel(name = "旅宿業者名稱")
    private String name;

    /** 商家類型 ( 0夜市 1餐廳 2商圈 3藝文 ) */
    private String type;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 緯度 */
    @Excel(name = "緯度")
    private Double latitude;

    /** 經度 */
    @Excel(name = "經度")
    private Double longitude;

    /** 是否協助發放抵用券( 0不協助 1協助 ) */
    @Excel(name = "是否協助發放抵用券( 0不協助 1協助 )")
    private String isSupportCoupon;

    /** 同意條款時間 */
    @Excel(name = "同意條款時間", width = 30, dateFormat = "yyyy-MM-dd")
    private Date agreeTime;

    /** 是否同意註冊條款 ( 0不同意 1同意 ) */
    @Excel(name = "是否同意註冊條款 ( 0不同意 1同意 )")
    private String isAgreeTerms;

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
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setIsSupportCoupon(String isSupportCoupon)
    {
        this.isSupportCoupon = isSupportCoupon;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getIsSupportCoupon()
    {
        return isSupportCoupon;
    }
    public void setAgreeTime(Date agreeTime)
    {
        this.agreeTime = agreeTime;
    }

    public Date getAgreeTime()
    {
        return agreeTime;
    }
    public void setIsAgreeTerms(String isAgreeTerms)
    {
        this.isAgreeTerms = isAgreeTerms;
    }

    public String getIsAgreeTerms()
    {
        return isAgreeTerms;
    }
    public void setPwNeedReset(String pwNeedReset)
    {
        this.pwNeedReset = pwNeedReset;
    }

    public String getPwNeedReset()
    {
        return pwNeedReset;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("name", getName())
                .append("address", getAddress())
                .append("latitude", getLatitude())
                .append("longitude", getLongitude())
                .append("isSupportCoupon", getIsSupportCoupon())
                .append("agreeTime", getAgreeTime())
                .append("isAgreeTerms", getIsAgreeTerms())
                .append("pwNeedReset", getPwNeedReset())
                .append("updateTime", getUpdateTime())
                .append("createTime", getCreateTime())
                .toString();
    }
}