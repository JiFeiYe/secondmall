package com.tu.mall.entity.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 行政区划信息实体类
 *
 * @author JiFeiYe
 * @since 2024/11/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResult {

    /**
     * 返回结果状态值
     * 值为0或1，0表示失败；1表示成功
     */
    private Integer status;

    /**
     * 返回状态说明
     * 当status为0时，info返回错误原因，否则返回“OK”。
     */
    private String info;

    /**
     * 状态码
     * 10000代表正确，详情参阅info状态表
     */
    private String infocode;

    /**
     * 建议结果列表
     */
    private Suggestion suggestion;

    /**
     * 行政区列表
     */
    private List<Districts> districts;

}