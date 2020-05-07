package com.promote.project.promote.controller;

import java.util.List;

import com.promote.project.promote.domain.ProNews;
import com.promote.project.promote.service.IProNewsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.promote.framework.aspectj.lang.annotation.Log;
import com.promote.framework.aspectj.lang.enums.BusinessType;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.common.utils.poi.ExcelUtil;
import com.promote.framework.web.page.TableDataInfo;

/**
 * 最新消息/公告Controller
 * 
 * @author promote
 * @date 2020-04-27
 */
@RestController
@RequestMapping("/system/news")
public class NewsController extends BaseController
{
    @Autowired
    private IProNewsService proNewsService;

    /**
     * 查詢最新消息/公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:news:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProNews proNews)
    {
        startPage();
        List<ProNews> list = proNewsService.selectProNewsList(proNews);
        return getDataTable(list);
    }

    /**
     * 匯出最新消息/公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:news:export')")
    @Log(title = "最新消息/公告", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ProNews proNews)
    {
        List<ProNews> list = proNewsService.selectProNewsList(proNews);
        ExcelUtil<ProNews> util = new ExcelUtil<ProNews>(ProNews.class);
        return util.exportExcel(list, "news");
    }

    /**
     * 獲取最新消息/公告詳細資訊
     */
    @PreAuthorize("@ss.hasPermi('system:news:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(proNewsService.selectProNewsById(id));
    }

    /**
     * 新增最新消息/公告
     */
    @PreAuthorize("@ss.hasPermi('system:news:add')")
    @Log(title = "最新消息/公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProNews proNews)
    {
        return toAjax(proNewsService.insertProNews(proNews));
    }

    /**
     * 修改最新消息/公告
     */
    @PreAuthorize("@ss.hasPermi('system:news:edit')")
    @Log(title = "最新消息/公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProNews proNews)
    {
        return toAjax(proNewsService.updateProNews(proNews));
    }

    /**
     * 刪除最新消息/公告
     */
    @PreAuthorize("@ss.hasPermi('system:news:remove')")
    @Log(title = "最新消息/公告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(proNewsService.deleteProNewsByIds(ids));
    }
}
