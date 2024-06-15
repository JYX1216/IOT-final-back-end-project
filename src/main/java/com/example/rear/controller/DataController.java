package com.example.rear.controller;

import com.example.rear.config.returnConfig.ReturnNo;
import com.example.rear.config.returnConfig.ReturnObject;
import com.example.rear.controller.vo.EngineDataVo;
import com.example.rear.service.DataService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class DataController {
    @Autowired
    private DataService dataService;
    @GetMapping("/getData")
    public ReturnObject getData(){
        EngineDataVo engineDataVo = this.dataService.getLatestData();
        return new ReturnObject(ReturnNo.OK,engineDataVo);
    }
    @GetMapping("/getMore")
    public ReturnObject getMore(){
        List<EngineDataVo> engineDataVos = this.dataService.getMoreData();
        return new ReturnObject(ReturnNo.OK,engineDataVos);
    }
    @PostMapping("/sendKafka")
    public ReturnObject sendKafka(){
        this.dataService.sendKafka();
        return new ReturnObject(ReturnNo.OK,ReturnNo.OK.getMessage());
    }
}
