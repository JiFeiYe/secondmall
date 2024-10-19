package com.tu.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("attribute")
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 对应三级分类id
     */
    private Long categoryId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Integer isDeleted;


}
