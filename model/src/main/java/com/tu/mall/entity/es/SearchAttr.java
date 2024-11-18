package com.tu.mall.entity.es;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 属性
 *
 * @author JiFeiYe
 * @since 2024/10/18
 */
@ApiModel(description = "属性")
@Data
public class SearchAttr {
    /**
     * 属性Id
     */
    @ApiModelProperty("属性Id")
    @Field(type = FieldType.Text, index = false)
    private String attrId;
    /**
     * 属性
     */
    @ApiModelProperty("属性")
    @Field(type = FieldType.Keyword)
    private String attrName;
    /**
     * 属性值Id
     */
    @ApiModelProperty("属性值Id")
    @Field(type = FieldType.Text, index = false)
    private String attrValueId;
    /**
     * 属性值
     */
    @ApiModelProperty("属性值")
    @Field(type = FieldType.Keyword)
    private String attrValueName;


}