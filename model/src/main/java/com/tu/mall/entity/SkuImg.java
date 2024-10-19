package com.tu.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sku_img")
public class SkuImg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花图片id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 对应spu的id
     */
    private Long skuId;

    /**
     * 图片名字（带后缀）
     */
    private String name;

    /**
     * 图片地址（阿里云oss）
     */
    private String url;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Integer isDeleted;


}
