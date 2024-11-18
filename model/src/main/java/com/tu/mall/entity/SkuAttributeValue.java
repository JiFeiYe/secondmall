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
 * sku基本/销售属性值
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-17
 */
@ApiModel(description = "sku基本/销售属性值")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sku_attribute_value")
public class SkuAttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花id
     */
    @ApiModelProperty("雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * sku的id
     */
    @ApiModelProperty("sku的id")
    private String skuId;

    /**
     * 基本/销售属性id
     */
    @ApiModelProperty("基本/销售属性id")
    private String attrId;
    /**
     * 基本/销售属性值id
     */
    @ApiModelProperty("基本/销售属性值id")
    private String attrValueId;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("")
    private Integer isDeleted;


}
