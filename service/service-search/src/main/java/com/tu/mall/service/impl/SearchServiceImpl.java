package com.tu.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tu.mall.api.SearchService;
import com.tu.mall.entity.Attribute;
import com.tu.mall.entity.SkuAttributeValue;
import com.tu.mall.entity.SkuInfo;
import com.tu.mall.entity.es.Good;
import com.tu.mall.entity.es.SearchAttr;
import com.tu.mall.entity.view.CategoryView;
import com.tu.mall.mapper.AttributeMapper;
import com.tu.mall.mapper.AttributeValueMapper;
import com.tu.mall.mapper.view.CategoryViewMapper;
import com.tu.mall.repository.GoodRepository;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author JiFeiYe
 * @since 2024/10/17
 */
@DubboService
public class SearchServiceImpl implements SearchService {

    @Autowired
    private AttributeMapper attributeMapper;
    @Autowired
    private AttributeValueMapper attributeValueMapper;
    @Autowired
    private CategoryViewMapper categoryViewMapper;

    @Autowired
    private GoodRepository goodRepository;

    @Override
    public void upperGoods(SkuInfo skuInfo) {
        // 先匹配实体类（skuInfo -> good）

        // 设置常规属性
        Good good = new Good();
        BeanUtil.copyProperties(skuInfo, good);

        // 设置imgUrlList
        List<String> urlList = new ArrayList<>();
        skuInfo.getSkuImgList().forEach(skuImg -> {
            String url = skuImg.getUrl();
            urlList.add(url);
        });
        good.setImgUrlList(urlList);

        // 设置searchAttrList
        List<SkuAttributeValue> skuAttributeValueList = skuInfo.getSkuAttributeValueList();
        List<SearchAttr> searchAttrList = new ArrayList<>();
        AtomicLong category3Id = new AtomicLong(0); // 原子更新
        skuAttributeValueList.forEach(skuAttributeValue -> {
            SearchAttr searchAttr = new SearchAttr();
            searchAttr.setAttrId(skuAttributeValue.getAttrId());
            searchAttr.setAttrValueId(skuAttributeValue.getAttrValueId());
            Attribute attribute = attributeMapper.selectById(skuAttributeValue.getAttrId());
            searchAttr.setAttrName(
                    attribute.getName()
            );
            searchAttr.setAttrValueName(
                    attributeValueMapper.selectById(skuAttributeValue.getAttrValueId()).getValue()
            );
            searchAttrList.add(searchAttr);
            category3Id.compareAndSet(0, attribute.getCategoryId()); // 就第一次就行了，后面都跳过
        });
        good.setAttrList(searchAttrList);

        // 设置一二三级分类
        LambdaQueryWrapper<CategoryView> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CategoryView::getCategory3Id, category3Id);
        CategoryView categoryView = categoryViewMapper.selectOne(lqw);
        BeanUtil.copyProperties(categoryView, good);

        // 加入es
        goodRepository.save(good);
    }

    @Override
    public void lowerGoods(Long goodId) {
        goodRepository.deleteById(goodId);
    }
}
