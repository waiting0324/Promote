package com.promote.project.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.promote.framework.web.domain.BaseEntity;

/**
 * 崗位表 sys_post
 * 
 * @author ruoyi
 */
public class SysPost extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 崗位序號 */
    @Excel(name = "崗位序號", cellType = ColumnType.NUMERIC)
    private Long postId;

    /** 崗位編碼 */
    @Excel(name = "崗位編碼")
    private String postCode;

    /** 崗位名稱 */
    @Excel(name = "崗位名稱")
    private String postName;

    /** 崗位排序 */
    @Excel(name = "崗位排序")
    private String postSort;

    /** 狀態（0正常 1停用） */
    @Excel(name = "狀態", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 使用者是否存在此崗位標識 預設不存在 */
    private boolean flag = false;

    public Long getPostId()
    {
        return postId;
    }

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    @NotBlank(message = "崗位編碼不能為空")
    @Size(min = 0, max = 64, message = "崗位編碼長度不能超過64個字元")
    public String getPostCode()
    {
        return postCode;
    }

    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }

    @NotBlank(message = "崗位名稱不能為空")
    @Size(min = 0, max = 50, message = "崗位名稱長度不能超過50個字元")
    public String getPostName()
    {
        return postName;
    }

    public void setPostName(String postName)
    {
        this.postName = postName;
    }

    @NotBlank(message = "顯示順序不能為空")
    public String getPostSort()
    {
        return postSort;
    }

    public void setPostSort(String postSort)
    {
        this.postSort = postSort;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("postId", getPostId())
            .append("postCode", getPostCode())
            .append("postName", getPostName())
            .append("postSort", getPostSort())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
