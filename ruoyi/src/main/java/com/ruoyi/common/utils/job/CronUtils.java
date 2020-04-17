package com.ruoyi.common.utils.job;

import java.text.ParseException;
import java.util.Date;
import org.quartz.CronExpression;

/**
 * cron表示式工具類
 * 
 * @author ruoyi
 *
 */
public class CronUtils
{
    /**
     * 返回一個布林值代表一個給定的Cron表示式的有效性
     *
     * @param cronExpression Cron表示式
     * @return boolean 表示式是否有效
     */
    public static boolean isValid(String cronExpression)
    {
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * 返回一個字串值,表示該訊息無效Cron表示式給出有效性
     *
     * @param cronExpression Cron表示式
     * @return String 無效時返回表示式錯誤描述,如果有效返回null
     */
    public static String getInvalidMessage(String cronExpression)
    {
        try
        {
            new CronExpression(cronExpression);
            return null;
        }
        catch (ParseException pe)
        {
            return pe.getMessage();
        }
    }

    /**
     * 返回下一個執行時間根據給定的Cron表示式
     *
     * @param cronExpression Cron表示式
     * @return Date 下次Cron表示式執行時間
     */
    public static Date getNextExecution(String cronExpression)
    {
        try
        {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        }
        catch (ParseException e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
