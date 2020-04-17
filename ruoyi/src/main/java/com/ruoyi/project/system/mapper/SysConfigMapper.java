package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.SysConfig;

/**
 * 引數配置 資料層
 * 
 * @author ruoyi
 */
public interface SysConfigMapper
{
    /**
     * 查詢引數配置資訊
     * 
     * @param config 引數配置資訊
     * @return 引數配置資訊
     */
    public SysConfig selectConfig(SysConfig config);

    /**
     * 查詢引數配置列表
     * 
     * @param config 引數配置資訊
     * @return 引數配置集合
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * 根據鍵名查詢引數配置資訊
     * 
     * @param configKey 引數鍵名
     * @return 引數配置資訊
     */
    public SysConfig checkConfigKeyUnique(String configKey);

    /**
     * 新增引數配置
     * 
     * @param config 引數配置資訊
     * @return 結果
     */
    public int insertConfig(SysConfig config);

    /**
     * 修改引數配置
     * 
     * @param config 引數配置資訊
     * @return 結果
     */
    public int updateConfig(SysConfig config);

    /**
     * 刪除引數配置
     * 
     * @param configId 引數ID
     * @return 結果
     */
    public int deleteConfigById(Long configId);

    /**
     * 批量刪除引數資訊
     * 
     * @param configIds 需要刪除的引數ID
     * @return 結果
     */
    public int deleteConfigByIds(Long[] configIds);
}