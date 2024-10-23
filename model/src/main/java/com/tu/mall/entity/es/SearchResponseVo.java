package com.tu.mall.entity.es;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JiFeiYe
 * @since 2024/10/18
 */
@Data
public class SearchResponseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Good> goodList = new ArrayList<>();

    private Long total;         // 总记录数
    private Integer size;       // 每页显示的内容
    private Integer page;       // 当前页面
    private Long totalPages;    // 总共几页
}
