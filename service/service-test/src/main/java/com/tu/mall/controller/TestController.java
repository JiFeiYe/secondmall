package com.tu.mall.controller;

import com.tu.mall.api.MyApi;
import com.tu.mall.common.result.Result;
import com.tu.mall.common.utils.MailService;
import com.tu.mall.entity.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping // "/test"
@Api(tags = "TestController测试")
public class TestController {

    @Autowired
    private MailService mailService;

    @Autowired
    private MyApi myApi;

//    @DubboReference
//    private MyApi2 myApi2;

    @ApiOperation(value = "邮件接口", notes = "这里是邮件接口的详细描述")
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

    @ApiOperation(value = "测试接口", notes = "这里是测试接口的详细描述")
    @GetMapping("/user")
    public Result<List<UserInfo>> getUserInfoList() {
//        String string = myApi2.testReference("abc,def");
//        System.out.println(string);

        List<UserInfo> userInfos = myApi.getUserInfoList();
        System.out.println(userInfos.get(0));
        return Result.ok(userInfos);
    }
}
