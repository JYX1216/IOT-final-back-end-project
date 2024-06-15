package com.example.rear.service;

import com.example.rear.config.KafkaUtil;
import com.example.rear.config.snowflake.SnowFlakeIdWorker;
import com.example.rear.controller.vo.EngineDataVo;
import com.example.rear.dao.EnginDataDao;
import com.example.rear.mapper.po.EnginData;
import com.example.rear.service.listener.MqttListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DataService {
    @Autowired
    private MqttListener mqttListener;
    @Autowired
    private EnginDataDao enginDataDao;
    @Autowired
    private KafkaUtil kafkaUtil;
    @Autowired
    private SnowFlakeIdWorker snowFlakeIdWorker;
    @Transactional
    public EngineDataVo getLatestData(){
        Optional<EnginData> engineData = this.enginDataDao.findLatestEnginData(0,1);
        return new EngineDataVo(engineData.orElseThrow(() -> new NoSuchElementException("No latest engine data found")));
    }
    @Transactional
    public List<EngineDataVo> getMoreData() {
        List<EnginData> engineDataList = this.enginDataDao.findMoreEnginData(0, 50);
        return engineDataList.stream()
                .map(EngineDataVo::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public void sendKafka(){
        Optional<EnginData> engineData = this.enginDataDao.findLatestEnginData(0,1);
        String key = String.valueOf(snowFlakeIdWorker.nextId());
        this.kafkaUtil.sendMessages(engineData.toString(),"engineData",key);
    }

}
