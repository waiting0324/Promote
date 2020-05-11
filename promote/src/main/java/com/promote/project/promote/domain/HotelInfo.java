package com.promote.project.promote.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Timestamp;

/**
 * 旅宿業基本資料物件 pro_hostel_info
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotelInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 旅宿業者的user_id */
    private Integer userId;

    /** 旅宿業者名稱 */
    private String name;

    /** 是否協助發放抵用券( 0不協助 1協助 ) */
    private String isSupportCoupon;

    /** 同意條款時間 */
    private Timestamp agreeTime;

    /** 是否同意註冊條款 ( 0不同意 1同意 ) */
    private String isAgreeTerms;

    /** 地址 */
    private String address;

    /** 緯度 */
    private Double latitude;

    /** 經度 */
    private Double longitude;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Timestamp getAgreeTime() {
        return agreeTime;
    }

    public void setAgreeTime(Timestamp agreeTime) {
        this.agreeTime = agreeTime;
    }

    public void setIsAgreeTerms(String isAgreeTerms)
    {
        this.isAgreeTerms = isAgreeTerms;
    }

    public String getIsAgreeTerms()
    {
        return isAgreeTerms;
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
                .append("updateTime", getUpdateTime())
                .append("createTime", getCreateTime())
                .toString();
    }
}