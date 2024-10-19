package com.tu.mall.common.utils;

import cn.hutool.core.util.StrUtil;
import org.junit.Test;

/**
 * @author JiFeiYe
 * @since 2024/10/14
 */
public class StrUtilTest {

    @Test
    public void test1() {
        String url = "/front/user/v3/api-docs";

        int i = StrUtil.indexOf(url, "/v2", 0, false);
        url = StrUtil.subSuf(url, i);
        System.out.println(i + " " + url);
    }
}
