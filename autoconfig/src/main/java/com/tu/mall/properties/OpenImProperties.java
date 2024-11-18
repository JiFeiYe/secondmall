package com.tu.mall.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author JiFeiYe
 * @since 2024/10/29
 */
@Data
@ConfigurationProperties(prefix = "openim")
public class OpenImProperties {

    private String api;
    private String apiApi;
    private String apiChat;
    private String apiAdmin;
    private String secret;
    private String adminAccount;
    private String adminPwd;
    private String authKey;
    private String requestParamValid;
}
