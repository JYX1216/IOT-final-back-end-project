package com.example.rear.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import static java.lang.Thread.sleep;
@Component
public class KafkaListener {
    @org.springframework.kafka.annotation.KafkaListener(topics = "engineData", groupId = "0")
    public void handleProductMessage(ConsumerRecord<String, String> record) {
        String messageId = record.key(); // 获取消息的 key，即消息的 ID
        String messageContent = record.value(); // 获取消息内
        try {
            System.out.println("Consume Engine Message: key = "+messageId);
            System.out.println("Content:"+messageContent);
            //模拟业务处理
            sleep(1000);
        } catch (Exception e) {
            System.out.println("出现错误：" + e.getMessage());
        }


    }
}
