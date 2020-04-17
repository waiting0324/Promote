package com.promote.framework.web.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.promote.common.constant.HttpStatus;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.StringUtils;
import com.promote.common.utils.sql.SqlUtil;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.framework.web.page.PageDomain;
import com.promote.framework.web.page.TableDataInfo;
import com.promote.framework.web.page.TableSupport;

/**
 * web層通用資料處理
 * 
 * @author ruoyi
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 將前臺傳遞過來的日期格式的字串，自動轉化為Date型別
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 型別轉換
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 設定請求分頁資料
     */
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 響應請求分頁資料
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 響應返回結果
     * 
     * @param rows 影響行數
     * @return 操作結果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
