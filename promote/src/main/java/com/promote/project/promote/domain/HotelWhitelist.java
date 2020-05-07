package com.promote.project.promote.domain;

import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 旅宿白名單檔物件
 *
 * @author 6592 曾培晃
 * @date 2020-05-06
 */
public class HotelWhitelist extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 旅宿業者代號 */
    private String dataId;

    /** 狀態 */
    private String status;

    /** 旅館民宿名稱 */
    private String name;

    /** 星級 */
    private String grade;

    /** 旅館民宿地址 */
    private String add;

    /** 地址的郵遞區號 */
    private String zipCode;

    /** 電話 */
    private String tel;

    /** 傳真 */
    private String fax;

    /** 旅館民宿管理權責單位 */
    private String gov;

    /** 旅館民宿網站 */
    private String webSite;

    /** X座標 */
    private String px;

    /** Y座標 */
    private String py;

    /** 類別 */
    private String type;

    /** 地圖連結網址 */
    private String map;

    /** 房型價目說明 */
    private String spec;

    /** 服務內容介紹 */
    private String serviceInfo;

    /** 停車資訊 */
    private String parkingInfo;

    /** 更新時間 */
    private String changeTime;

    /** 旅宿網網址 */
    private String taiwanStayReferenceUrl;

    /** 房間數 */
    private String totalNumberOfRooms;

    /** 低價位 */
    private String lowestPrice;

    /** 高價位 */
    private String ceilingPrice;

    /** 統一編號 */
    private String unifiedBusinessNo;

    /** taiwanhost標記 */
    private String taiwanHost;

    /** 縣市 */
    private String region;

    /** 鄉鎮市區 */
    private String town;

    /** 旅館民宿id(經雜湊) */
    private String hotelHomeId;

    /** 業者email */
    private String industryEmail;

    /** 員工人數 */
    private String totalNumberOfPeople;

    /** 無障礙客房 */
    private String accessibilityRooms;

    /** 公共廁所 */
    private String publicToilets;

    /** 起重設備 */
    private String liftingEquipment;

    /** 停車位 */
    private String parkingSpace;

    /** 業者證號數字 */
    private String markNumber;

    /** 業者證號敘述 */
    private String markNumberText;

    /** 業者預設帳號 */
    private String firstUserName;

    /** 首次登入密碼 */
    private String firstPassword;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getGov() {
        return gov;
    }

    public void setGov(String gov) {
        this.gov = gov;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getPx() {
        return px;
    }

    public void setPx(String px) {
        this.px = px;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(String serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public String getParkingInfo() {
        return parkingInfo;
    }

    public void setParkingInfo(String parkingInfo) {
        this.parkingInfo = parkingInfo;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public String getTaiwanStayReferenceUrl() {
        return taiwanStayReferenceUrl;
    }

    public void setTaiwanStayReferenceUrl(String taiwanStayReferenceUrl) {
        this.taiwanStayReferenceUrl = taiwanStayReferenceUrl;
    }

    public String getTotalNumberOfRooms() {
        return totalNumberOfRooms;
    }

    public void setTotalNumberOfRooms(String totalNumberOfRooms) {
        this.totalNumberOfRooms = totalNumberOfRooms;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getCeilingPrice() {
        return ceilingPrice;
    }

    public void setCeilingPrice(String ceilingPrice) {
        this.ceilingPrice = ceilingPrice;
    }

    public String getUnifiedBusinessNo() {
        return unifiedBusinessNo;
    }

    public void setUnifiedBusinessNo(String unifiedBusinessNo) {
        this.unifiedBusinessNo = unifiedBusinessNo;
    }

    public String getTaiwanHost() {
        return taiwanHost;
    }

    public void setTaiwanHost(String taiwanHost) {
        this.taiwanHost = taiwanHost;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getHotelHomeId() {
        return hotelHomeId;
    }

    public void setHotelHomeId(String hotelHomeId) {
        this.hotelHomeId = hotelHomeId;
    }

    public String getIndustryEmail() {
        return industryEmail;
    }

    public void setIndustryEmail(String industryEmail) {
        this.industryEmail = industryEmail;
    }

    public String getTotalNumberOfPeople() {
        return totalNumberOfPeople;
    }

    public void setTotalNumberOfPeople(String totalNumberOfPeople) {
        this.totalNumberOfPeople = totalNumberOfPeople;
    }

    public String getAccessibilityRooms() {
        return accessibilityRooms;
    }

    public void setAccessibilityRooms(String accessibilityRooms) {
        this.accessibilityRooms = accessibilityRooms;
    }

    public String getPublicToilets() {
        return publicToilets;
    }

    public void setPublicToilets(String publicToilets) {
        this.publicToilets = publicToilets;
    }

    public String getLiftingEquipment() {
        return liftingEquipment;
    }

    public void setLiftingEquipment(String liftingEquipment) {
        this.liftingEquipment = liftingEquipment;
    }

    public String getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(String parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public String getMarkNumber() {
        return markNumber;
    }

    public void setMarkNumber(String markNumber) {
        this.markNumber = markNumber;
    }

    public String getMarkNumberText() {
        return markNumberText;
    }

    public void setMarkNumberText(String markNumberText) {
        this.markNumberText = markNumberText;
    }

    public String getFirstUserName() {
        return firstUserName;
    }

    public void setFirstUserName(String firstUserName) {
        this.firstUserName = firstUserName;
    }

    public String getFirstPassword() {
        return firstPassword;
    }

    public void setFirstPassword(String firstPassword) {
        this.firstPassword = firstPassword;
    }
}