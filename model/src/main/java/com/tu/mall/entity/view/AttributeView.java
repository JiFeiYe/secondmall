package com.tu.mall.entity.view;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JiFeiYe
 * @since 2024/10/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("attribute_view")
public class AttributeView implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long attrId;
    private String attrName;
    private Long categoryId; // 三级
    private Long attrValueId;
    private String attrValueName;
}
