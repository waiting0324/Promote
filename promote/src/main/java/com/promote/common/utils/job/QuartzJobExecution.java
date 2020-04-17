package com.promote.common.utils.job;

import org.quartz.JobExecutionContext;
import com.promote.project.monitor.domain.SysJob;

/**
 * 定時任務處理（允許併發執行）
 * 
 * @author ruoyi
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
