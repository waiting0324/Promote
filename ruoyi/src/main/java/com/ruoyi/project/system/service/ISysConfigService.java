package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.SysConfig;

/**
 * 引數配置 服務層
 * 
 * @author ruoyi
 */
public interface ISysConfigService
{
    /**
     * 查詢引數配置資訊
     * 
     * @param configId 引數配置ID
     * @return 引數配置資訊
     */
    public SysConfig selectConfigById(Long configId);

    /**
     * 根據鍵名查詢引數配置資訊
     * 
     * @param configKey 引數鍵名
     * @return 引數鍵值
     */
    public String selectConfigByKey(String configKey);

    /**
     * 查詢引數配置列表
     * 
     * @param config 引數配置資訊
     * @return 引數配置集合
     */
    public List<SysConfig> selectConfigList(SysConfig config);

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
     * 刪除引數配置資訊
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

    /**
     * 校驗引數鍵名是否唯一
     * 
     * @param config 引數資訊
     * @return 結果
     */
    public String checkConfigKeyUnique(SysConfig config);
}
