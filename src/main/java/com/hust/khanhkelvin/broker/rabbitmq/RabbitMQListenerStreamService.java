package com.hust.khanhkelvin.broker.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.khanhkelvin.service.SensorService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RabbitMQListenerStreamService implements MessageListener {

//    @Autowired
//    private SensorService sensorService;

    @Override
    public void onMessage(Message message) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = new String(message.getBody());
        System.out.println(jsonObject);
    }
}
