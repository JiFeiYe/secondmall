package com.tu.mall.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author JiFeiYe
 * @since 2024/10/14
 */
@Configuration
@EnableSwagger2WebMvc // springboot2版本使用mvc，3版本使用flux
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tu.mall.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * api相关的信息
     */
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("易电脑")
                .description("二手电脑交易平台")
                .contact(new Contact("liutiaoren", null, null))
                .version("1.0")
                .build();
    }
}
