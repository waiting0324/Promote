package com.promote.project.promote.service;

import com.promote.project.promote.domain.ProWhitelist;

import java.util.List;
import java.util.Map;

/**
 * 白名單Service介面
 * 
 * @author 6550 劉威廷
 * @date 2020-04-20
 */
public interface IProWhitelistService 
{
    /**
     * 查詢白名單
     * 
     * @param id 白名單ID
     * @return 白名單
     */
    public ProWhitelist selectProWhitelistById(String id);

    /**
     * 查詢白名單列表
     * 
     * @param proWhitelist 白名單
     * @return 白名單集合
     */
    public List<ProWhitelist> selectProWhitelistList(ProWhitelist proWhitelist);

    /**
     * 新增白名單
     * 
     * @param proWhitelist 白名單
     * @return 結果
     */
    public int insertProWhitelist(ProWhitelist proWhitelist);

    /**
     * 修改白名單
     * 
     * @param proWhitelist 白名單
     * @return 結果
     */
    public int updateProWhitelist(ProWhitelist proWhitelist);

    /**
     * 批量刪除白名單
     * 
     * @param ids 需要刪除的白名單ID
     * @return 結果
     */
    public int deleteProWhitelistByIds(Long[] ids);

    /**
     * 刪除白名單資訊
     * 
     * @param id 白名單ID
     * @return 結果
     */
    public int deleteProWhitelistById(Long id);

    /**
     * 將旅店資料匯入白名單
     *
     * @param file 資料
     * @param version Excel版本
     */
//    public void importHostelData(InputStream file, String version);


    /**
     * 將商家資料匯入白名單
     *
     * @param file 資料
     * @param version Excel版本
     */
//    public void importStoreData(InputStream file, String version);

    /**
     * 根據代號及資料類型查找白名單資料
     *
     * @param id 代號
     * @param type 資料類型
     * @return 白名單
     */
    public ProWhitelist selectProWhitelistByIdType(String id,String type);


    /**
     * 根據統編/身分證字號查找白名單資料
     *
     * @param taxNo 統編/身分證字號
     * @return 白名單
     */
    public ProWhitelist selectProWhitelistByTaxNo(String taxNo);

    /**
     *根據資料類型及統編/身分證字號查找白名單資料
     *
     * @param type 資料類型 (1旅宿業者 2商家)
     * @param taxNo 統編/身分證字號
     * @return 結果
     */
    public List<Map<String,Object>> getByTypeTaxNo(String type, String taxNo);
}
