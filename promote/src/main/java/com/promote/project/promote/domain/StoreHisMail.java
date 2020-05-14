package com.promote.project.promote.domain;

import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 14.	店家查詢明細郵件表物件 pro_store_his_mail
 *
 * @author 6382 卓奕昌
 * @date 2020-05-14
 */
public class StoreHisMail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long storeId;
    private String startDate;
    private String endDate;
    private String status;
    private String storeType;
    private String mail;
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getStoreId() {
        return storeId;
    }
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStoreType() {
        return storeType;
    }
    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}