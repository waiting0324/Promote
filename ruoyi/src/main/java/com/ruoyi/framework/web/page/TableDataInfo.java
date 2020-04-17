package com.ruoyi.framework.web.page;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分頁資料物件
 * 
 * @author ruoyi
 */
public class TableDataInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 總記錄數 */
    private long total;

    /** 列表資料 */
    private List<?> rows;

    /** 訊息狀態碼 */
    private int code;

    /** 訊息內容 */
    private int msg;

    /**
     * 表格資料物件
     */
    public TableDataInfo()
    {
    }

    /**
     * 分頁
     * 
     * @param list 列表資料
     * @param total 總記錄數
     */
    public TableDataInfo(List<?> list, int total)
    {
        this.rows = list;
        this.total = total;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<?> getRows()
    {
        return rows;
    }

    public void setRows(List<?> rows)
    {
        this.rows = rows;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public int getMsg()
    {
        return msg;
    }

    public void setMsg(int msg)
    {
        this.msg = msg;
    }
}