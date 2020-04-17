package com.promote.common.utils.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import com.promote.project.monitor.domain.SysJob;

/**
 * 定時任務處理（禁止併發執行）
 * 
 * @author ruoyi
 *
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
