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
    private String keyword; // 搜索关键字

    private String order = ""; // 排序规则
    private Integer pageNo = 1;// 当前页面
    private Integer pageSize = 3; // 每页显示几条
}
