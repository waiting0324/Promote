package com.ruoyi.project.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 角色表 sys_role
 * 
 * @author ruoyi
 */
public class SysRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @Excel(name = "角色序號", cellType = ColumnType.NUMERIC)
    private Long roleId;

    /** 角色名稱 */
    @Excel(name = "角色名稱")
    private String roleName;

    /** 角色許可權 */
    @Excel(name = "角色許可權")
    private String roleKey;

    /** 角色排序 */
    @Excel(name = "角色排序")
    private String roleSort;

    /** 資料範圍（1：所有資料許可權；2：自定義資料許可權；3：本部門資料許可權；4：本部門及以下資料許可權） */
    @Excel(name = "資料範圍", readConverterExp = "1=所有資料許可權,2=自定義資料許可權,3=本部門資料許可權,4=本部門及以下資料許可權")
    private String dataScope;

    /** 角色狀態（0正常 1停用） */
    @Excel(name = "角色狀態", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 刪除標誌（0代表存在 2代表刪除） */
    private String delFlag;

    /** 使用者是否存在此角色標識 預設不存在 */
    private boolean flag = false;

    /** 選單組 */
    private Long[] menuIds;

    /** 部門組（資料許可權） */
    private Long[] deptIds;

    public SysRole()
    {

    }

    public SysRole(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1L == roleId;
    }

    @NotBlank(message = "角色名稱不能為空")
    @Size(min = 0, max = 30, message = "角色名稱長度不能超過30個字元")
    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    @NotBlank(message = "許可權字元不能為空")
    @Size(min = 0, max = 100, message = "許可權字元長度不能超過100個字元")
    public String getRoleKey()
    {
        return roleKey;
    }

    public void setRoleKey(String roleKey)
    {
        this.roleKey = roleKey;
    }

    @NotBlank(message = "顯示順序不能為空")
    public String getRoleSort()
    {
        return roleSort;
    }

    public void setRoleSort(String roleSort)
    {
        this.roleSort = roleSort;
    }

    public String getDataScope()
    {
        return dataScope;
    }

    public void setDataScope(String dataScope)
    {
        this.dataScope = dataScope;
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

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public Long[] getMenuIds()
    {
        return menuIds;
    }

    public void setMenuIds(Long[] menuIds)
    {
        this.menuIds = menuIds;
    }

    public Long[] getDeptIds()
    {
        return deptIds;
    }

    public void setDeptIds(Long[] deptIds)
    {
        this.deptIds = deptIds;
    }
    
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("roleSort", getRoleSort())
            .append("dataScope", getDataScope())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
