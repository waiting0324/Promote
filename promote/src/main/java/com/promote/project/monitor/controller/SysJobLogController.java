package com.promote.project.monitor.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.promote.framework.aspectj.lang.annotation.Log;
import com.promote.framework.aspectj.lang.enums.BusinessType;
import com.promote.project.monitor.domain.SysJobLog;
import com.promote.project.monitor.service.ISysJobLogService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.common.utils.poi.ExcelUtil;
import com.promote.framework.web.page.TableDataInfo;

/**
 * 排程日誌操作處理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/jobLog")
public class SysJobLogController extends BaseController
{
    @Autowired
    private ISysJobLogService jobLogService;

    /**
     * 查詢定時任務排程日誌列表
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysJobLog sysJobLog)
    {
        startPage();
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        return getDataTable(list);
    }

    /**
     * 匯出定時任務排程日誌列表
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:export')")
    @Log(title = "任務排程日誌", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysJobLog sysJobLog)
    {
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        ExcelUtil<SysJobLog> util = new ExcelUtil<SysJobLog>(SysJobLog.class);
        return util.exportExcel(list, "排程日誌");
    }
    
    /**
     * 根據排程編號獲取詳細資訊
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:query')")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable Long jobLogId)
    {
        return AjaxResult.success(jobLogService.selectJobLogById(jobLogId));
    }


    /**
     * 刪除定時任務排程日誌
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "定時任務排程日誌", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobLogIds}")
    public AjaxResult remove(@PathVariable Long[] jobLogIds)
    {
        return toAjax(jobLogService.deleteJobLogByIds(jobLogIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "排程日誌", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        jobLogService.cleanJobLog();
        return AjaxResult.success();
    }
}
