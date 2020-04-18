package com.promote.project.system.domain;

import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 白名單表 pro_whitelist
 *
 * @author 曾培晃
 */
public class ProWhitelist extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 白名單主鍵
     */
    private Long whitelistId;

    /**
     * 白名單帳號
     */
    private String whitelistAcct;

    /**
     * 白名單密碼
     */
    private String whitelistPwd;

    /**
     * 白名單旅宿業者名
     */
    private String whitelistName;

    /**
     * 白名單旅宿業者電話
     */
    private String whitelistPhone;

    /**
     * 白名單旅宿業者地址
     */
    private String whitelistAddr;


    /**
     * 白名單旅宿業者統編
     */
    private Integer whitelistTaxIdNo;

    public Long getWhitelistId() {
        return whitelistId;
    }

    public void setWhitelistId(Long whitelistId) {
        this.whitelistId = whitelistId;
    }

    public String getWhitelistAcct() {
        return whitelistAcct;
    }

    public void setWhitelistAcct(String whitelistAcct) {
        this.whitelistAcct = whitelistAcct;
    }

    public String getWhitelistPwd() {
        return whitelistPwd;
    }

    public void setWhitelistPwd(String whitelistPwd) {
        this.whitelistPwd = whitelistPwd;
    }

    public String getWhitelistName() {
        return whitelistName;
    }

    public void setWhitelistName(String whitelistName) {
        this.whitelistName = whitelistName;
    }

    public String getWhitelistPhone() {
        return whitelistPhone;
    }

    public void setWhitelistPhone(String whitelistPhone) {
        this.whitelistPhone = whitelistPhone;
    }

    public String getWhitelistAddr() {
        return whitelistAddr;
    }

    public void setWhitelistAddr(String whitelistAddr) {
        this.whitelistAddr = whitelistAddr;
    }

    public Integer getWhitelistTaxIdNo() {
        return whitelistTaxIdNo;
    }

    public void setWhitelistTaxIdNo(Integer whitelistTaxIdNo) {
        this.whitelistTaxIdNo = whitelistTaxIdNo;
    }
}
