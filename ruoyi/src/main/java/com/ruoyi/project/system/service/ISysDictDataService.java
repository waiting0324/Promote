package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.SysDictData;

/**
 * 字典 業務層
 * 
 * @author ruoyi
 */
public interface ISysDictDataService
{
    /**
     * 根據條件分頁查詢字典資料
     * 
     * @param dictData 字典資料資訊
     * @return 字典資料集合資訊
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * 根據字典型別查詢字典資料
     * 
     * @param dictType 字典型別
     * @return 字典資料集合資訊
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 根據字典型別和字典鍵值查詢字典資料資訊
     * 
     * @param dictType 字典型別
     * @param dictValue 字典鍵值
     * @return 字典標籤
     */
    public String selectDictLabel(String dictType, String dictValue);

    /**
     * 根據字典資料ID查詢資訊
     * 
     * @param dictCode 字典資料ID
     * @return 字典資料
     */
    public SysDictData selectDictDataById(Long dictCode);

    /**
     * 通過字典ID刪除字典資料資訊
     * 
     * @param dictCode 字典資料ID
     * @return 結果
     */
    public int deleteDictDataById(Long dictCode);

    /**
     * 批量刪除字典資料資訊
     * 
     * @param dictCodes 需要刪除的字典資料ID
     * @return 結果
     */
    public int deleteDictDataByIds(Long[] dictCodes);

    /**
     * 新增儲存字典資料資訊
     * 
     * @param dictData 字典資料資訊
     * @return 結果
     */
    public int insertDictData(SysDictData dictData);

    /**
     * 修改儲存字典資料資訊
     * 
     * @param dictData 字典資料資訊
     * @return 結果
     */
    public int updateDictData(SysDictData dictData);
}
