package com.ruoyi.common.utils.sql;

import com.ruoyi.common.utils.StringUtils;

/**
 * sql操作工具類
 * 
 * @author ruoyi
 */
public class SqlUtil
{
    /**
     * 僅支援字母、數字、下劃線、空格、逗號（支援多個欄位排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,]+";

    /**
     * 檢查字元，防止注入繞過
     */
    public static String escapeOrderBySql(String value)
    {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value))
        {
            return StringUtils.EMPTY;
        }
        return value;
    }

    /**
     * 驗證 order by 語法是否符合規範
     */
    public static boolean isValidOrderBySql(String value)
    {
        return value.matches(SQL_PATTERN);
    }
}
