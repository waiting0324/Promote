package com.promote.framework.aspectj.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.promote.framework.aspectj.lang.enums.DataSourceType;

/**
 * 自定義多資料來源切換註解
 * 
 * @author ruoyi
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource
{
    /**
     * 切換資料來源名稱
     */
    public DataSourceType value() default DataSourceType.MASTER;
}
