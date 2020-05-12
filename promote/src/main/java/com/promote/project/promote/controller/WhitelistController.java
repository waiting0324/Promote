package com.promote.project.promote.controller;

import com.promote.common.utils.StringUtils;
import com.promote.common.utils.poi.ExcelUtil;
import com.promote.framework.aspectj.lang.annotation.Log;
import com.promote.framework.aspectj.lang.enums.BusinessType;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.framework.web.page.TableDataInfo;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.service.IProWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 白名單Controller
 *
 * @author 6550 劉威廷
 * @date 2020-04-20
 */
@RestController
@RequestMapping("/whitelist")
public class WhitelistController extends BaseController {
    @Autowired
    private IProWhitelistService proWhitelistService;

    /**
     * 查詢白名單列表
     */
    @PreAuthorize("@ss.hasPermi('system:whitelist:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProWhitelist proWhitelist) {
        startPage();
        List<ProWhitelist> list = proWhitelistService.selectProWhitelistList(proWhitelist);
        return getDataTable(list);
    }

    /**
     * 匯出白名單列表
     */
    @PreAuthorize("@ss.hasPermi('system:whitelist:export')")
    @Log(title = "白名單", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ProWhitelist proWhitelist) {
        List<ProWhitelist> list = proWhitelistService.selectProWhitelistList(proWhitelist);
        ExcelUtil<ProWhitelist> util = new ExcelUtil<ProWhitelist>(ProWhitelist.class);
        return util.exportExcel(list, "whitelist");
    }

    /**
     * 獲取白名單詳細資訊
     */
    @PreAuthorize("@ss.hasPermi('system:whitelist:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(proWhitelistService.selectProWhitelistById(id));
    }

    /**
     * 新增白名單
     */
    @PreAuthorize("@ss.hasPermi('system:whitelist:add')")
    @Log(title = "白名單", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProWhitelist proWhitelist) {
        return toAjax(proWhitelistService.insertProWhitelist(proWhitelist));
    }

    /**
     * 修改白名單
     */
    @PreAuthorize("@ss.hasPermi('system:whitelist:edit')")
    @Log(title = "白名單", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProWhitelist proWhitelist) {
        return toAjax(proWhitelistService.updateProWhitelist(proWhitelist));
    }

    /**
     * 刪除白名單
     */
    @PreAuthorize("@ss.hasPermi('system:whitelist:remove')")
    @Log(title = "白名單", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(proWhitelistService.deleteProWhitelistByIds(ids));
    }

    /**
     * 查詢白名單
     *
     * @param request 請求
     * @return 結果
     */
    @PostMapping("/getWhiteListInfo")
    public AjaxResult getWhiteListInfo(@RequestBody Map<String, Object> request) {
        String type = (String)request.get("type");
        String identity = (String)request.get("identity");
        if(StringUtils.isEmpty(type) || StringUtils.isEmpty(identity)){
            return AjaxResult.error("請輸入類型及統編/證號");
        }
        type = "S".equalsIgnoreCase(type) ? "2" : "H".equalsIgnoreCase(type) ? "1" : null;
        if(StringUtils.isEmpty(type)){
            return AjaxResult.error("類型錯誤");
        }
        List<Map<String, Object>> list = proWhitelistService.getByTypeTaxNo(type, identity);
        if(StringUtils.isNotNull(list) && list.size() > 0){
            AjaxResult ajax = AjaxResult.success();
            ajax.put("whiteListInfo",list);
            return ajax;
        }
        return AjaxResult.success("查無資料");
    }
}
