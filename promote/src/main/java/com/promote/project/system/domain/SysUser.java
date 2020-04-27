package com.promote.project.system.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.promote.framework.aspectj.lang.annotation.Excel.Type;
import com.promote.framework.aspectj.lang.annotation.Excels;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 使用者物件 sys_user
 *
 * @author ruoyi
 */
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 使用者ID
     */
    @Excel(name = "使用者序號", cellType = ColumnType.NUMERIC, prompt = "使用者編號")
    private Long userId;

    /**
     * 使用者 身分證/居留證/統一編號
     */
    private String identity;


    /**
     * 部門ID
     */
    @Excel(name = "部門編號", type = Type.IMPORT)
    private Long deptId;

    /**
     * 使用者帳號
     */
    @Excel(name = "登入名稱")
    private String username;

    /**
     * 密碼
     */
    private String password;

    /**
     * 電子信箱
     */
    private String email;

    /**
     * 手機號碼
     */
    private String mobile;

    /**
     * 鹽加密
     */
    private String salt;

    /**
     * 帳號狀態（0正常 1停用）
     */
    @Excel(name = "帳號狀態", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 刪除標誌（0代表存在 2代表刪除）
     */
    private String delFlag;

    /**
     * 最後登陸IP
     */
    @Excel(name = "最後登陸IP", type = Type.EXPORT)
    private String loginIp;

    /**
     * 最後登陸時間
     */
    @Excel(name = "最後登陸時間", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /**
     * 部門物件
     */
    @Excels({
            @Excel(name = "部門名稱", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部門負責人", targetAttr = "leader", type = Type.EXPORT)
    })
    private SysDept dept;

    /**
     * 角色物件
     */
    private List<SysRole> roles;

    /**
     * 角色組
     */
    private Long[] roleIds;

    /**
     * 崗位組
     */
    private Long[] postIds;

    public SysUser() {

    }


    public SysUser(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @NotBlank(message = "使用者帳號不能為空")
    @Size(min = 0, max = 30, message = "使用者帳號長度不能超過30個字元")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }


    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("deptId", getDeptId())
                .append("userName", getUsername())
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
