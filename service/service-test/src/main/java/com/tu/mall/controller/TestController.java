package com.tu.mall.controller;

import com.tu.mall.api.MyApi;
import com.tu.mall.api.MyApi2;
import com.tu.mall.common.utils.MailService;
import com.tu.mall.common.result.Result;
import com.tu.mall.entity.UserInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JiFeiYe
 * @since 2024/10/11
 */
@RestController
@RequestMapping("/my")
public class TestController {

    @Autowired
    private MailService mailService;

    @Autowired
    private MyApi myApi;

    @DubboReference
    private MyApi2 myApi2;

    @PostMapping("/mail")
    public Result<String> testSendTextMail(String to, String subject, String text) {
        try {
            mailService.sendTextMail(to, subject, text);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }
        return Result.ok("消息发送成功");
    }

    @GetMapping("/user")
    public Result<List<UserInfo>> getUserInfoList() {
        String string = myApi2.testReference("abc,def");
        System.out.println(string);

        List<UserInfo> userInfos = myApi.getUserInfoList();
        System.out.println(userInfos.get(0));
        return Result.ok(userInfos);
    }
}
