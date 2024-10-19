package com.tu.mall.entity.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JiFeiYe
 * @since 2024/10/18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("category_view")
public class CategoryView implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("category1_id")
    private Long category1Id;

    @TableField("category1_name")
    private String category1Name;

    @TableField("category2_id")
    private Long category2Id;

    @TableField("category2_name")
    private String category2Name;

    @TableField("category3_id")
    private Long category3Id;

    @TableField("category3_name")
    private String category3Name;
}
