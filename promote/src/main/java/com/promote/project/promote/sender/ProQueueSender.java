package com.promote.project.promote.sender;

import com.alibaba.fastjson.JSON;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.project.monitor.domain.SysLogininfor;
import com.promote.project.monitor.domain.SysOperLog;
import com.promote.project.promote.domain.CouponConsume;
import com.promote.project.promote.domain.FundAmount;
import com.promote.project.promote.domain.RemindCoupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 6592 曾培晃
 * @date 2020/5/08
 * @description IBM MQ發送端
 */
@Component
public class ProQueueSender {

    @Autowired
    JmsOperations jmsOperations;

    @Value("${ibm.mq.proCouponConsumeQueueName}")
    private String proCouponConsumeQueueName;

    @Value("${ibm.mq.proFundAmountQueueName}")
    private String proFundAmountQueueName;

    @Value("${ibm.mq.sysLoginInforQueueName}")
    private String sysLoginInforQueueName;

    @Value("${ibm.mq.sysOperLogQueueName}")
    private String sysOperLogQueueName;

    /**
     * 發送資料到IBM MQ
     *
     * @param entity 實體
     */
//    @PostConstruct
    public void send(Object entity) {
        if (StringUtils.isNotNull(entity)) {
            if (entity instanceof CouponConsume) {
                CouponConsume couponConsume = (CouponConsume) entity;
                jmsOperations.convertAndSend(proCouponConsumeQueueName, couponConsume);
            } else if (entity instanceof FundAmount) {
                FundAmount fundAmount = (FundAmount) entity;
                jmsOperations.convertAndSend(proFundAmountQueueName, fundAmount);
            } else if (entity instanceof SysLogininfor) {
                SysLogininfor sysLogininfor = (SysLogininfor) entity;
                jmsOperations.convertAndSend(sysLoginInforQueueName, sysLogininfor);
            } else if (entity instanceof SysOperLog) {
                SysOperLog sysOperLog = (SysOperLog) entity;
                jmsOperations.convertAndSend(sysOperLogQueueName, sysOperLog);
            } else if(entity instanceof RemindCoupon){
                RemindCoupon remindCoupon = (RemindCoupon) entity;
//                jmsOperations.convertAndSend(TOFIX, remindCoupon);
            }
        }
    }
}