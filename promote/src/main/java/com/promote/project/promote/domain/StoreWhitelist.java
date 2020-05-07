package com.promote.project.promote.domain;

import com.promote.framework.web.domain.BaseEntity;

/**
 * 店家白名單檔物件 store_whitelist
 *
 * @author 6592 曾培晃
 * @date 2020-04-27
 */
public class StoreWhitelist extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 店家代碼 */
    private String storeUid;

    /** 店家名稱 */
    private String storeName;

    /** 公司登記名稱 */
    private String businessName;

    /** 店家統編/身分證字號 */
    private String taxNo;

    /** 攤位編號/許可證號 */
    private String licenseNo;

    /** 店家地址 */
    private String address;

    /** 店家地址郵遞區號 */
    private String zipCode;

    /** 負責人 */
    private String owner;

    /** 電話 */
    private String phoneNumber;

    /** 為列管夜市之攤商 */
    private String nMarket;

    /** 為列管市場之攤商 */
    private String tMarket;

    /** 夜市/市場名稱 */
    private String marketName;

    /** 店家業別為餐飲業 */
    private String foodBeverage;

    /** 店家業別為藝文產業 */
    private String cultural;

    /** 店家業別為觀光工廠 */
    private String sightSeeing;

    /** 列管/自理組織名稱 */
    private String adminOrg;

    /** 消費場所註記 */
    private String spaceMemo;

    /** 藝文消費類型 */
    private String culturalType;

    /** 位於商圈內 */
    private String shoppingArea;

    /** 商圈名稱 */
    private String areaName;

    /** 商圈代碼 */
    private String areaCode;

    public String getStoreUid() {
        return storeUid;
    }

    public void setStoreUid(String storeUid) {
        this.storeUid = storeUid;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNMarket() {
        return nMarket;
    }

    public void setNMarket(String nMarket) {
        this.nMarket = nMarket;
    }

    public String getTMarket() {
        return tMarket;
    }

    public void setTMarket(String tMarket) {
        this.tMarket = tMarket;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getFoodBeverage() {
        return foodBeverage;
    }

    public void setFoodBeverage(String foodBeverage) {
        this.foodBeverage = foodBeverage;
    }

    public String getCultural() {
        return cultural;
    }

    public void setCultural(String cultural) {
        this.cultural = cultural;
    }

    public String getSightSeeing() {
        return sightSeeing;
    }

    public void setSightSeeing(String sightSeeing) {
        this.sightSeeing = sightSeeing;
    }

    public String getAdminOrg() {
        return adminOrg;
    }

    public void setAdminOrg(String adminOrg) {
        this.adminOrg = adminOrg;
    }

    public String getSpaceMemo() {
        return spaceMemo;
    }

    public void setSpaceMemo(String spaceMemo) {
        this.spaceMemo = spaceMemo;
    }

    public String getCulturalType() {
        return culturalType;
    }

    public void setCulturalType(String culturalType) {
        this.culturalType = culturalType;
    }

    public String getShoppingArea() {
        return shoppingArea;
    }

    public void setShoppingArea(String shoppingArea) {
        this.shoppingArea = shoppingArea;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
