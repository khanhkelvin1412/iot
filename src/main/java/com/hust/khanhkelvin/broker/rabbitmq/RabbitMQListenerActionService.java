package com.hust.khanhkelvin.broker.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.khanhkelvin.dto.response.sensor.SensorData;
import com.hust.khanhkelvin.service.SensorDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class RabbitMQListenerActionService implements MessageListener {

    @Autowired
    private SensorDataService sensorDataService;

    @Override
    public void onMessage(Message message) {
        System.out.println("Message Body: " + message.getBody());
        // receive and map data to user
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = new String(message.getBody());
        try {
            SensorData sensorData = objectMapper.readValue(jsonObject, SensorData.class);
            System.out.println(sensorData);
            sensorDataService.saveSensorData(sensorData);
        } catch (JsonProcessingException e) {
            log.error("Convert Data from RabbitMQ Fail!");
        }
        System.out.println(jsonObject);
    }
}
