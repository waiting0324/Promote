package com.promote.project.tool.gen.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.promote.common.core.text.Convert;
import com.promote.framework.aspectj.lang.annotation.Log;
import com.promote.framework.aspectj.lang.enums.BusinessType;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.framework.web.page.TableDataInfo;
import com.promote.project.tool.gen.domain.GenTable;
import com.promote.project.tool.gen.domain.GenTableColumn;
import com.promote.project.tool.gen.service.IGenTableColumnService;
import com.promote.project.tool.gen.service.IGenTableService;

/**
 * 程式碼生成 操作處理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/tool/gen")
public class GenController extends BaseController
{
    @Autowired
    private IGenTableService genTableService;

    @Autowired
    private IGenTableColumnService genTableColumnService;

    /**
     * 查詢程式碼生成列表
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping("/list")
    public TableDataInfo genList(GenTable genTable)
    {
        startPage();
        List<GenTable> list = genTableService.selectGenTableList(genTable);
        return getDataTable(list);
    }

    /**
     * 修改程式碼生成業務
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:query')")
    @GetMapping(value = "/{talbleId}")
    public AjaxResult getInfo(@PathVariable Long talbleId)
    {
        GenTable table = genTableService.selectGenTableById(talbleId);
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(talbleId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("info", table);
        map.put("rows", list);
        return AjaxResult.success(map);
    }

    /**
     * 查詢資料庫列表
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping("/db/list")
    public TableDataInfo dataList(GenTable genTable)
    {
        startPage();
        List<GenTable> list = genTableService.selectDbTableList(genTable);
        return getDataTable(list);
    }

    /**
     * 查詢資料表字段列表
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping(value = "/column/{talbleId}")
    public TableDataInfo columnList(Long tableId)
    {
        TableDataInfo dataInfo = new TableDataInfo();
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        dataInfo.setRows(list);
        dataInfo.setTotal(list.size());
        return dataInfo;
    }

    /**
     * 匯入表結構（儲存）
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @Log(title = "程式碼生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importTable")
    public AjaxResult importTableSave(String tables)
    {
        String[] tableNames = Convert.toStrArray(tables);
        // 查詢表資訊
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList);
        return AjaxResult.success();
    }

    /**
     * 修改儲存程式碼生成業務
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:edit')")
    @Log(title = "程式碼生成", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult editSave(@Validated @RequestBody GenTable genTable)
    {
        System.out.println(genTable.getParams().size());
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:remove')")
    @Log(title = "程式碼生成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableIds}")
    public AjaxResult remove(@PathVariable Long[] tableIds)
    {
        genTableService.deleteGenTableByIds(tableIds);
        return AjaxResult.success();
    }

    /**
     * 預覽程式碼
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:preview')")
    @GetMapping("/preview/{tableId}")
    public AjaxResult preview(@PathVariable("tableId") Long tableId) throws IOException
    {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return AjaxResult.success(dataMap);
    }

    /**
     * 生成程式碼
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @Log(title = "程式碼生成", businessType = BusinessType.GENCODE)
    @GetMapping("/genCode/{tableName}")
    public void genCode(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException
    {
        byte[] data = genTableService.generatorCode(tableName);
        genCode(response, data);
    }

    /**
     * 批量生成程式碼
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @Log(title = "程式碼生成", businessType = BusinessType.GENCODE)
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException
    {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.generatorCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip檔案
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException
    {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}