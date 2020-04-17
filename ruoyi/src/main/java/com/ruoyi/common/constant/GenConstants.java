package com.ruoyi.common.constant;

/**
 * 程式碼生成通用常量
 * 
 * @author ruoyi
 */
public class GenConstants
{
    /** 單表（增刪改查） */
    public static final String TPL_CRUD = "crud";

    /** 樹表（增刪改查） */
    public static final String TPL_TREE = "tree";

    /** 樹編碼欄位 */
    public static final String TREE_CODE = "treeCode";

    /** 樹父編碼欄位 */
    public static final String TREE_PARENT_CODE = "treeParentCode";

    /** 樹名稱欄位 */
    public static final String TREE_NAME = "treeName";

    /** 資料庫字串型別 */
    public static final String[] COLUMNTYPE_STR = { "char", "varchar", "narchar", "varchar2", "tinytext", "text",
            "mediumtext", "longtext" };

    /** 資料庫時間型別 */
    public static final String[] COLUMNTYPE_TIME = { "datetime", "time", "date", "timestamp" };

    /** 資料庫數字型別 */
    public static final String[] COLUMNTYPE_NUMBER = { "tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bigint", "float", "float", "double", "decimal" };

    /** 頁面不需要編輯欄位 */
    public static final String[] COLUMNNAME_NOT_EDIT = { "id", "create_by", "create_time", "del_flag" };

    /** 頁面不需要顯示的列表欄位 */
    public static final String[] COLUMNNAME_NOT_LIST = { "id", "create_by", "create_time", "del_flag", "update_by",
            "update_time" };

    /** 頁面不需要查詢欄位 */
    public static final String[] COLUMNNAME_NOT_QUERY = { "id", "create_by", "create_time", "del_flag", "update_by",
            "update_time", "remark" };

    /** Entity基類欄位 */
    public static final String[] BASE_ENTITY = { "createBy", "createTime", "updateBy", "updateTime", "remark" };

    /** Tree基類欄位 */
    public static final String[] TREE_ENTITY = { "parentName", "parentId", "orderNum", "ancestors", "children" };

    /** 文字框 */
    public static final String HTML_INPUT = "input";

    /** 文字域 */
    public static final String HTML_TEXTAREA = "textarea";

    /** 下拉框 */
    public static final String HTML_SELECT = "select";

    /** 單選框 */
    public static final String HTML_RADIO = "radio";

    /** 複選框 */
    public static final String HTML_CHECKBOX = "checkbox";

    /** 日期控制元件 */
    public static final String HTML_DATETIME = "datetime";

    /** 字串型別 */
    public static final String TYPE_STRING = "String";

    /** 整型 */
    public static final String TYPE_INTEGER = "Integer";

    /** 長整型 */
    public static final String TYPE_LONG = "Long";

    /** 浮點型 */
    public static final String TYPE_DOUBLE = "Double";

    /** 高精度計算型別 */
    public static final String TYPE_BIGDECIMAL = "BigDecimal";

    /** 時間型別 */
    public static final String TYPE_DATE = "Date";

    /** 模糊查詢 */
    public static final String QUERY_LIKE = "LIKE";

    /** 需要 */
    public static final String REQUIRE = "1";
}
