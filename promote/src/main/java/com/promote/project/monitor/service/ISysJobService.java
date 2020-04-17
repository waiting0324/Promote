package com.promote.project.monitor.service;

import java.util.List;
import org.quartz.SchedulerException;
import com.promote.common.exception.job.TaskException;
import com.promote.project.monitor.domain.SysJob;

/**
 * 定時任務排程資訊資訊 服務層
 * 
 * @author ruoyi
 */
public interface ISysJobService
{
    /**
     * 獲取quartz排程器的計劃任務
     * 
     * @param job 排程資訊
     * @return 排程任務集合
     */
    public List<SysJob> selectJobList(SysJob job);

    /**
     * 通過排程任務ID查詢排程資訊
     * 
     * @param jobId 排程任務ID
     * @return 排程任務物件資訊
     */
    public SysJob selectJobById(Long jobId);

    /**
     * 暫停任務
     * 
     * @param job 排程資訊
     * @return 結果
     */
    public int pauseJob(SysJob job) throws SchedulerException;

    /**
     * 恢復任務
     * 
     * @param job 排程資訊
     * @return 結果
     */
    public int resumeJob(SysJob job) throws SchedulerException;

    /**
     * 刪除任務後，所對應的trigger也將被刪除
     * 
     * @param job 排程資訊
     * @return 結果
     */
    public int deleteJob(SysJob job) throws SchedulerException;

    /**
     * 批量刪除排程資訊
     * 
     * @param jobIds 需要刪除的任務ID
     * @return 結果
     */
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException;

    /**
     * 任務排程狀態修改
     * 
     * @param job 排程資訊
     * @return 結果
     */
    public int changeStatus(SysJob job) throws SchedulerException;

    /**
     * 立即執行任務
     * 
     * @param job 排程資訊
     * @return 結果
     */
    public void run(SysJob job) throws SchedulerException;

    /**
     * 新增任務
     * 
     * @param job 排程資訊
     * @return 結果
     */
    public int insertJob(SysJob job) throws SchedulerException, TaskException;

    /**
     * 更新任務
     * 
     * @param job 排程資訊
     * @return 結果
     */
    public int updateJob(SysJob job) throws SchedulerException, TaskException;

    /**
     * 校驗cron表示式是否有效
     * 
     * @param cronExpression 表示式
     * @return 結果
     */
    public boolean checkCronExpressionIsValid(String cronExpression);
}