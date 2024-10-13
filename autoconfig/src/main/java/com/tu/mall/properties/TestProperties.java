package com.tu.mall.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 引入环境变量
 *
 * @author JiFeiYe
 * @since 2024/9/26
 */
@Data
@ConfigurationProperties(prefix = "mall.test")
public class TestProperties {
    private String accessId;
    private String accessSecret;
    private String url;
}
