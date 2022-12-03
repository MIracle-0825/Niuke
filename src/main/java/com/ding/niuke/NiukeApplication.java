package com.ding.niuke;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@MapperScan(value = "com.ding.niuke.mapper")
public class NiukeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NiukeApplication.class, args);
    }

}
