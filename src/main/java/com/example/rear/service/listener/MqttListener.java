package com.example.rear.service.listener;

import com.example.rear.config.MqttClientFactory;
import com.example.rear.dao.EnginDataDao;
import com.example.rear.mapper.EnginDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component
public class MqttListener implements MqttClientFactory.MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqttListener.class);

    @Autowired
    private EnginDataDao enginDataDao;
    private final MqttClientFactory mqttClientFactory;
    private final double min_engine_speed = 0;
    private final double max_engine_speed = 8000;
    private final double min_engine_temp = 0;
    private final double max_engine_temp = 300;
    private final double min_fuel_efficiency = 0;
    private final double max_fuel_efficiency = 20;
    private final double min_value = 0;
    private final double max_value = 100;

    private final Map<String, Double> data_dic = new HashMap<>();
    private final String clientId = "rearListener";
    private final String[] topics = {"sensors/fuel_efficiency", "sensors/temperature", "sensors/rotate_speed"};

    @Autowired
    public MqttListener(MqttClientFactory mqttClientFactory) {
        this.mqttClientFactory = mqttClientFactory;
        this.mqttClientFactory.createClient(clientId, topics, this);
        this.initializeDataDictionary();
    }

    @Override
    public void handleMessage(String topic, String message) {
        logger.info("Received message: {} on topic: {}", message, topic);

        switch (topic) {
            case "sensors/fuel_efficiency":
                handleFuelEfficiencyMessage(message);
                break;
            case "sensors/temperature":
                handleTemperatureMessage(message);
                break;
            case "sensors/rotate_speed":
                handleRotateSpeedMessage(message);
                break;
            default:
                logger.warn("Unknown topic: {}", topic);
                break;
        }
    }

    private void handleFuelEfficiencyMessage(String message) {
        try {
            double fuelEfficiency = Double.parseDouble(message);
            data_dic.put("fuel", fuelEfficiency);
            normalizeAndProcessData();
        } catch (NumberFormatException e) {
            logger.error("Invalid fuel efficiency value: {}", message, e);
        }
    }

    private void handleTemperatureMessage(String message) {
        try {
            double temperature = Double.parseDouble(message);
            data_dic.put("tem", temperature);
            normalizeAndProcessData();
        } catch (NumberFormatException e) {
            logger.error("Invalid temperature value: {}", message, e);
        }
    }

    private void handleRotateSpeedMessage(String message) {
        try {
            double rotateSpeed = Double.parseDouble(message);
            data_dic.put("rotate", rotateSpeed);
            normalizeAndProcessData();
        } catch (NumberFormatException e) {
            logger.error("Invalid rotate speed value: {}", message, e);
        }
    }

    private void normalizeAndProcessData() {
        if (data_dic.values().stream().noneMatch(v -> v == -1)) {
            double engineLoad = normalize(data_dic.get("rotate"), data_dic.get("tem"), data_dic.get("fuel"));
            logger.info("Normalized engine load: {}", engineLoad);

            this.enginDataDao.processData(engineLoad,data_dic.get("tem"), data_dic.get("rotate"), data_dic.get("fuel"));
            resetDataDictionary();
        }
    }

    private void resetDataDictionary() {
        data_dic.replaceAll((k, v) -> -1.0);
    }

    private void initializeDataDictionary() {
        data_dic.put("fuel", -1.0);
        data_dic.put("tem", -1.0);
        data_dic.put("rotate", -1.0);
    }

    private double normalize(double engineSpeed, double engineTemp, double fuelEfficiency) {
        double normalized_engine_speed = (engineSpeed - min_engine_speed) / (max_engine_speed - min_engine_speed) * (max_value - min_value) + min_value;
        double normalized_engine_temp = (engineTemp - min_engine_temp) / (max_engine_temp - min_engine_temp) * (max_value - min_value) + min_value;
        double normalized_fuel_efficiency = (fuelEfficiency - min_fuel_efficiency) / (max_fuel_efficiency - min_fuel_efficiency) * (max_value - min_value) + min_value;
        return (normalized_engine_speed + normalized_engine_temp + normalized_fuel_efficiency) / 3;
    }

    public void startListening() {
        logger.info("Started listening on client: {}", clientId);
    }

    public void stopListening() {
        mqttClientFactory.disconnectClient(clientId);
        logger.info("Stopped listening on client: {}", clientId);
    }
}
