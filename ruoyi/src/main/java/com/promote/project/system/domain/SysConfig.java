package com.promote.project.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.promote.framework.web.domain.BaseEntity;

/**
 * 引數配置表 sys_config
 * 
 * @author ruoyi
 */
public class SysConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 引數主鍵 */
    @Excel(name = "引數主鍵", cellType = ColumnType.NUMERIC)
    private Long configId;

    /** 引數名稱 */
    @Excel(name = "引數名稱")
    private String configName;

    /** 引數鍵名 */
    @Excel(name = "引數鍵名")
    private String configKey;

    /** 引數鍵值 */
    @Excel(name = "引數鍵值")
    private String configValue;

    /** 系統內建（Y是 N否） */
    @Excel(name = "系統內建", readConverterExp = "Y=是,N=否")
    private String configType;

    public Long getConfigId()
    {
        return configId;
    }

    public void setConfigId(Long configId)
    {
        this.configId = configId;
    }

    @NotBlank(message = "引數名稱不能為空")
    @Size(min = 0, max = 100, message = "引數名稱不能超過100個字元")
    public String getConfigName()
    {
        return configName;
    }

    public void setConfigName(String configName)
    {
        this.configName = configName;
    }

    @NotBlank(message = "引數鍵名長度不能為空")
    @Size(min = 0, max = 100, message = "引數鍵名長度不能超過100個字元")
    public String getConfigKey()
    {
        return configKey;
    }

    public void setConfigKey(String configKey)
    {
        this.configKey = configKey;
    }

    @NotBlank(message = "引數鍵值不能為空")
    @Size(min = 0, max = 500, message = "引數鍵值長度不能超過500個字元")
    public String getConfigValue()
    {
        return configValue;
    }

    public void setConfigValue(String configValue)
    {
        this.configValue = configValue;
    }

    public String getConfigType()
    {
        return configType;
    }

    public void setConfigType(String configType)
    {
        this.configType = configType;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("configName", getConfigName())
            .append("configKey", getConfigKey())
            .append("configValue", getConfigValue())
            .append("configType", getConfigType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
