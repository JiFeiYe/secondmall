package com.tu.mall.entity.view;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JiFeiYe
 * @since 2024/10/23
 */
@ApiModel(description = "全属性视图")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("attribute_view")
public class AttributeView implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("属性id")
    private Long attrId;
    @ApiModelProperty("属性")
    private String attrName;
    @ApiModelProperty("三级分类id")
    private Long categoryId; // 三级
    @ApiModelProperty("属性值id")
    private Long attrValueId;
    @ApiModelProperty("属性值")
    private String attrValueName;
}
