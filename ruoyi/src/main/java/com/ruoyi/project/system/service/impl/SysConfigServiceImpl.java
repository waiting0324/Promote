package com.ruoyi.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.system.domain.SysConfig;
import com.ruoyi.project.system.mapper.SysConfigMapper;
import com.ruoyi.project.system.service.ISysConfigService;

/**
 * 引數配置 服務層實現
 * 
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService
{
    @Autowired
    private SysConfigMapper configMapper;

    /**
     * 查詢引數配置資訊
     * 
     * @param configId 引數配置ID
     * @return 引數配置資訊
     */
    @Override
    public SysConfig selectConfigById(Long configId)
    {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * 根據鍵名查詢引數配置資訊
     * 
     * @param configKey 引數key
     * @return 引數鍵值
     */
    @Override
    public String selectConfigByKey(String configKey)
    {
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        return StringUtils.isNotNull(retConfig) ? retConfig.getConfigValue() : "";
    }

    /**
     * 查詢引數配置列表
     * 
     * @param config 引數配置資訊
     * @return 引數配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config)
    {
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增引數配置
     * 
     * @param config 引數配置資訊
     * @return 結果
     */
    @Override
    public int insertConfig(SysConfig config)
    {
        return configMapper.insertConfig(config);
    }

    /**
     * 修改引數配置
     * 
     * @param config 引數配置資訊
     * @return 結果
     */
    @Override
    public int updateConfig(SysConfig config)
    {
        return configMapper.updateConfig(config);
    }

    /**
     * 刪除引數配置資訊
     * 
     * @param configId 引數ID
     * @return 結果
     */
    @Override
    public int deleteConfigById(Long configId)
    {
        return configMapper.deleteConfigById(configId);
    }

    /**
     * 批量刪除引數資訊
     * 
     * @param configIds 需要刪除的引數ID
     * @return 結果
     */
    @Override
    public int deleteConfigByIds(Long[] configIds)
    {
        return configMapper.deleteConfigByIds(configIds);
    }

    /**
     * 校驗引數鍵名是否唯一
     * 
     * @param config 引數配置資訊
     * @return 結果
     */
    @Override
    public String checkConfigKeyUnique(SysConfig config)
    {
        Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
