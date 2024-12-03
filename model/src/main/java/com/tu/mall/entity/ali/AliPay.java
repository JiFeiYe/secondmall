package com.tu.mall.entity.ali;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author JiFeiYe
 * @since 2024/11/26
 */
@Data
public class AliPay {
    private String traceNo; // 交易订单号 - orderInfoId
    private BigDecimal totalAmount; // 交易金额
    private String subject; // 交易名称
    private String alipayTraceNo; // 销售产品码，与支付宝签约的产品码名称。注：目前电脑支付场景下仅支持FAST_INSTANT_TRADE_PAY
}