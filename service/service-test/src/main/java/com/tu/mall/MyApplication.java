package com.tu.mall;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author JiFeiYe
 * @since 2024/10/11
 */
@SpringBootApplication
@MapperScan({"com.tu.mall.mapper"})
@EnableDubbo
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
