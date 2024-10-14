package com.tu.mall.gateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tu.mall.common.result.Result;
import com.tu.mall.common.result.ResultCodeEnum;
import com.tu.mall.common.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 拦截请求获取jwt并解析
 *
 * @author JiFeiYe
 * @since 2024/10/11
 */
@Component
public class AuthGlobalFilter implements GlobalFilter {

    @Value("${freeUrls.url}")
    private String freeUrls;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        // 获取url（作用待定->api拦截）
        String url = request.getURI().getPath();
        System.out.println("请求路径" + url);

        // 匹配免权限路径
        for (String freeUrl : StrUtil.split(freeUrls, ",")) {
            if (StrUtil.indexOf(url, freeUrl, 0, false) != -1) { // 包含
                System.out.println(url + "是免权限路径，通过。");
                return chain.filter(exchange);
            }
        }

        // 获取jwt-token并解析出userId
        String token = getToken(request);
        String userId = "";
        if (StrUtil.isNotEmpty(token)) {
            userId = JWTUtil.getUserId(token);
        }
        if (StrUtil.isNotEmpty(userId)) { // 如果jwt校验通过
            System.out.println("jwt校验通过，userId：" + userId);
            // 将userId设置到请求头
            request.mutate().header("userId", userId).build();
            // 将request变回exchange对象并传递到下一过滤器链
            return chain.filter(exchange.mutate().request(request).build());
        } else {
            System.out.println("jwt校验失败");
            ServerHttpResponse response = exchange.getResponse();
            return out(response, ResultCodeEnum.LOGIN_AUTH);
        }
        // 默认返回
        // return chain.filter(exchange);
    }

    /**
     * 获取jwt-token
     *
     * @param request request
     * @return {@code String}
     */
    private String getToken(ServerHttpRequest request) {
        String token = "";
        List<String> list = request.getHeaders().get("token");
        if (CollectionUtil.isNotEmpty(list)) {
            token = list.get(0);
        }
        System.out.println("获取的token是：" + token);
        return token;
    }

    /**
     * 自定义返回内容
     *
     * @param response       response
     * @param resultCodeEnum resultCodeEnum
     * @return {@code Mono<Void>}
     */
    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        // 设置响应头，标识为json格式
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        // 设置返回值
        Result<String> result = Result.exception(resultCodeEnum);
        // 转换成Json格式（hutool）
        String resultJson = JSONUtil.toJsonStr(result);
        // 写入响应体
        DataBuffer wrap = response.bufferFactory().wrap(resultJson.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(wrap));
    }
}
