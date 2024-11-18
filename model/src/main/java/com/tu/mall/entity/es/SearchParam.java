package com.tu.mall.entity.es;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 搜索条件
 *
 * @author JiFeiYe
 * @since 2024/10/18
 */
@ApiModel(description = "搜索条件")
@Data
public class SearchParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("一级分类id")
    private Long category1Id;
    @ApiModelProperty("二级分类id")
    private Long category2Id;
    @ApiModelProperty("三级分类id")
    private Long category3Id;
    // {"运行内存:8GB", "屏幕尺寸:7英寸", ...}
    @ApiModelProperty("属性:属性值数组")
    private String[] attrs;

    @ApiModelProperty("搜索关键句")
    private String KeyText;

    // {"price:desc", "createTime:desc", ...}
    @ApiModelProperty("排序规则")
    private String[] orders;      // 排序规则
    @ApiModelProperty("当前页面")
    private Integer page = 1;       // 当前页面
    @ApiModelProperty("页面大小")
    private Integer size = 3;       // 每页显示几条
}
