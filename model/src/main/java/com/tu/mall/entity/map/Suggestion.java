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
public class Suggestion {
    /**
     * 建议关键字列表
     */
    private List<String> keywords;

    /**
     * 建议城市列表
     */
    private List<String> cities;
}
