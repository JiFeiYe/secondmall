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
 * 图片
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
@ApiModel(description = "图片")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sku_img")
public class SkuImg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花图片id
     */
    @ApiModelProperty("雪花图片id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 对应spu的id
     */
    @ApiModelProperty("对应spu的id")
    private String skuId;

    /**
     * 图片名字（带后缀）
     */
    @ApiModelProperty("图片名字（带后缀）")
    private String name;

    /**
     * 图片地址（阿里云oss）
     */
    @ApiModelProperty("图片地址（阿里云oss）")
    private String url;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("")
    private Integer isDeleted;


}
