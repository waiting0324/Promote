package com.promote.project.system.domain;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.promote.framework.web.domain.BaseEntity;

/**
 * 選單許可權表 sys_menu
 * 
 * @author ruoyi
 */
public class SysMenu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 選單ID */
    private Long menuId;

    /** 選單名稱 */
    private String menuName;

    /** 父選單名稱 */
    private String parentName;

    /** 父選單ID */
    private Long parentId;

    /** 顯示順序 */
    private String orderNum;

    /** 路由地址 */
    private String path;

    /** 元件路徑 */
    private String component;

    /** 是否為外鏈（0是 1否） */
    private String isFrame;

    /** 型別（M目錄 C選單 F按鈕） */
    private String menuType;

    /** 選單狀態:0顯示,1隱藏 */
    private String visible;

    /** 許可權字串 */
    private String perms;

    /** 選單圖示 */
    private String icon;

    /** 子選單 */
    private List<SysMenu> children = new ArrayList<SysMenu>();

    public Long getMenuId()
    {
        return menuId;
    }

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

    @NotBlank(message = "選單名稱不能為空")
    @Size(min = 0, max = 50, message = "選單名稱長度不能超過50個字元")
    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
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

    @Size(min = 0, max = 200, message = "路由地址不能超過200個字元")
    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    @Size(min = 0, max = 200, message = "元件路徑不能超過255個字元")
    public String getComponent()
    {
        return component;
    }

    public void setComponent(String component)
    {
        this.component = component;
    }

    public String getIsFrame()
    {
        return isFrame;
    }

    public void setIsFrame(String isFrame)
    {
        this.isFrame = isFrame;
    }

    @NotBlank(message = "選單型別不能為空")
    public String getMenuType()
    {
        return menuType;
    }

    public void setMenuType(String menuType)
    {
        this.menuType = menuType;
    }

    public String getVisible()
    {
        return visible;
    }

    public void setVisible(String visible)
    {
        this.visible = visible;
    }

    @Size(min = 0, max = 100, message = "許可權標識長度不能超過100個字元")
    public String getPerms()
    {
        return perms;
    }

    public void setPerms(String perms)
    {
        this.perms = perms;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public List<SysMenu> getChildren()
    {
        return children;
    }

    public void setChildren(List<SysMenu> children)
    {
        this.children = children;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("menuId", getMenuId())
            .append("menuName", getMenuName())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("path", getPath())
            .append("component", getComponent())
            .append("isFrame", getIsFrame())
            .append("menuType", getMenuType())
            .append("visible", getVisible())
            .append("perms", getPerms())
            .append("icon", getIcon())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
