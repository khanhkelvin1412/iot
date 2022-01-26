package com.hust.khanhkelvin.config;

import com.hust.khanhkelvin.broker.rabbitmq.RabbitMQListenerActionService;
import com.hust.khanhkelvin.broker.rabbitmq.RabbitMQListenerStreamService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    private final RabbitMQConfigurationProperties configurationProperties;

    private final RabbitMQListenerActionService rabbitMQListenerActionService;

    private final RabbitMQListenerStreamService rabbitMQListenerStreamService;

    private final String DEFAULT_EXCHANGE = "office_topic_exchange";

    public RabbitConfig(RabbitMQConfigurationProperties configurationProperties, RabbitMQListenerActionService rabbitMQListenerActionService, RabbitMQListenerStreamService rabbitMQListenerStreamService) {
        this.configurationProperties = configurationProperties;
        this.rabbitMQListenerActionService = rabbitMQListenerActionService;
        this.rabbitMQListenerStreamService = rabbitMQListenerStreamService;
    }

    @Bean(name = "queueAction")
    Queue queueAction() {
        Map<String, Object> args = new HashMap<>();
//        args.put("x-dead-letter-routing-key", "notification_dead_letter_key");
//        args.put("x-max-length", 1000000L);

        return new Queue(configurationProperties.getQueueHustIotAction(), true, false, false, args);
    }

    @Bean(name = "queueStream")
    Queue queueStream() {
        Map<String, Object> args = new HashMap<>();
//        args.put("x-dead-letter-routing-key", "notification_dead_letter_key");
//        args.put("x-max-length", 1000000L);

        return new Queue(configurationProperties.getQueueHustIotStream(), true, false, false, args);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(DEFAULT_EXCHANGE);
    }

    @Bean
    Binding bindingDepartment(@Qualifier("queueStream") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(configurationProperties.getQueueHustIotStream());
    }

    @Bean
    Binding bindingUserDept(@Qualifier("queueAction") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(configurationProperties.getQueueHustIotAction());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // SM_USER
    // RabbitMQListener dung de lang nghe cac data duoc day len server va xu ly
    @Bean
    MessageListenerContainer messageListenerContainerAction(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(queueAction());
        simpleMessageListenerContainer.setMessageListener(rabbitMQListenerActionService);
        return simpleMessageListenerContainer;
    }

    // SM_DEPARTMENT
    @Bean
    MessageListenerContainer messageListenerContainerStream(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(queueStream());
        simpleMessageListenerContainer.setMessageListener(rabbitMQListenerStreamService);
        return simpleMessageListenerContainer;
    }
}
