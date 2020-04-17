package com.promote.project.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * 路由配置資訊
 * 
 * @author ruoyi
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo
{
    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 是否隱藏路由，當設定 true 的時候該路由不會再側邊欄出現
     */
    private String hidden;

    /**
     * 重定向地址，當設定 noRedirect 的時候該路由在麵包屑導航中不可被點選
     */
    private String redirect;

    /**
     * 元件地址
     */
    private String component;

    /**
     * 當你一個路由下面的 children 宣告的路由大於1個時，自動會變成巢狀的模式--如元件頁面
     */
    private Boolean alwaysShow;

    /**
     * 其他元素
     */
    private MetaVo meta;

    /**
     * 子路由
     */
    private List<RouterVo> children;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getHidden()
    {
        return hidden;
    }

    public void setHidden(String hidden)
    {
        this.hidden = hidden;
    }

    public String getRedirect()
    {
        return redirect;
    }

    public void setRedirect(String redirect)
    {
        this.redirect = redirect;
    }

    public String getComponent()
    {
        return component;
    }

    public void setComponent(String component)
    {
        this.component = component;
    }

    public Boolean getAlwaysShow()
    {
        return alwaysShow;
    }

    public void setAlwaysShow(Boolean alwaysShow)
    {
        this.alwaysShow = alwaysShow;
    }

    public MetaVo getMeta()
    {
        return meta;
    }

    public void setMeta(MetaVo meta)
    {
        this.meta = meta;
    }

    public List<RouterVo> getChildren()
    {
        return children;
    }

    public void setChildren(List<RouterVo> children)
    {
        this.children = children;
    }
}
