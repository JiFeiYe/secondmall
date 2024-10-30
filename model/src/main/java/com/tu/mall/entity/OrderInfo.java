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
 * 订单详情信息
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
@ApiModel(description = "订单详情信息")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_info")
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单地址（买家）
     */
    @ApiModelProperty("订单地址（买家）")
    @TableField(exist = false)
    private OrderAddress orderAddress;
    /**
     * 订单商品信息id
     */
    @ApiModelProperty("订单商品信息id")
    @TableField(exist = false)
    private OrderItem orderItem;
    /**
     * 订单商品信息
     */
    @ApiModelProperty("订单商品信息")
    @TableField(exist = false)
    private SkuInfo skuInfo;

    /**
     * 雪花订单id
     */
    @ApiModelProperty("雪花订单id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 买家id
     */
    @ApiModelProperty("买家id")
    private Long buyerId;

    /**
     * 卖家id
     */
    @ApiModelProperty("卖家id")
    private Long sellerId;

    /**
     * 订单状态（0待付款，1待发货，2已发货，3已完成，4已关闭）
     */
    @ApiModelProperty("订单状态（0待付款，1待发货，2已发货，3已完成，4已关闭）")
    private Integer status;

    /**
     * 自动确认时间（天），定时任务
     */
    @ApiModelProperty("自动确认时间（天），定时任务")
    private Integer autoConfirmDay;

    /**
     * 物流单号
     */
    @ApiModelProperty("物流单号")
    private Long deliveryId;

    /**
     * 订单总金额
     */
    @ApiModelProperty("订单总金额")
    private BigDecimal totalPrice;

    /**
     * 订单应付金额
     */
    @ApiModelProperty("订单应付金额")
    private BigDecimal payPrice;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("")
    private Integer isDeleted;


}
