package com.tu.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品（基本/销售）属性表
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
@ApiModel(description = "商品（基本/销售）属性表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("attribute")
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花id
     */
    @ApiModelProperty("雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 属性名称
     */
    @ApiModelProperty("属性名称")
    private String name;

    /**
     * 对应二级分类id
     */
    @ApiModelProperty("对应二级分类id")
    private String categoryId;

    /**
     * 二级分类名称
     */
    @ApiModelProperty("二级分类名称")
    @TableField(exist = false)
    private String categoryName;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("")
    private Integer isDeleted;


}
