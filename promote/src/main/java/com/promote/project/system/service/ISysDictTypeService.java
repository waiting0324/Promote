package com.promote.project.system.service;

import java.util.List;
import com.promote.project.system.domain.SysDictType;

/**
 * 字典 業務層
 * 
 * @author ruoyi
 */
public interface ISysDictTypeService
{
    /**
     * 根據條件分頁查詢字典型別
     * 
     * @param dictType 字典型別資訊
     * @return 字典型別集合資訊
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * 根據所有字典型別
     * 
     * @return 字典型別集合資訊
     */
    public List<SysDictType> selectDictTypeAll();

    /**
     * 根據字典型別ID查詢資訊
     * 
     * @param dictId 字典型別ID
     * @return 字典型別
     */
    public SysDictType selectDictTypeById(Long dictId);

    /**
     * 根據字典型別查詢資訊
     * 
     * @param dictType 字典型別
     * @return 字典型別
     */
    public SysDictType selectDictTypeByType(String dictType);

    /**
     * 通過字典ID刪除字典資訊
     * 
     * @param dictId 字典ID
     * @return 結果
     */
    public int deleteDictTypeById(Long dictId);

    /**
     * 批量刪除字典資訊
     * 
     * @param dictIds 需要刪除的字典ID
     * @return 結果
     */
    public int deleteDictTypeByIds(Long[] dictIds);

    /**
     * 新增儲存字典型別資訊
     * 
     * @param dictType 字典型別資訊
     * @return 結果
     */
    public int insertDictType(SysDictType dictType);

    /**
     * 修改儲存字典型別資訊
     * 
     * @param dictType 字典型別資訊
     * @return 結果
     */
    public int updateDictType(SysDictType dictType);

    /**
     * 校驗字典型別稱是否唯一
     * 
     * @param dictType 字典型別
     * @return 結果
     */
    public String checkDictTypeUnique(SysDictType dictType);
}
