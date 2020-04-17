package com.ruoyi.project.tool.gen.mapper;

import java.util.List;
import com.ruoyi.project.tool.gen.domain.GenTable;

/**
 * 業務 資料層
 * 
 * @author ruoyi
 */
public interface GenTableMapper
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
     * 查詢表ID業務資訊
     * 
     * @param id 業務ID
     * @return 業務資訊
     */
    public GenTable selectGenTableById(Long id);

    /**
     * 查詢表名稱業務資訊
     * 
     * @param tableName 表名稱
     * @return 業務資訊
     */
    public GenTable selectGenTableByName(String tableName);

    /**
     * 新增業務
     * 
     * @param genTable 業務資訊
     * @return 結果
     */
    public int insertGenTable(GenTable genTable);

    /**
     * 修改業務
     * 
     * @param genTable 業務資訊
     * @return 結果
     */
    public int updateGenTable(GenTable genTable);

    /**
     * 批量刪除業務
     * 
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    public int deleteGenTableByIds(Long[] ids);
}