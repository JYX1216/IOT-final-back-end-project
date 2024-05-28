package com.example.rear.service.bo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnginDataBo {

    private double temperature;


    private double engineLoad;

    private double rotateSpeed;

    private double fuelEfficiency;
    public EnginDataBo(Double engineLoad,Double rotateSpeed,Double fuelEfficiency,Double temperature){
        this.engineLoad = engineLoad;
        this.temperature = temperature;
        this.fuelEfficiency = fuelEfficiency;
        this.rotateSpeed = rotateSpeed;
    }}