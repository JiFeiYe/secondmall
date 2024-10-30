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
 * 平台评价
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-30
 */
@ApiModel(description = "平台评价")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("app_review")
public class AppReview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花id
     */
    @ApiModelProperty("雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

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

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("")
    private Integer isDeleted;


}
