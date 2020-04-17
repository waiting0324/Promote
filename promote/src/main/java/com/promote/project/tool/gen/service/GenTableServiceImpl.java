package com.promote.project.tool.gen.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.promote.common.constant.Constants;
import com.promote.common.constant.GenConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.project.tool.gen.domain.GenTable;
import com.promote.project.tool.gen.domain.GenTableColumn;
import com.promote.project.tool.gen.mapper.GenTableColumnMapper;
import com.promote.project.tool.gen.mapper.GenTableMapper;
import com.promote.project.tool.gen.util.GenUtils;
import com.promote.project.tool.gen.util.VelocityInitializer;
import com.promote.project.tool.gen.util.VelocityUtils;

/**
 * 業務 服務層實現
 * 
 * @author ruoyi
 */
@Service
public class GenTableServiceImpl implements IGenTableService
{
    private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);

    @Autowired
    private GenTableMapper genTableMapper;

    @Autowired
    private GenTableColumnMapper genTableColumnMapper;

    /**
     * 查詢業務資訊
     * 
     * @param id 業務ID
     * @return 業務資訊
     */
    @Override
    public GenTable selectGenTableById(Long id)
    {
        GenTable genTable = genTableMapper.selectGenTableById(id);
        setTableFromOptions(genTable);
        return genTable;
    }

    /**
     * 查詢業務列表
     * 
     * @param genTable 業務資訊
     * @return 業務集合
     */
    @Override
    public List<GenTable> selectGenTableList(GenTable genTable)
    {
        return genTableMapper.selectGenTableList(genTable);
    }

    /**
     * 查詢據庫列表
     * 
     * @param genTable 業務資訊
     * @return 資料庫表集合
     */
    public List<GenTable> selectDbTableList(GenTable genTable)
    {
        return genTableMapper.selectDbTableList(genTable);
    }

    /**
     * 查詢據庫列表
     * 
     * @param tableNames 表名稱組
     * @return 資料庫表集合
     */
    public List<GenTable> selectDbTableListByNames(String[] tableNames)
    {
        return genTableMapper.selectDbTableListByNames(tableNames);
    }

    /**
     * 修改業務
     * 
     * @param genTable 業務資訊
     * @return 結果
     */
    @Override
    @Transactional
    public void updateGenTable(GenTable genTable)
    {
        String options = JSON.toJSONString(genTable.getParams());
        genTable.setOptions(options);
        int row = genTableMapper.updateGenTable(genTable);
        if (row > 0)
        {
            for (GenTableColumn cenTableColumn : genTable.getColumns())
            {
                genTableColumnMapper.updateGenTableColumn(cenTableColumn);
            }
        }
    }

    /**
     * 刪除業務物件
     * 
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    @Override
    @Transactional
    public void deleteGenTableByIds(Long[] tableIds)
    {
        genTableMapper.deleteGenTableByIds(tableIds);
        genTableColumnMapper.deleteGenTableColumnByIds(tableIds);
    }

    /**
     * 匯入表結構
     * 
     * @param tableList 匯入表列表
     */
    @Override
    @Transactional
    public void importGenTable(List<GenTable> tableList)
    {
        String operName = SecurityUtils.getUsername();
        for (GenTable table : tableList)
        {
            try
            {
                String tableName = table.getTableName();
                GenUtils.initTable(table, operName);
                int row = genTableMapper.insertGenTable(table);
                if (row > 0)
                {
                    // 儲存列資訊
                    List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
                    for (GenTableColumn column : genTableColumns)
                    {
                        GenUtils.initColumnField(column, table);
                        genTableColumnMapper.insertGenTableColumn(column);
                    }
                }
            }
            catch (Exception e)
            {
                log.error("表名 " + table.getTableName() + " 匯入失敗：", e);
            }
        }
    }

    /**
     * 預覽程式碼
     * 
     * @param tableId 表編號
     * @return 預覽資料列表
     */
    public Map<String, String> previewCode(Long tableId)
    {
        Map<String, String> dataMap = new LinkedHashMap<>();
        // 查詢表資訊
        GenTable table = genTableMapper.selectGenTableById(tableId);
        // 查詢列資訊
        List<GenTableColumn> columns = table.getColumns();
        setPkColumn(table, columns);
        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 獲取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates)
        {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }

    /**
     * 生成程式碼
     * 
     * @param tableName 表名稱
     * @return 資料
     */
    @Override
    public byte[] generatorCode(String tableName)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(tableName, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 批量生成程式碼
     * 
     * @param tableNames 表陣列
     * @return 資料
     */
    @Override
    public byte[] generatorCode(String[] tableNames)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames)
        {
            generatorCode(tableName, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查詢表資訊並生成程式碼
     */
    private void generatorCode(String tableName, ZipOutputStream zip)
    {
        // 查詢表資訊
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // 查詢列資訊
        List<GenTableColumn> columns = table.getColumns();
        setPkColumn(table, columns);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 獲取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates)
        {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try
            {
                // 新增到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, Constants.UTF8);
                IOUtils.closeQuietly(sw);
				zip.flush();
                zip.closeEntry();
            }
            catch (IOException e)
            {
                log.error("渲染模板失敗，表名：" + table.getTableName(), e);
            }
        }
    }

    /**
     * 修改儲存引數校驗
     * 
     * @param genTable 業務資訊
     */
    public void validateEdit(GenTable genTable)
    {
        if (GenConstants.TPL_TREE.equals(genTable.getTplCategory()))
        {
            String options = JSON.toJSONString(genTable.getParams());
            JSONObject paramsObj = JSONObject.parseObject(options);
            if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_CODE)))
            {
                throw new CustomException("樹編碼欄位不能為空");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_PARENT_CODE)))
            {
                throw new CustomException("樹父編碼欄位不能為空");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_NAME)))
            {
                throw new CustomException("樹名稱欄位不能為空");
            }
        }
    }

    /**
     * 設定主鍵列資訊
     * 
     * @param genTable 業務表資訊
     * @param columns 業務欄位列表
     */
    public void setPkColumn(GenTable table, List<GenTableColumn> columns)
    {
        for (GenTableColumn column : columns)
        {
            if (column.isPk())
            {
                table.setPkColumn(column);
                break;
            }
        }
        if (StringUtils.isNull(table.getPkColumn()))
        {
            table.setPkColumn(columns.get(0));
        }
    }

    /**
     * 設定程式碼生成其他選項值
     * 
     * @param genTable 設定後的生成物件
     */
    public void setTableFromOptions(GenTable genTable)
    {
        JSONObject paramsObj = JSONObject.parseObject(genTable.getOptions());
        if (StringUtils.isNotNull(paramsObj))
        {
            String treeCode = paramsObj.getString(GenConstants.TREE_CODE);
            String treeParentCode = paramsObj.getString(GenConstants.TREE_PARENT_CODE);
            String treeName = paramsObj.getString(GenConstants.TREE_NAME);
            genTable.setTreeCode(treeCode);
            genTable.setTreeParentCode(treeParentCode);
            genTable.setTreeName(treeName);
        }
    }
}