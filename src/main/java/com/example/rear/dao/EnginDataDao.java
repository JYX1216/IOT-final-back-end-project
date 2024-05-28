package com.example.rear.dao;

import com.example.rear.mapper.EnginDataRepository;
import com.example.rear.mapper.po.EnginData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@Repository
public class EnginDataDao {
    private final EnginDataRepository enginDataRepository;
    @Autowired
    public EnginDataDao(EnginDataRepository enginDataRepository) {
        this.enginDataRepository = enginDataRepository;
    }
    public void processData(double engineLoad, double temperature, double rotateSpeed, double fuelEfficiency) {

        EnginData enginData = new EnginData();
        enginData.setEngineLoad(engineLoad);
        enginData.setTemperature(temperature);
        enginData.setRotateSpeed(rotateSpeed);
        enginData.setFuelEfficiency(fuelEfficiency);

        enginDataRepository.save(enginData);
    }
    public Optional<EnginData> findLatestEnginData(Integer pageNumber,Integer pageSize) {
        // 创建分页请求，仅请求第一页的一条数据
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // 按 ID 降序排序，获取第一条记录
        List<EnginData> enginDataList = enginDataRepository.findAllByOrderByIdDesc(pageable);

        if (!enginDataList.isEmpty()) {
            return Optional.of(enginDataList.get(0));
        } else {
            return Optional.empty();
        }
    }

    public List<EnginData> findMoreEnginData(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return enginDataRepository.findAllByOrderByIdDesc(pageable);
    }

}
