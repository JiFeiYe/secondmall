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
 * 订单评价
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-30
 */
@ApiModel(description = "订单评价")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_review")
public class OrderReview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花id
     */
    @ApiModelProperty("雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long orderId;

    /**
     * 用户id（评价者）
     */
    @ApiModelProperty("用户id（评价者）")
    private Long userId;

    /**
     * 评分
     */
    @ApiModelProperty("评分")
    private Integer rating;

    /**
     * 用户评论内容
     */
    @ApiModelProperty("用户评论内容")
    private String comment;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("")
    private Integer isDeleted;


}
