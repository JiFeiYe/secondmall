package com.tu.mall;

import cn.hutool.json.JSONUtil;
import com.tu.mall.template.OpenImTemplate;
import org.ccs.openim.api.auth.req.GetUserTokenReq;
import org.ccs.openim.api.user.req.UserRegisterReq;
import org.ccs.openim.api.vo.UserInfo;
import org.ccs.openim.base.OpenImResult;
import org.ccs.openim.base.OpenImToken;
import org.ccs.openim.service.OpenImService;
import org.ccs.openim.utils.OpenimUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JiFeiYe
 * @since 2024/10/28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class IMTest {

    @Value("${openim.api}")
    private String api;
    @Value("${openim.secret}")
    private String secret;
    @Value("${openim.adminAccount}")
    private String adminAccount;
    @Value("${openim.adminPwd}")
    private String adminPwd;
    @Value("${openim.authKey}")
    private String authKey;
    @Value("${openim.requestParamValid}")
    private String requestParamValid;

    @Autowired
    private OpenImTemplate openImTemplate;

    @Test
    public void getAdminToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("operationID", "1646445464564");
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("secret", "openIM123");
        requestBody.put("userID", "imAdmin");
        String jsonStr = JSONUtil.toJsonStr(requestBody);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonStr, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://192.168.136.134:10002/auth/get_admin_token",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        System.out.println("Response status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
    }

    @Test
    public void test1() throws IOException {
        OpenImService openImService = openImTemplate.getService();
        OpenimUtils.setOpenimConfig(openImTemplate.getConfig());

        OpenImToken openImToken = new OpenImToken();
        openImToken.setImToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySUQiOiJpbUFkbWluIiwiUGxhdGZvcm1JRCI6MTAsImV4cCI6MTczNzk2NTIwMCwibmJmIjoxNzMwMTg4OTAwLCJpYXQiOjE3MzAxODkyMDB9.bpN8ok3eLkyzd_9TBKl0gtJYO_i99Z2U4jmhW2PMa8c");
//        openImToken.setChatToken();
//        openImToken.setAdminToken();
        openImToken.setOperationId("1");
//        openImToken.setUserId();
        GetUserTokenReq getUserTokenReq = new GetUserTokenReq();
        getUserTokenReq.setPlatformID(1);
        getUserTokenReq.setUserID("8527475173");
        OpenImResult<GetUserTokenReq> userToken = openImService.api().auth().getUserToken(openImToken, getUserTokenReq);
    }

    @Test
    public void test2() {
        OpenImService openImService = openImTemplate.getService();
        OpenimUtils.setOpenimConfig(openImTemplate.getConfig());
        OpenImToken openImToken = new OpenImToken();
        openImToken.setImToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySUQiOiJpbUFkbWluIiwiUGxhdGZvcm1JRCI6MTAsImV4cCI6MTczNzk2NTIwMCwibmJmIjoxNzMwMTg4OTAwLCJpYXQiOjE3MzAxODkyMDB9.bpN8ok3eLkyzd_9TBKl0gtJYO_i99Z2U4jmhW2PMa8c");
        openImToken.setOperationId("1");
        UserRegisterReq userRegisterReq = new UserRegisterReq();
        List<UserInfo> users = new ArrayList<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserID("2");
        userInfo.setNickname("hh");
        users.add(userInfo);
        userRegisterReq.setUsers(users);
        OpenImResult<String> stringOpenImResult = openImService.api().user().userRegister(openImToken, userRegisterReq);
        System.out.println("stringOpenImResult = " + stringOpenImResult);
    }
}
