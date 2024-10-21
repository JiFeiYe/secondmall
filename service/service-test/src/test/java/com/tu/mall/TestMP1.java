package com.tu.mall;

import cn.hutool.json.JSONUtil;
import com.tu.mall.entity.UserInfo;
import com.tu.mall.mapper.UserInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JiFeiYe
 * @since 2024/10/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMP1 {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    public void myTest() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("测试名字");
        userInfoMapper.insertOrUpdate(userInfo);
        String jsonStr = JSONUtil.toJsonStr(userInfo);
        System.out.println("JSONUtil.formatJsonStr(jsonStr) = \n" + JSONUtil.formatJsonStr(jsonStr));
    }

    @Test
    public void myTest2() {
        List<UserInfo> userInfoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setName("测试名字" + i);
            userInfoList.add(userInfo);
        }
        userInfoMapper.insert(userInfoList);
        for (UserInfo userInfo : userInfoList) {
            String jsonStr = JSONUtil.toJsonStr(userInfo);
            System.out.println("JSONUtil.formatJsonStr(jsonStr) = \n" + JSONUtil.formatJsonStr(jsonStr));
        }
    }
}
