package com.tu.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * sku主信息
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
@ApiModel(description = "sku主信息")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sku_info")
public class SkuInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图片文件列表
     */
    @ApiModelProperty("图片文件列表")
    @TableField(exist = false)
    private List<MultipartFile> imgFileList;
    /**
     * 图片列表
     */
    @ApiModelProperty("图片列表")
    @TableField(exist = false)
    private List<SkuImg> skuImgList;
    /**
     * 属性列表
     */
    @ApiModelProperty("属性列表")
    @TableField(exist = false)
    private List<SkuAttributeValue> skuAttributeValueList;

    /**
     * 雪花id
     */
    @ApiModelProperty("雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 商品标题
     */
    @ApiModelProperty("商品标题")
    private String title;

    /**
     * 商品描述
     */
    @ApiModelProperty("商品描述")
    private String description;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 商品状态（0下架，1上架）
     */
    @ApiModelProperty("商品状态（0下架，1上架）")
    private Integer status;

    /**
     * 三级分类id（冗余）
     */
    @ApiModelProperty("三级分类id（冗余）")
    private String categoryId;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("")
    private Integer isDeleted;


}
