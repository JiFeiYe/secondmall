package com.tu.mall.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author JiFeiYe
 * @since 2024/10/17
 */
@Data
@ConfigurationProperties(prefix = "mall.oss")
public class OSSProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String url;
}
