package com.tu.mall.entity.view;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JiFeiYe
 * @since 2024/10/18
 */
@ApiModel(description = "全分类视图")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("category_view")
public class CategoryView implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("一级分类id")
    private String category1Id;
    @ApiModelProperty("一级分类名")
    private String category1Name;
    @ApiModelProperty("二级分类id")
    private String category2Id;
    @ApiModelProperty("二级分类名")
    private String category2Name;
    @ApiModelProperty("三级分类id")
    private String category3Id;
    @ApiModelProperty("三级分类名")
    private String category3Name;
}
