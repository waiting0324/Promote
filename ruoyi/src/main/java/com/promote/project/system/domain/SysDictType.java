package com.promote.project.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.promote.framework.web.domain.BaseEntity;

/**
 * 字典型別表 sys_dict_type
 * 
 * @author ruoyi
 */
public class SysDictType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 字典主鍵 */
    @Excel(name = "字典主鍵", cellType = ColumnType.NUMERIC)
    private Long dictId;

    /** 字典名稱 */
    @Excel(name = "字典名稱")
    private String dictName;

    /** 字典型別 */
    @Excel(name = "字典型別")
    private String dictType;

    /** 狀態（0正常 1停用） */
    @Excel(name = "狀態", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getDictId()
    {
        return dictId;
    }

    public void setDictId(Long dictId)
    {
        this.dictId = dictId;
    }

    @NotBlank(message = "字典名稱不能為空")
    @Size(min = 0, max = 100, message = "字典型別名稱長度不能超過100個字元")
    public String getDictName()
    {
        return dictName;
    }

    public void setDictName(String dictName)
    {
        this.dictName = dictName;
    }

    @NotBlank(message = "字典型別不能為空")
    @Size(min = 0, max = 100, message = "字典型別型別長度不能超過100個字元")
    public String getDictType()
    {
        return dictType;
    }

    public void setDictType(String dictType)
    {
        this.dictType = dictType;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictId", getDictId())
            .append("dictName", getDictName())
            .append("dictType", getDictType())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
