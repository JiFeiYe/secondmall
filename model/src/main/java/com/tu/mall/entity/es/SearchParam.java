package com.tu.mall.entity.es;

import lombok.Data;

/**
 * 封装搜索条件
 *
 * @author JiFeiYe
 * @since 2024/10/18
 */
@Data
public class SearchParam {

    private Long category1Id;
    private Long category2Id;
    private Long category3Id;
    // {"运行内存:8GB", "屏幕尺寸:7英寸", ...}
    private String[] attrs;

    private String KeyText;

    // "price:desc"
    private String order = "";      // 排序规则
    private Integer page = 1;       // 当前页面
    private Integer size = 3;       // 每页显示几条
}
