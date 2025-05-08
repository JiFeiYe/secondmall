package com.tu.mall.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author JiFeiYe
 * @since 2025/2/20
 */
@Data
@ConfigurationProperties(prefix = "mall.chat")
public class ChatProperties {

    private String apiKey;
}
