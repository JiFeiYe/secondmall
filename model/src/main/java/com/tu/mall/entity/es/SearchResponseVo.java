package com.tu.mall.entity.es;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索响应
 *
 * @author JiFeiYe
 * @since 2024/10/18
 */
@ApiModel(description = "搜索响应")
@Data
public class SearchResponseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品列表")
    private List<Good> goodList = new ArrayList<>();

    @ApiModelProperty("总记录数")
    private Long total;         // 总记录数
    @ApiModelProperty("页面大小")
    private Integer size;       // 页面大小
    @ApiModelProperty("当前页面")
    private Integer page;       // 当前页面
    @ApiModelProperty("总共几页")
    private Long totalPages;    // 总共几页
}
