package com.promote.project.promote.service;


import com.promote.project.promote.domain.DailyConsume;
import com.promote.project.system.domain.SysUser;

import javax.mail.MessagingException;
import java.util.Date;


/**
 * 每日消費統計檔 Service介面
 *
 * @author 6592 曾培晃
 */
public interface IDailyConsumeService {

    /**
     * 新增每日消費統計檔
     *
     * @param consumeTime  使用日期
     * @param storeId      商家的user_id
     * @param couponAmount 抵用券金額
     * @return 結果
     */
    public int insertDailyConsume(Date consumeTime, Long storeId, Long couponAmount);

}
