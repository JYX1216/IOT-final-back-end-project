package com.example.rear.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Component
public class MqttClientFactory {

    private static final String BROKER_URL = "tcp://127.0.0.1:1883"; // MQTT代理地址
    private Map<String, MqttClient> clients = new HashMap<>();

    public interface MessageHandler {
        void handleMessage(String topic, String message);
    }

    public MqttClient createClient(String clientId, String[] topics, MessageHandler messageHandler) {
        try {
            MqttClient mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true); // 设置为true表示客户端与代理断开连接时会清除会话状态

            System.out.println("Connecting to broker: " + BROKER_URL);
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            // 订阅多个主题
            for (String topic : topics) {
                mqttClient.subscribe(topic, (topic1, msg) -> {
                    String message = new String(msg.getPayload());
                    System.out.println("Message received on client " + clientId + " for topic " + topic1 + ": " + message);
                    messageHandler.handleMessage(topic1, message);
                });
            }

            clients.put(clientId, mqttClient);
            return mqttClient;

        } catch (MqttException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MqttClient getClient(String clientId) {
        return clients.get(clientId);
    }

    public void disconnectClient(String clientId) {
        MqttClient client = clients.get(clientId);
        if (client != null) {
            try {
                client.disconnect();
                System.out.println("Client " + clientId + " disconnected.");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Client with ID " + clientId + " not found.");
        }
    }

    public void disconnectAll() {
        for (Map.Entry<String, MqttClient> entry : clients.entrySet()) {
            try {
                entry.getValue().disconnect();
                System.out.println("Client " + entry.getKey() + " disconnected.");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        clients.clear();
    }

}
