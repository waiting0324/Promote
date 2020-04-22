package com.promote.project.system.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.promote.framework.aspectj.lang.annotation.Excel.Type;
import com.promote.framework.aspectj.lang.annotation.Excels;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 使用者物件 sys_user
 * 
 * @author ruoyi
 */
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 使用者ID */
    @Excel(name = "使用者序號", cellType = ColumnType.NUMERIC, prompt = "使用者編號")
    private Long userId;

    /** 使用者 身分證/居留證/統一編號 */
    private String identity;

    /** 使用者生日 */
    private String birthday;

    /** 部門ID */
    @Excel(name = "部門編號", type = Type.IMPORT)
    private Long deptId;

    /** 使用者帳號 */
    @Excel(name = "登入名稱")
    private String userName;

    /** 密碼 */
    private String password;

    /** 使用者 姓名 or 商店名稱 */
    @Excel(name = "使用者名稱")
    private String name;

    /** 抵用券領用類型 */
    @Excel(name = "抵用券領用類型", readConverterExp = "0=電子,1=紙本")
    private String couponType;

    /** 抵用券 列印兌換碼 */
    private String couponPrintCode;

    /** 抵用券 列印狀態 */
    private String couponPrintType;

    /** 抵用券 發放狀態 */
    private String couponProvideType;

    /** 抵用券 發放時間 */
    private Date couponProvideTime;

    /** 使用者郵箱 */
    @Excel(name = "使用者郵箱")
    private String email;

    /** 手機號碼 */
    @Excel(name = "手機號碼")
    private String phonenumber;

    /** 銀行帳戶 */
    private String bankAccount;

    /** 使用者性別 */
    @Excel(name = "使用者性別", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 使用者頭像 */
    private String avatar;

    /** 商家緯度 */
    private Double latitude;

    /** 商家經度 */
    private Double longitude;

    /** 商家地址 */
    private String address;

    /** 旅宿業者是否協助發放抵用券 ( 0不協助 1協助) */
    private String isSupportCoupon;

    /** 是否同意註冊條款 (0 不同意 1同意 ) */
    private String isAgreeTerms;

    /** 鹽加密 */
    private String salt;

    /** 帳號狀態（0正常 1停用） */
    @Excel(name = "帳號狀態", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 刪除標誌（0代表存在 2代表刪除） */
    private String delFlag;

    /** 最後登陸IP */
    @Excel(name = "最後登陸IP", type = Type.EXPORT)
    private String loginIp;

    /** 最後登陸時間 */
    @Excel(name = "最後登陸時間", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /** 部門物件 */
    @Excels({
        @Excel(name = "部門名稱", targetAttr = "deptName", type = Type.EXPORT),
        @Excel(name = "部門負責人", targetAttr = "leader", type = Type.EXPORT)
    })
    private SysDept dept;

    /** 角色物件 */
    private List<SysRole> roles;

    /** 角色組 */
    private Long[] roleIds;

    /** 崗位組 */
    private Long[] postIds;

    public SysUser()
    {

    }


    @NotBlank(message = "使用者生日不能為空")
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponPrintCode() {
        return couponPrintCode;
    }

    public void setCouponPrintCode(String couponPrintCode) {
        this.couponPrintCode = couponPrintCode;
    }

    public String getCouponPrintType() {
        return couponPrintType;
    }

    public void setCouponPrintType(String couponPrintType) {
        this.couponPrintType = couponPrintType;
    }

    public String getCouponProvideType() {
        return couponProvideType;
    }

    public void setCouponProvideType(String couponProvideType) {
        this.couponProvideType = couponProvideType;
    }

    public Date getCouponProvideTime() {
        return couponProvideTime;
    }

    public void setCouponProvideTime(Date couponProvideTime) {
        this.couponProvideTime = couponProvideTime;
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

    public SysUser(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }


    @NotBlank(message = "使用者帳號不能為空")
    @Size(min = 0, max = 30, message = "使用者帳號長度不能超過30個字元")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Email(message = "郵箱格式不正確")
    @Size(min = 0, max = 50, message = "郵箱長度不能超過50個字元")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Size(min = 0, max = 10, message = "手機號碼長度不能超過10個字元")
    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    @JsonProperty
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getLoginIp()
    {
        return loginIp;
    }

    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public Date getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(Date loginDate)
    {
        this.loginDate = loginDate;
    }

    public SysDept getDept()
    {
        return dept;
    }

    public void setDept(SysDept dept)
    {
        this.dept = dept;
    }

    public List<SysRole> getRoles()
    {
        return roles;
    }

    public void setRoles(List<SysRole> roles)
    {
        this.roles = roles;
    }

    public Long[] getRoleIds()
    {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds)
    {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds()
    {
        return postIds;
    }

    public void setPostIds(Long[] postIds)
    {
        this.postIds = postIds;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsSupportCoupon() {
        return isSupportCoupon;
    }

    public void setIsSupportCoupon(String isSupportCoupon) {
        this.isSupportCoupon = isSupportCoupon;
    }

    public String getIsAgreeTerms() {
        return isAgreeTerms;
    }

    public void setIsAgreeTerms(String isAgreeTerms) {
        this.isAgreeTerms = isAgreeTerms;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("userName", getUserName())
            .append("email", getEmail())
            .append("phonenumber", getPhonenumber())
            .append("sex", getSex())
            .append("avatar", getAvatar())
            .append("password", getPassword())
            .append("salt", getSalt())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("loginIp", getLoginIp())
            .append("loginDate", getLoginDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("dept", getDept())
            .toString();
    }
}
