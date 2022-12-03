package com.ding.niuke.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AlphaService {
    public void init(){
        System.out.println("初始化Service");
    }
}
