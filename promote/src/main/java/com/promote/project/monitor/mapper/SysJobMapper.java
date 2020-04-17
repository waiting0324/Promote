package com.promote.project.monitor.mapper;

import java.util.List;
import com.promote.project.monitor.domain.SysJob;

/**
 * 排程任務資訊 資料層
 * 
 * @author ruoyi
 */
public interface SysJobMapper
{
    /**
     * 查詢排程任務日誌集合
     * 
     * @param job 排程資訊
     * @return 操作日誌集合
     */
    public List<SysJob> selectJobList(SysJob job);

    /**
     * 查詢所有排程任務
     * 
     * @return 排程任務列表
     */
    public List<SysJob> selectJobAll();

    /**
     * 通過排程ID查詢排程任務資訊
     * 
     * @param jobId 排程ID
     * @return 角色物件資訊
     */
    public SysJob selectJobById(Long jobId);

    /**
     * 通過排程ID刪除排程任務資訊
     * 
     * @param jobId 排程ID
     * @return 結果
     */
    public int deleteJobById(Long jobId);

    /**
     * 批量刪除排程任務資訊
     * 
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    public int deleteJobByIds(Long[] ids);

    /**
     * 修改排程任務資訊
     * 
     * @param job 排程任務資訊
     * @return 結果
     */
    public int updateJob(SysJob job);

    /**
     * 新增排程任務資訊
     * 
     * @param job 排程任務資訊
     * @return 結果
     */
    public int insertJob(SysJob job);
}
