package com.tu.mall;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author JiFeiYe
 * @since 2024/10/23
 */
@SpringBootApplication
@MapperScan({"com.tu.mall.mapper"})
@EnableDubbo
public class ReviewApp {
    public static void main(String[] args) {
        SpringApplication.run(ReviewApp.class, args);
    }
}
