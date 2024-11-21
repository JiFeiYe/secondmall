package com.tu.mall.entity.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author JiFeiYe
 * @since 2024/11/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Districts {
    /**
     * 城市编码
     */
    private List<String> citycode;

    /**
     * 区域编码
     * 街道没有独有的adcode，均继承父类（区县）的adcode
     */
    private String adcode;

    /**
     * 行政区名称
     */
    private String name;

    /**
     * 行政区边界坐标点
     * 当一个行政区范围由完全分隔两块或者多块的地块组成，每块地的polyline坐标串以|分隔。
     * 如北京的朝阳区
     */
    private String polyline;

    /**
     * 区域中心点
     * 乡镇级别返回的center是边界线上的形点，其他行政级别返回的center不一定是中心点，
     * 若政府机构位于面内，则返回政府坐标，政府不在面内，则返回繁华点坐标。
     */
    private String center;

    /**
     * 行政区划级别
     * country: 国家
     * province: 省份（直辖市会在province显示）
     * city: 市（直辖市会在province显示）
     * district: 区县
     * street: 街道
     */
    private String level;

    /**
     * 下级行政区列表，包含district元素
     */
    private List<Districts> districts;
}
