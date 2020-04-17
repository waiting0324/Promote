package com.promote.project.tool.gen.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.promote.common.core.text.Convert;
import com.promote.project.tool.gen.domain.GenTableColumn;
import com.promote.project.tool.gen.mapper.GenTableColumnMapper;

/**
 * 業務欄位 服務層實現
 * 
 * @author ruoyi
 */
@Service
public class GenTableColumnServiceImpl implements IGenTableColumnService 
{
	@Autowired
	private GenTableColumnMapper genTableColumnMapper;

	/**
     * 查詢業務欄位列表
     * 
     * @param genTableColumn 業務欄位編號
     * @return 業務欄位集合
     */
	@Override
	public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId)
	{
	    return genTableColumnMapper.selectGenTableColumnListByTableId(tableId);
	}
	
    /**
     * 新增業務欄位
     * 
     * @param genTableColumn 業務欄位資訊
     * @return 結果
     */
	@Override
	public int insertGenTableColumn(GenTableColumn genTableColumn)
	{
	    return genTableColumnMapper.insertGenTableColumn(genTableColumn);
	}
	
	/**
     * 修改業務欄位
     * 
     * @param genTableColumn 業務欄位資訊
     * @return 結果
     */
	@Override
	public int updateGenTableColumn(GenTableColumn genTableColumn)
	{
	    return genTableColumnMapper.updateGenTableColumn(genTableColumn);
	}

	/**
     * 刪除業務欄位物件
     * 
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
	@Override
	public int deleteGenTableColumnByIds(String ids)
	{
		return genTableColumnMapper.deleteGenTableColumnByIds(Convert.toLongArray(ids));
	}
}