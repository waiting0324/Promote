package com.promote.project.promote.domain;

import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;

/**
 * 白名單物件 pro_whitelist
 * 
 * @author 6550 劉威廷
 * @date 2020-04-20
 */
public class ProWhitelist extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流水號 */
    private Long id;

    /** 負責人姓名 */
    @Excel(name = "負責人姓名")
    private String owner;

    /** 商家/旅宿 名稱 */
    @Excel(name = "商家/旅宿 名稱")
    private String name;

    /** 統編/身分證字號 */
    @Excel(name = "統編/身分證字號")
    private String taxNo;

    /** 旅宿業者預設帳號 */
    @Excel(name = "旅宿業者預設帳號")
    private String username;

    /** 旅宿業者預設密碼 */
    @Excel(name = "旅宿業者預設密碼")
    private String password;

    /** 地址 (帶上郵遞區號) */
    @Excel(name = "地址 (帶上郵遞區號)")
    private String address;

    /** 電話 */
    @Excel(name = "電話")
    private String phonenumber;

    /** 電子信箱 */
    @Excel(name = "電子信箱")
    private String email;

    /** 資料類型 (1旅宿業者 2商家) */
    @Excel(name = "資料類型 (1旅宿業者 2商家)")
    private String type;

    /** 是否為夜市攤商 ( 0否 1是 ) */
    @Excel(name = "是否為夜市攤商 ( 0否 1是 )")
    private String isNMarket;

    /** 是否為市場攤商 ( 0否 1是 ) */
    @Excel(name = "是否為市場攤商 ( 0否 1是 )")
    private String isTMarket;

    /** 是否為餐飲業 ( 0否 1是 ) */
    @Excel(name = "是否為餐飲業 ( 0否 1是 )")
    private String isFoodbeverage;

    /** 是否為藝文產業 ( 0否 1是 ) */
    @Excel(name = "是否為藝文產業 ( 0否 1是 )")
    private String isCulture;

    /** 是否為觀光工廠 ( 0否 1是 ) */
    @Excel(name = "是否為觀光工廠 ( 0否 1是 )")
    private String isSightseeing;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOwner(String owner) 
    {
        this.owner = owner;
    }

    public String getOwner() 
    {
        return owner;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setTaxNo(String taxNo) 
    {
        this.taxNo = taxNo;
    }

    public String getTaxNo() 
    {
        return taxNo;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setPhonenumber(String phonenumber) 
    {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber() 
    {
        return phonenumber;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setIsNMarket(String isNMarket) 
    {
        this.isNMarket = isNMarket;
    }

    public String getIsNMarket() 
    {
        return isNMarket;
    }
    public void setIsTMarket(String isTMarket) 
    {
        this.isTMarket = isTMarket;
    }

    public String getIsTMarket() 
    {
        return isTMarket;
    }
    public void setIsFoodbeverage(String isFoodbeverage) 
    {
        this.isFoodbeverage = isFoodbeverage;
    }

    public String getIsFoodbeverage() 
    {
        return isFoodbeverage;
    }
    public void setIsCulture(String isCulture) 
    {
        this.isCulture = isCulture;
    }

    public String getIsCulture() 
    {
        return isCulture;
    }
    public void setIsSightseeing(String isSightseeing) 
    {
        this.isSightseeing = isSightseeing;
    }

    public String getIsSightseeing() 
    {
        return isSightseeing;
    }

    @Override
    public String toString() {
        /*return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("owner", getOwner())
            .append("name", getName())
            .append("taxNo", getTaxNo())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("address", getAddress())
            .append("phonenumber", getPhonenumber())
            .append("email", getEmail())
            .append("type", getType())
            .append("isNMarket", getIsNMarket())
            .append("isTMarket", getIsTMarket())
            .append("isFoodbeverage", getIsFoodbeverage())
            .append("isCulture", getIsCulture())
            .append("isSightseeing", getIsSightseeing())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .toString();*/
        return "";
    }
}
