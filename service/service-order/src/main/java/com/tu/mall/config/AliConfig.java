package com.tu.mall.config;

import com.alipay.api.AlipayConfig;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;

@Data
@Component
//读取yml文件中alipay 开头的配置
@ConfigurationProperties(prefix = "alipay")
public class AliConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    private String appId;
    // 商户私钥，您的PKCS8格式RSA2私钥
    private String appPrivateKey;
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String alipayPublicKey;
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    private String notifyUrl;
    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    private String returnUrl;
    // 支付宝网关
    private String gatewayUrl;
    // 支付宝网关
    private static String log_path = "C:\\";

    @PostConstruct
    public AlipayConfig getAlipayConfig() {
        System.out.println(this);
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(this.getGatewayUrl()); // "https://openapi.alipay.com/gateway.do"
        alipayConfig.setAppId(this.getAppId()); // "<-- 请填写您的AppId，例如：2019091767145019 -->"
        alipayConfig.setPrivateKey(this.getAppPrivateKey()); // "<-- 请填写您的应用私钥，例如：MIIEvQIBADANB ... ... -->";
        alipayConfig.setFormat("json");
        alipayConfig.setAlipayPublicKey(this.getAlipayPublicKey()); // "<-- 请填写您的支付宝公钥，例如：MIIBIjANBg... -->";
        alipayConfig.setCharset("UTF-8");
        alipayConfig.setSignType("RSA2");
        return alipayConfig;
    }

    @PostConstruct
    public void init() {
        // 设置参数（全局只需设置一次）
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi-sandbox.dl.alipaydev.com";
        config.signType = "RSA2";
        config.appId = this.appId;
        config.merchantPrivateKey = this.appPrivateKey;
        config.alipayPublicKey = this.alipayPublicKey;
        config.notifyUrl = this.notifyUrl;
        Factory.setOptions(config);
        System.out.println("=======支付宝SDK初始化成功=======");
    }
}