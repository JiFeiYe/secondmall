package com.tu.mall.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * es索引库mapping
 *
 * @author JiFeiYe
 * @since 2024/10/17
 */
@Data
@Document(indexName = "good")
public class Good {
    /**
     * 商品雪花id
     */
    @Id
    private Long id;
    /**
     * 用户id
     */
    @Field(type = FieldType.Long, index = false)
    private Integer userId;
    /**
     * 商品标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    /**
     * 商品描述
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String description;
    /**
     * 价格
     */
    @Field(type = FieldType.Double, index = false)
    private BigDecimal price;
    /**
     * 上架时间
     */
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 一级分类
     */
    @Field(type = FieldType.Long)
    private Long category1Id;
    @Field(type = FieldType.Keyword)
    private String category1Name;
    /**
     * 二级分类
     */
    @Field(type = FieldType.Long)
    private Long category2Id;
    @Field(type = FieldType.Keyword)
    private String category2Name;
    /**
     * 三级分类
     */
    @Field(type = FieldType.Long)
    private Long category3Id;
    @Field(type = FieldType.Keyword)
    private String category3Name;
    /**
     * 图片地址
     */
    @Field(type = FieldType.Keyword, index = false)
    private List<String> imgUrlList;
    /**
     * 基本/销售属性
     */
    @Field(type = FieldType.Nested)
    private List<SearchAttr> attrList;
}