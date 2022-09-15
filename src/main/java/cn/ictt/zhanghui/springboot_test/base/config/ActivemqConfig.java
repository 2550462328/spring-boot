package cn.ictt.zhanghui.springboot_test.base.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;

/**
 * ActiveMQ的配置类
 * 用来配置队列、交换器、路由等高级信息
 */
@Configuration
@ConditionalOnProperty(name = "spring.activemq.enable", havingValue = "true", matchIfMissing = false)
public class ActivemqConfig {
    @Value("${spring.activemq.user}")
    private String usrName;

    @Value("${spring.activemq.password}")
    private String password;

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(usrName, password, brokerUrl);
    }

    @Bean
    public JmsListenerContainerFactory<?> queueListenerContainer(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(false);
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    @Bean
    public JmsListenerContainerFactory<?> topicListenerContainer(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        //设置为发布订阅方式, 默认情况下使用的生产消费者方式
        bean.setPubSubDomain(true);
        bean.setClientId("client_topic_1");
        bean.setSubscriptionDurable(true);
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    @Bean(name = "jmsQueueTemplate")
    public JmsMessagingTemplate jmsQueueTemplate(ActiveMQConnectionFactory connectionFactory) {
        JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate();
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setPubSubDomain(false);
        jmsTemplate.setConnectionFactory(connectionFactory);
//      deliveryMode, priority, timeToLive 的开关，要生效，必须配置为true，默认false
        jmsTemplate.setExplicitQosEnabled(true);
//      发送模式  DeliveryMode.NON_PERSISTENT=1:非持久 ; DeliveryMode.PERSISTENT=2:持久
        jmsTemplate.setDeliveryMode(2);
        jmsTemplate.setPriority(5);
        jmsTemplate.setTimeToLive(30000);
        jmsMessagingTemplate.setJmsTemplate(jmsTemplate);
        return jmsMessagingTemplate;
    }

    @Bean(name = "jmsTopicTemplate")
    public JmsMessagingTemplate jmsTopicTemplate(ActiveMQConnectionFactory connectionFactory) {
        JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate();
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setConnectionFactory(connectionFactory);
//      deliveryMode, priority, timeToLive 的开关，要生效，必须配置为true，默认false
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setDeliveryMode(2);
        jmsTemplate.setPriority(5);
        jmsTemplate.setTimeToLive(30000);
        jmsMessagingTemplate.setJmsTemplate(jmsTemplate);
        return jmsMessagingTemplate;
    }
}
