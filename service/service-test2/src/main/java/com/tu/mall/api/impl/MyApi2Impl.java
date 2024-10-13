package com.tu.mall.api.impl;

import com.tu.mall.api.MyApi2;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author JiFeiYe
 * @since 2024/10/11
 */
@DubboService
public class MyApi2Impl implements MyApi2 {

    @Override
    public String testReference(String t) {
        System.out.println(t);
        return "aaa";
    }
}
