package com.ruoyi.framework.aspectj.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定義匯出Excel資料註解
 * 
 * @author ruoyi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel
{
    /**
     * 匯出到Excel中的名字.
     */
    public String name() default "";

    /**
     * 日期格式, 如: yyyy-MM-dd
     */
    public String dateFormat() default "";

    /**
     * 讀取內容轉表示式 (如: 0=男,1=女,2=未知)
     */
    public String readConverterExp() default "";

    /**
     * 匯出型別（0數字 1字串）
     */
    public ColumnType cellType() default ColumnType.STRING;

    /**
     * 匯出時在excel中每個列的高度 單位為字元
     */
    public double height() default 14;

    /**
     * 匯出時在excel中每個列的寬 單位為字元
     */
    public double width() default 16;

    /**
     * 文字字尾,如% 90 變成90%
     */
    public String suffix() default "";

    /**
     * 當值為空時,欄位的預設值
     */
    public String defaultValue() default "";

    /**
     * 提示資訊
     */
    public String prompt() default "";

    /**
     * 設定只能選擇不能輸入的列內容.
     */
    public String[] combo() default {};

    /**
     * 是否匯出資料,應對需求:有時我們需要匯出一份模板,這是標題需要但內容需要使用者手工填寫.
     */
    public boolean isExport() default true;

    /**
     * 另一個類中的屬性名稱,支援多級獲取,以小數點隔開
     */
    public String targetAttr() default "";

    /**
     * 欄位型別（0：匯出匯入；1：僅匯出；2：僅匯入）
     */
    Type type() default Type.ALL;

    public enum Type
    {
        ALL(0), EXPORT(1), IMPORT(2);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    public enum ColumnType
    {
        NUMERIC(0), STRING(1);
        private final int value;

        ColumnType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }
}