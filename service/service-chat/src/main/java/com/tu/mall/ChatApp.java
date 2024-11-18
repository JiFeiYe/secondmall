package com.tu.mall;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author JiFeiYe
 * @since 2024/10/30
 */
@SpringBootApplication
@MapperScan({"com.tu.mall.mapper"})
@EnableDubbo
public class ChatApp {
    public static void main(String[] args) {
        SpringApplication.run(ChatApp.class, args);
    }
}
