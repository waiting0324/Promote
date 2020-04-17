package com.promote.project.tool.gen.service;

import java.util.List;
import java.util.Map;
import com.promote.project.tool.gen.domain.GenTable;

/**
 * 業務 服務層
 * 
 * @author ruoyi
 */
public interface IGenTableService
{
    /**
     * 查詢業務列表
     * 
     * @param genTable 業務資訊
     * @return 業務集合
     */
    public List<GenTable> selectGenTableList(GenTable genTable);

    /**
     * 查詢據庫列表
     * 
     * @param genTable 業務資訊
     * @return 資料庫表集合
     */
    public List<GenTable> selectDbTableList(GenTable genTable);

    /**
     * 查詢據庫列表
     * 
     * @param tableNames 表名稱組
     * @return 資料庫表集合
     */
    public List<GenTable> selectDbTableListByNames(String[] tableNames);

    /**
     * 查詢業務資訊
     * 
     * @param id 業務ID
     * @return 業務資訊
     */
    public GenTable selectGenTableById(Long id);

    /**
     * 修改業務
     * 
     * @param genTable 業務資訊
     * @return 結果
     */
    public void updateGenTable(GenTable genTable);

    /**
     * 刪除業務資訊
     * 
     * @param tableIds 需要刪除的表資料ID
     * @return 結果
     */
    public void deleteGenTableByIds(Long[] tableIds);

    /**
     * 匯入表結構
     * 
     * @param tableList 匯入表列表
     */
    public void importGenTable(List<GenTable> tableList);

    /**
     * 預覽程式碼
     * 
     * @param tableId 表編號
     * @return 預覽資料列表
     */
    public Map<String, String> previewCode(Long tableId);

    /**
     * 生成程式碼
     * 
     * @param tableName 表名稱
     * @return 資料
     */
    public byte[] generatorCode(String tableName);

    /**
     * 批量生成程式碼
     * 
     * @param tableNames 表陣列
     * @return 資料
     */
    public byte[] generatorCode(String[] tableNames);

    /**
     * 修改儲存引數校驗
     * 
     * @param genTable 業務資訊
     */
    public void validateEdit(GenTable genTable);
}
