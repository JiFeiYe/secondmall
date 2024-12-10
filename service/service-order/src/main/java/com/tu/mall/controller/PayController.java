package com.tu.mall.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.easysdk.factory.Factory;
import com.tu.mall.config.AliConfig;
import com.tu.mall.entity.ali.AliPay;
import com.tu.mall.service.IOrderInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JiFeiYe
 * @since 2024/11/27
 */
@Api(tags = {"PayController"})
@RestController
@RequestMapping("/alipay")
@Slf4j
public class PayController {

    @Autowired
    private IOrderInfoService orderInfoService;

    @Autowired
    private AliConfig alipayConfig;

    //    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "utf-8";
    private static final String SIGN_TYPE = "RSA2";

    @GetMapping("/easyPay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public void easyPay(AliPay aliPay, HttpServletResponse httpResponse) throws Exception {
        log.info("支付接口调用");

        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGatewayUrl(), alipayConfig.getAppId(),
                alipayConfig.getAppPrivateKey(), FORMAT, CHARSET, alipayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setReturnUrl(alipayConfig.getReturnUrl());
        request.setBizContent("{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","
                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","
                + "\"subject\":\"" + aliPay.getSubject() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        // 直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @GetMapping("/pay")
    public void pay(AliPay aliPay, HttpServletResponse httpResponse) throws AlipayApiException, IOException {
        log.info("支付接口调用");

        // 初始化SDK
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getAlipayConfig());
        // 构造请求参数以调用接口
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        // 设置商户订单号
        model.setOutTradeNo(aliPay.getTraceNo());
        // 设置订单总金额
        model.setTotalAmount(String.valueOf(aliPay.getTotalAmount()));
        // 设置订单标题
        model.setSubject(aliPay.getSubject());
        // 设置产品码
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        request.setBizModel(model);
        // 第三方代调用模式下请设置app_auth_token
        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setReturnUrl(alipayConfig.getReturnUrl());
        request.setBizContent("{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","
                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","
                + "\"subject\":\"" + aliPay.getSubject() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        // 不写POST默认是POST
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
        // 如果需要返回GET请求，请使用
//         AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
        String pageRedirectionData = response.getBody();
//        System.out.println(pageRedirectionData);

        if (response.isSuccess()) {
            httpResponse.setContentType("text/html;charset=" + CHARSET);
            httpResponse.getWriter().write(pageRedirectionData);
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
            String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            System.out.println(diagnosisUrl);
        }
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request, @RequestBody String a) throws Exception {
        log.info("支付宝异步回调");

        if (request.getParameter("trade_status") != null) {
            if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
                System.out.println("=========支付宝异步回调========");

                Map<String, String> params = new HashMap<>();
                Map<String, String[]> requestParams = request.getParameterMap();
                for (String name : requestParams.keySet()) {
                    params.put(name, request.getParameter(name));
                    // System.out.println(name + " = " + request.getParameter(name));
                }
                // 支付宝验签
                if (Factory.Payment.Common().verifyNotify(params)) {
                    // 验签通过
                    System.out.println("交易名称: " + params.get("subject"));
                    System.out.println("交易状态: " + params.get("trade_status"));
                    System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                    System.out.println("商户订单号: " + params.get("out_trade_no")); // order_id
                    System.out.println("交易金额: " + params.get("total_amount"));
                    System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                    System.out.println("买家付款时间: " + params.get("gmt_payment"));
                    System.out.println("买家付款金额: " + params.get("buyer_pay_amount")); // pay_price
                    // 更新订单未已支付
                    String orderId = params.get("out_trade_no");
                    String payPrice = params.get("buyer_pay_amount");
                    orderInfoService.payOrder(orderId, payPrice);
                }
            }
        }
        return "success";
    }
}
