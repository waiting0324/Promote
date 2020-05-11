package com.promote.project.promote.sender;

import com.alibaba.fastjson.JSON;
import com.promote.project.monitor.domain.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 發送資料到IBM MQ
     *
     * @param className 類名
     * @param entity 實體
     */
//    @PostConstruct
    public void send(String className,Object entity) {
        String json = JSON.toJSONString(entity,true);
        json =  className + ";" + json;
//        try {
//            Object obj = JSON.parseObject(json.substring(json.indexOf(";") + 1),Class.forName(json.substring(0,json.indexOf(";"))) );
//            if(obj instanceof SysOperLog){
//                SysOperLog sysOperLog = (SysOperLog)obj;
//                System.out.println(sysOperLog.getMethod());
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        jmsOperations.convertAndSend("queueName", json);
    }
}