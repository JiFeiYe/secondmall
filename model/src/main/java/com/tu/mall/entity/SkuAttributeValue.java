package com.tu.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sku_attribute_value")
public class SkuAttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * sku的id
     */
    private Long skuId;

    /**
     * 基本/销售属性id
     */
    private Long attrId;
    /**
     * 基本/销售属性值id
     */
    private Long attrValueId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Integer isDeleted;


}
