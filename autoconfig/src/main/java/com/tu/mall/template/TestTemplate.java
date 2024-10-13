package com.tu.mall.template;

import com.tu.mall.properties.TestProperties;

/**
 * sdk模板代码
 *
 * @author JiFeiYe
 * @since 2024/9/26
 */
public class TestTemplate {
    private final TestProperties testProperties;

    public TestTemplate(TestProperties testProperties) {
        this.testProperties = testProperties;
    }

    /**
     * 实际业务代码
     * @param param1 参数1
     * @param param2 参数2
     * @return {@code String}
     */
    public String Business(String param1, String param2) {
        System.out.println(param1);
        System.out.println(param2);
        return testProperties + "_ok";
    }
}
