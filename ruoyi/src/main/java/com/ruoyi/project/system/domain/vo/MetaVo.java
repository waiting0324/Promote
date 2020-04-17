package com.ruoyi.project.system.domain.vo;

/**
 * 路由顯示資訊
 * 
 * @author ruoyi
 */
public class MetaVo
{
    /**
     * 設定該路由在側邊欄和麵包屑中展示的名字
     */
    private String title;

    /**
     * 設定該路由的圖示，對應路徑src/icons/svg
     */
    private String icon;

    public MetaVo()
    {
    }

    public MetaVo(String title, String icon)
    {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }
}
