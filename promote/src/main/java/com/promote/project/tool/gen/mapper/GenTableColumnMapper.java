package com.promote.project.tool.gen.mapper;

import java.util.List;
import com.promote.project.tool.gen.domain.GenTableColumn;

/**
 * 業務欄位 資料層
 * 
 * @author ruoyi
 */
public interface GenTableColumnMapper
{
    /**
     * 根據表名稱查詢列資訊
     * 
     * @param tableName 表名稱
     * @return 列資訊
     */
    public List<GenTableColumn> selectDbTableColumnsByName(String tableName);
    
    /**
     * 查詢業務欄位列表
     * 
     * @param tableId 業務欄位編號
     * @return 業務欄位集合
     */
    public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId);

    /**
     * 新增業務欄位
     * 
     * @param genTableColumn 業務欄位資訊
     * @return 結果
     */
    public int insertGenTableColumn(GenTableColumn genTableColumn);

    /**
     * 修改業務欄位
     * 
     * @param genTableColumn 業務欄位資訊
     * @return 結果
     */
    public int updateGenTableColumn(GenTableColumn genTableColumn);

    /**
     * 批量刪除業務欄位
     * 
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    public int deleteGenTableColumnByIds(Long[] ids);
}