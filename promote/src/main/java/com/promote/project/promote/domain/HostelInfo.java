package com.promote.project.promote.domain;

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
public class HostelInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 旅宿業者的user_id */
    private Long userId;

    /** 旅宿業者名稱 */
    @Excel(name = "旅宿業者名稱")
    private String name;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 緯度 */
    @Excel(name = "緯度")
    private Float latitude;

    /** 經度 */
    @Excel(name = "經度")
    private Float longitude;

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

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
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