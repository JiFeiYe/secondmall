package com.tu.mall.autoconfig;

import com.tu.mall.properties.TestProperties;
import com.tu.mall.template.TestTemplate;
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
        com.tu.mall.properties.TestProperties.class
})
public class MallAutoConfiguration {
    @Bean
    public TestTemplate testTemplate(TestProperties testProperties) {
        return new TestTemplate(testProperties);
    }
}
