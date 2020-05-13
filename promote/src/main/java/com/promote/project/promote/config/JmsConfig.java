package com.promote.project.promote.config;

import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.promote.project.promote.receiver.ProQueueReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.JMSException;

@Configuration
public class JmsConfig {

    @Value("${ibm.mq.host}")
    private String host;

    @Value("${ibm.mq.port}")
    private Integer port;

    @Value("${ibm.mq.queue-manager}")
    private String queueManager;

    @Value("${ibm.mq.channel}")
    private String channel;

    @Value("${ibm.mq.receive-timeout}")
    private long receiveTimeout;

//    @Autowired
//    private ProQueueReceiver proQueueReceiver;
//    @Value("TEST_Q")
//    private String testQueueDestination;
//    @Value("2")
//    private int testQueueConcurrentConsumers;
//    @Value("3")
//    private int testQueueMaxConcurrentConsumers;


    /**
     * 配置連線工廠
     *
     * @return
     */
    @Bean
    public MQQueueConnectionFactory mqQueueConnectionFactory() {
        MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
        mqQueueConnectionFactory.setHostName(host);
        try {
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            //1208表示UTF-8
            mqQueueConnectionFactory.setCCSID(1208);
            mqQueueConnectionFactory.setChannel(channel);
            mqQueueConnectionFactory.setPort(port);
            mqQueueConnectionFactory.setQueueManager(queueManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mqQueueConnectionFactory;
    }

    /**
     * 配置緩存連接工廠
     *
     * @param mqQueueConnectionFactory
     * @return
     */
    @Bean
    @Primary
    public CachingConnectionFactory cachingConnectionFactory(MQQueueConnectionFactory mqQueueConnectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(mqQueueConnectionFactory);
        cachingConnectionFactory.setSessionCacheSize(500);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    /**
     * 配置事務管理器
     *
     * @param cachingConnectionFactory
     * @return
     */
//    @Bean
//    public PlatformTransactionManager jmsTransactionManager(CachingConnectionFactory cachingConnectionFactory) {
//        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
//        jmsTransactionManager.setConnectionFactory(cachingConnectionFactory);
//        return jmsTransactionManager;
//    }

    /**
     * 配置JMS模板
     *
     * @param cachingConnectionFactory
     * @return
     */
    @Bean
    public JmsOperations jmsOperations(CachingConnectionFactory cachingConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setReceiveTimeout(receiveTimeout);
        return jmsTemplate;
    }

//    @Bean
//    public DefaultMessageListenerContainer testQueueContainer(CachingConnectionFactory cachingConnectionFactory) throws JMSException {
//        DefaultMessageListenerContainer testQueueContainer = new DefaultMessageListenerContainer();
//        testQueueContainer.setConnectionFactory(cachingConnectionFactory);
//        testQueueContainer.setDestination(new MQQueue(testQueueDestination));
//        testQueueContainer.setMessageListener(proQueueReceiver);
//        testQueueContainer.setConcurrentConsumers(testQueueConcurrentConsumers);
//        testQueueContainer.setMaxConcurrentConsumers(testQueueMaxConcurrentConsumers);
//        testQueueContainer.setSessionTransacted(true); // 要加這個才會roll back
//        return testQueueContainer;
//    }
}