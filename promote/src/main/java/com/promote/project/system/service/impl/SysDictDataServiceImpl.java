package com.promote.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.promote.project.system.domain.SysDictData;
import com.promote.project.system.mapper.SysDictDataMapper;
import com.promote.project.system.service.ISysDictDataService;

/**
 * 字典 業務層處理
 * 
 * @author ruoyi
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService
{
    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 根據條件分頁查詢字典資料
     * 
     * @param dictData 字典資料資訊
     * @return 字典資料集合資訊
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData)
    {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 根據字典型別查詢字典資料
     * 
     * @param dictType 字典型別
     * @return 字典資料集合資訊
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType)
    {
        return dictDataMapper.selectDictDataByType(dictType);
    }

    /**
     * 根據字典型別和字典鍵值查詢字典資料資訊
     * 
     * @param dictType 字典型別
     * @param dictValue 字典鍵值
     * @return 字典標籤
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue)
    {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根據字典資料ID查詢資訊
     * 
     * @param dictCode 字典資料ID
     * @return 字典資料
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode)
    {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 通過字典ID刪除字典資料資訊
     * 
     * @param dictCode 字典資料ID
     * @return 結果
     */
    @Override
    public int deleteDictDataById(Long dictCode)
    {
        return dictDataMapper.deleteDictDataById(dictCode);
    }

    /**
     * 批量刪除字典資料資訊
     * 
     * @param dictCodes 需要刪除的字典資料ID
     * @return 結果
     */
    public int deleteDictDataByIds(Long[] dictCodes)
    {
        return dictDataMapper.deleteDictDataByIds(dictCodes);
    }

    /**
     * 新增儲存字典資料資訊
     * 
     * @param dictData 字典資料資訊
     * @return 結果
     */
    @Override
    public int insertDictData(SysDictData dictData)
    {
        return dictDataMapper.insertDictData(dictData);
    }

    /**
     * 修改儲存字典資料資訊
     * 
     * @param dictData 字典資料資訊
     * @return 結果
     */
    @Override
    public int updateDictData(SysDictData dictData)
    {
        return dictDataMapper.updateDictData(dictData);
    }
}
