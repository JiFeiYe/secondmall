package com.tu.mall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * @author JiFeiYe
 * @since 2024/10/14
 */
@Configuration
public class CorsConfig {

//    @Bean
    public CorsWebFilter corsWebFilter() {
        //  在此定义规则：
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");        // 设置请求头
        corsConfiguration.addAllowedMethod("*");        // 设置请求方法
        // 添加多个允许的源
        List<String> allowedOrigins = Arrays.asList(
                "http://127.0.0.1",
                "http://localhost"
        );
        corsConfiguration.setAllowedOrigins(allowedOrigins);        // 设置请求域名
        corsConfiguration.setAllowCredentials(true);    // 允许携带cookie

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(corsConfigurationSource);
    }
}
