package com.promote.project.system.domain;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.promote.framework.web.domain.BaseEntity;

/**
 * 部門表 sys_dept
 * 
 * @author ruoyi
 */
public class SysDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 部門ID */
    private Long deptId;

    /** 父部門ID */
    private Long parentId;

    /** 祖級列表 */
    private String ancestors;

    /** 部門名稱 */
    private String deptName;

    /** 顯示順序 */
    private String orderNum;

    /** 負責人 */
    private String leader;

    /** 聯絡電話 */
    private String phone;

    /** 郵箱 */
    private String email;

    /** 部門狀態:0正常,1停用 */
    private String status;

    /** 刪除標誌（0代表存在 2代表刪除） */
    private String delFlag;

    /** 父部門名稱 */
    private String parentName;
    
    /** 子部門 */
    private List<SysDept> children = new ArrayList<SysDept>();

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "部門名稱不能為空")
    @Size(min = 0, max = 30, message = "部門名稱長度不能超過30個字元")
    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    @NotBlank(message = "顯示順序不能為空")
    public String getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(String orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getLeader()
    {
        return leader;
    }

    public void setLeader(String leader)
    {
        this.leader = leader;
    }

    @Size(min = 0, max = 11, message = "聯絡電話長度不能超過11個字元")
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
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

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public List<SysDept> getChildren()
    {
        return children;
    }

    public void setChildren(List<SysDept> children)
    {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deptId", getDeptId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("deptName", getDeptName())
            .append("orderNum", getOrderNum())
            .append("leader", getLeader())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
