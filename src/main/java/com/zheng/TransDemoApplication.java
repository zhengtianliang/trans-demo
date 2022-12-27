package com.zheng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zheng.mapper")
public class TransDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransDemoApplication.class, args);
    }

}
