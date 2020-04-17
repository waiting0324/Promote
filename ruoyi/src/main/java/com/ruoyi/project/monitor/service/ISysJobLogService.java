package com.ruoyi.project.monitor.service;

import java.util.List;
import com.ruoyi.project.monitor.domain.SysJobLog;

/**
 * 定時任務排程日誌資訊資訊 服務層
 * 
 * @author ruoyi
 */
public interface ISysJobLogService
{
    /**
     * 獲取quartz排程器日誌的計劃任務
     * 
     * @param jobLog 排程日誌資訊
     * @return 排程任務日誌集合
     */
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog);

    /**
     * 通過排程任務日誌ID查詢排程資訊
     * 
     * @param jobLogId 排程任務日誌ID
     * @return 排程任務日誌物件資訊
     */
    public SysJobLog selectJobLogById(Long jobLogId);

    /**
     * 新增任務日誌
     * 
     * @param jobLog 排程日誌資訊
     */
    public void addJobLog(SysJobLog jobLog);

    /**
     * 批量刪除排程日誌資訊
     * 
     * @param logIds 需要刪除的日誌ID
     * @return 結果
     */
    public int deleteJobLogByIds(Long[] logIds);

    /**
     * 刪除任務日誌
     * 
     * @param jobId 排程日誌ID
     * @return 結果
     */
    public int deleteJobLogById(Long jobId);

    /**
     * 清空任務日誌
     */
    public void cleanJobLog();
}
