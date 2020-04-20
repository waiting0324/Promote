package com.promote.project.promote.service.impl;

import java.util.List;
import com.promote.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.promote.project.promote.mapper.ProWhitelistMapper;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.service.IProWhitelistService;

/**
 * 白名單Service業務層處理
 * 
 * @author 6550 劉威廷
 * @date 2020-04-20
 */
@Service
public class ProWhitelistServiceImpl implements IProWhitelistService 
{
    @Autowired
    private ProWhitelistMapper proWhitelistMapper;

    /**
     * 查詢白名單
     * 
     * @param id 白名單ID
     * @return 白名單
     */
    @Override
    public ProWhitelist selectProWhitelistById(Long id)
    {
        return proWhitelistMapper.selectProWhitelistById(id);
    }

    /**
     * 查詢白名單列表
     * 
     * @param proWhitelist 白名單
     * @return 白名單
     */
    @Override
    public List<ProWhitelist> selectProWhitelistList(ProWhitelist proWhitelist)
    {
        return proWhitelistMapper.selectProWhitelistList(proWhitelist);
    }

    /**
     * 新增白名單
     * 
     * @param proWhitelist 白名單
     * @return 結果
     */
    @Override
    public int insertProWhitelist(ProWhitelist proWhitelist)
    {
        proWhitelist.setCreateTime(DateUtils.getNowDate());
        return proWhitelistMapper.insertProWhitelist(proWhitelist);
    }

    /**
     * 修改白名單
     * 
     * @param proWhitelist 白名單
     * @return 結果
     */
    @Override
    public int updateProWhitelist(ProWhitelist proWhitelist)
    {
        proWhitelist.setUpdateTime(DateUtils.getNowDate());
        return proWhitelistMapper.updateProWhitelist(proWhitelist);
    }

    /**
     * 批量刪除白名單
     * 
     * @param ids 需要刪除的白名單ID
     * @return 結果
     */
    @Override
    public int deleteProWhitelistByIds(Long[] ids)
    {
        return proWhitelistMapper.deleteProWhitelistByIds(ids);
    }

    /**
     * 刪除白名單資訊
     * 
     * @param id 白名單ID
     * @return 結果
     */
    @Override
    public int deleteProWhitelistById(Long id)
    {
        return proWhitelistMapper.deleteProWhitelistById(id);
    }
}
