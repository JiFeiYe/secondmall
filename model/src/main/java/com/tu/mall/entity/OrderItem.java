package com.tu.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单商品信息（时效）
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
@ApiModel(description = "订单商品信息（时效）")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_item")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花id
     */
    @ApiModelProperty("雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private String orderId;

    /**
     * 商品sku的id
     */
    @ApiModelProperty("商品sku的id")
    private String skuId;

    /**
     * 商品属性表id（用,分隔）
     */
    @ApiModelProperty("商品属性表id（用,分隔）")
    private String skuAttrValueId;

    /**
     * 商品销售价格（下单时）
     */
    @ApiModelProperty("商品销售价格（下单时）")
    private BigDecimal price;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("")
    private Integer isDeleted;


}
