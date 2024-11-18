package com.tu.mall.entity.es;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * EsIndexMapping
 *
 * @author JiFeiYe
 * @since 2024/10/17
 */
@ApiModel(description = "EsIndexMapping")
@Data
@Document(indexName = "good")
public class Good implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 商品雪花id
     */
    @ApiModelProperty("商品雪花id")
    @Id
    private String id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @Field(type = FieldType.Text, index = false)
    private String userId;
    /**
     * 商品标题
     */
    @ApiModelProperty("商品标题")
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    /**
     * 商品描述
     */
    @ApiModelProperty("商品描述")
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String description;
    /**
     * 价格
     */
    @ApiModelProperty("价格")
    @Field(type = FieldType.Double)
    private BigDecimal price;
    /**
     * 上架时间
     */
    @ApiModelProperty("上架时间")
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 一级分类
     */
    @ApiModelProperty("一级分类")
    @Field(type = FieldType.Text)
    private String category1Id;
    @ApiModelProperty("")
    @Field(type = FieldType.Keyword)
    private String category1Name;
    /**
     * 二级分类
     */
    @ApiModelProperty("二级分类")
    @Field(type = FieldType.Text)
    private String category2Id;
    @ApiModelProperty("")
    @Field(type = FieldType.Keyword)
    private String category2Name;
    /**
     * 三级分类
     */
    @ApiModelProperty("三级分类")
    @Field(type = FieldType.Text)
    private String category3Id;
    @ApiModelProperty("")
    @Field(type = FieldType.Keyword)
    private String category3Name;
    /**
     * 图片地址
     */
    @ApiModelProperty("图片地址")
    @Field(type = FieldType.Keyword, index = false)
    private List<String> imgUrlList;
    /**
     * 属性
     */
    @ApiModelProperty("属性")
    @Field(type = FieldType.Nested)
    private List<SearchAttr> attrList;
}