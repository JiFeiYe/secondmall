package com.tu.mall.autoconfig;

import com.tu.mall.properties.OSSProperties;
import com.tu.mall.properties.OpenImProperties;
import com.tu.mall.template.OSSTemplate;
import com.tu.mall.template.OpenImTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 将自定义Template注册成bean
 * 由spring自动配置参数并实例化
 *
 * @author JiFeiYe
 * @since 2024/9/26
 */
@EnableConfigurationProperties({
        com.tu.mall.properties.OSSProperties.class,
        com.tu.mall.properties.OpenImProperties.class
})
public class MallAutoConfiguration {

    @Bean
    public OSSTemplate ossTemplate(OSSProperties ossProperties) {
        return new OSSTemplate(ossProperties);
    }

    @Bean
    public OpenImTemplate openImTemplate(OpenImProperties openImProperties) {
        return new OpenImTemplate(openImProperties);
    }
}
