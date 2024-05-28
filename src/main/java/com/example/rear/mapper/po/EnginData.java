package com.example.rear.mapper.po;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EnginData {
    @Id
    private int id;
    @Column(name = "temperature")
    private double temperature;

    @Column(name = "engine_load")
    private double engineLoad;



    @Column(name = "rotate_speed")
    private double rotateSpeed;

    @Column(name = "fuel_efficiency")
    private double fuelEfficiency;
}
