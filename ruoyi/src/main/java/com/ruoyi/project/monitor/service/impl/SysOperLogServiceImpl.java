package com.ruoyi.project.monitor.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.monitor.domain.SysOperLog;
import com.ruoyi.project.monitor.mapper.SysOperLogMapper;
import com.ruoyi.project.monitor.service.ISysOperLogService;

/**
 * 操作日誌 服務層處理
 * 
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * 新增操作日誌
     * 
     * @param operLog 操作日誌物件
     */
    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查詢系統操作日誌集合
     * 
     * @param operLog 操作日誌物件
     * @return 操作日誌集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量刪除系統操作日誌
     * 
     * @param operIds 需要刪除的操作日誌ID
     * @return 結果
     */
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * 查詢操作日誌詳細
     * 
     * @param operId 操作ID
     * @return 操作日誌物件
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日誌
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}
