package com.hust.khanhkelvin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
@Data
public class RabbitMQConfigurationProperties {
    private String queueHustIotAction;

    private String queueHustIotStream;

    private String queueHustIotHouseSensor;

    private String houseSensorRoutingKey;

    private String houseSensorExchange;
}
