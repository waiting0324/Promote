package com.promote.project.promote.receiver;

import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * @author 6592 曾培晃
 * @date 2020/5/08
 * @description IBM MQ接收端
 */
@Component
public class ProQueueReceiver extends MessageListenerAdapter {
    private void handleMessage(String msg) throws Exception {
        Thread.sleep(1000L);
        // TODO 接下來就可以對訊息做處理
    }
}