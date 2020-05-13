package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.ProWhitelist;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 白名單 資料層
 * 
 * @author 曾培晃
 */
public interface ProWhitelistMapper
{
    /**
     * 根據帳號,密碼取得旅宿業者資料
     *
     * @param username 帳號
     * @param password 密碼
     * @return 結果
     */
    public ProWhitelist selectProWhitelistByUsernameAndPwd(@Param("username") String username, @Param("password") String password);


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
     * 刪除白名單
     *
     * @param id 白名單ID
     * @return 結果
     */
    public int deleteProWhitelistById(Long id);

    /**
     * 批量刪除白名單
     *
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    public int deleteProWhitelistByIds(Long[] ids);


    /**
     * 查詢白名單
     *
     * @param id 旅宿業者代號
     * @return 白名單
     */
    public ProWhitelist selectProWhitelistByIdType(@Param("id") String id,@Param("type") String type);


    /**
     * 根據統編/身分證字號查找白名單資料
     *
     * @param taxNo 統編/身分證字號
     * @return 白名單
     */
    public List<ProWhitelist> selectProWhitelistByTaxNo(String taxNo);


    /**
     * 據資料類型及統編/身分證字號查找白名單資料
     *
     * @param type 資料類型 (1旅宿業者 2商家)
     * @param taxNo 統編/身分證字號
     * @return 結果
     */
    public List<Map<String,Object>> getByTypeTaxNo(@Param("type") String type, @Param("taxNo") String taxNo);
}