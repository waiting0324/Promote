package com.promote.project.promote.service;

import com.promote.project.promote.domain.ProWhitelist;

import java.util.List;

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
    public ProWhitelist selectProWhitelistById(Long id);

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
}