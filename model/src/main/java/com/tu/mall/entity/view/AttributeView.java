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
    private String attrId;
    @ApiModelProperty("属性")
    private String attrName;
    @ApiModelProperty("二级分类id")
    private String categoryId; // 二级
    @ApiModelProperty("属性值id")
    private String attrValueId;
    @ApiModelProperty("属性值")
    private String attrValueName;
}
