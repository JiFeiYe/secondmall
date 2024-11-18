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
 * 商品（基本/销售）属性值表
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
@ApiModel(description = "商品（基本/销售）属性值表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("attribute_value")
public class AttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花id
     */
    @ApiModelProperty("雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 属性值
     */
    @ApiModelProperty("属性值")
    private String name;

    /**
     * 属性id
     */
    @ApiModelProperty("属性id")
    private String attrId;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("")
    private Integer isDeleted;


}
