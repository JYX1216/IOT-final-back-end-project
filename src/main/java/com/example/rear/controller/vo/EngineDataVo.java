package com.example.rear.controller.vo;

import com.example.rear.mapper.po.EnginData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;

@Getter
@Setter
@NoArgsConstructor
public class EngineDataVo {

    private double temperature;

    private double engineLoad;

    private double rotateSpeed;

    private double fuelEfficiency;
    public EngineDataVo(EnginData enginData){
        DecimalFormat df = new DecimalFormat("#.##"); // 设置格式
        this.engineLoad = Double.parseDouble(df.format(enginData.getEngineLoad())); // 格式化 engineLoad
        this.temperature = enginData.getTemperature();
        this.rotateSpeed = enginData.getRotateSpeed();
        this.fuelEfficiency = enginData.getFuelEfficiency();
    }


}
