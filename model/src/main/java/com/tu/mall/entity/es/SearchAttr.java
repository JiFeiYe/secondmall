package com.tu.mall.entity.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 仅限基本属性
 *
 * @author JiFeiYe
 * @since 2024/10/18
 */
@Data
public class SearchAttr {
    /**
     * 属性Id
     */
    @Field(type = FieldType.Long, index = false)
    private Long attrId;
    /**
     * 属性
     */
    @Field(type = FieldType.Keyword)
    private String attrName;
    /**
     * 属性值Id
     */
    @Field(type = FieldType.Long, index = false)
    private Long attrValueId;
    /**
     * 属性值
     */
    @Field(type = FieldType.Keyword)
    private String attrValueName;


}