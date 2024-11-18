package com.tu.mall.template;

import com.tu.mall.properties.OpenImProperties;
import org.ccs.openim.base.OpenimConfig;
import org.ccs.openim.service.OpenImService;

/**
 * @author JiFeiYe
 * @since 2024/10/29
 */
public class OpenImTemplate {
    private final OpenImProperties openImProperties;

    public OpenImTemplate(OpenImProperties openImProperties) {
        this.openImProperties = openImProperties;
    }

    public OpenImService getService() {
        return new OpenImService();
    }

    public OpenimConfig getConfig() {
        OpenimConfig openimConfig = new OpenimConfig();
        openimConfig.setApi(openImProperties.getApi());
        openimConfig.setApiApi(openImProperties.getApiApi());
        openimConfig.setApiChat(openImProperties.getApiChat());
        openimConfig.setApiAdmin(openImProperties.getApiAdmin());
        openimConfig.setSecret(openImProperties.getSecret());
        openimConfig.setAdminAccount(openImProperties.getAdminAccount());
        openimConfig.setAdminPwd(openImProperties.getAdminPwd());
        openimConfig.setAuthKey(openImProperties.getAuthKey());
        openimConfig.setRequestParamValid(openImProperties.getRequestParamValid().equals("true"));
        return openimConfig;
    }
}
