package com.tu.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tu.mall.api.SearchService;
import com.tu.mall.common.exception.CustomException;
import com.tu.mall.entity.*;
import com.tu.mall.entity.es.Good;
import com.tu.mall.entity.es.SearchAttr;
import com.tu.mall.entity.es.SearchParam;
import com.tu.mall.entity.es.SearchResponseVo;
import com.tu.mall.entity.view.CategoryView;
import com.tu.mall.mapper.AttributeMapper;
import com.tu.mall.mapper.AttributeValueMapper;
import com.tu.mall.mapper.view.CategoryViewMapper;
import com.tu.mall.repository.GoodRepository;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
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
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void upperGoods(SkuInfo skuInfo) {
        // 先匹配实体类（skuInfo -> good）
        System.out.println(skuInfo);
        // 设置常规属性
        Good good = new Good();
        BeanUtil.copyProperties(skuInfo, good);
        good.setCategory3Id(skuInfo.getCategoryId());

        // 设置imgUrlList
        List<SkuImg> skuImgList = skuInfo.getSkuImgList();
        if (skuImgList != null && CollUtil.isNotEmpty(skuImgList)) {
            List<String> urlList = new ArrayList<>();
            skuImgList.forEach(skuImg -> {
                String url = skuImg.getUrl();
                urlList.add(url);
            });
            good.setImgUrlList(urlList);
        }

        // 设置searchAttrList
        List<SkuAttributeValue> skuAttributeValueList = skuInfo.getSkuAttributeValueList();
        List<SearchAttr> searchAttrList = new ArrayList<>();
        AtomicLong category3Id = new AtomicLong(0); // 原子更新
        if (skuAttributeValueList != null && CollUtil.isNotEmpty(skuAttributeValueList)) {
            skuAttributeValueList.forEach(skuAttributeValue -> {
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(skuAttributeValue.getAttrId());
                searchAttr.setAttrValueId(skuAttributeValue.getAttrValueId());
                Attribute attribute = attributeMapper.selectById(skuAttributeValue.getAttrId());
                if (ObjectUtil.isNull(attribute))
                    return;
                searchAttr.setAttrName(
                        attribute.getName()
                );
                AttributeValue attributeValue = attributeValueMapper.selectById(skuAttributeValue.getAttrValueId());
                if (ObjectUtil.isNull(attributeValue))
                    return;
                searchAttr.setAttrValueName(
                        attributeValue.getName()
                );
                searchAttrList.add(searchAttr);
                category3Id.compareAndSet(0, attribute.getCategoryId()); // 就第一次就行了，后面都跳过
            });
        }
        good.setAttrList(searchAttrList);
        // 设置一二三级分类
        LambdaQueryWrapper<CategoryView> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CategoryView::getCategory3Id, category3Id);
        CategoryView categoryView = categoryViewMapper.selectOne(lqw);
        BeanUtil.copyProperties(categoryView, good);

        // 加入es
        System.out.println(good);
        goodRepository.save(good);
    }

    @Override
    public void lowerGoods(Long goodId) {
        goodRepository.deleteById(goodId);
    }

    @Override
    public SearchResponseVo search(SearchParam searchParam) {
        SearchRequest request = this.buildDsl(searchParam);
        SearchResponse response;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new CustomException(e.getMessage(), 5002);
        }
        SearchResponseVo vo = this.parseResponse(response);
        vo.setPage(searchParam.getPage());
        vo.setSize(searchParam.getSize());
        vo.setTotalPages(
                vo.getTotal() % searchParam.getSize() == 0 ?
                        vo.getTotal() / searchParam.getSize() :
                        vo.getTotal() / searchParam.getSize() + 1
        );
        return vo;
    }

    private SearchRequest buildDsl(SearchParam searchParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        // 一二三级分类
        if (ObjectUtil.isNotNull(searchParam.getCategory1Id())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("category1Id", searchParam.getCategory1Id()));
        }
        if (ObjectUtil.isNotNull(searchParam.getCategory2Id())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("category2Id", searchParam.getCategory2Id()));
        }
        if (ObjectUtil.isNotNull(searchParam.getCategory3Id())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("category3Id", searchParam.getCategory3Id()));
        }

        // 属性 {"运行内存,8GB", "屏幕尺寸,7英寸", ...}
        String[] attrs = searchParam.getAttrs();
        if (ObjectUtil.isNotNull(attrs) && ArrayUtil.isNotEmpty(attrs)) {
            for (String attr : attrs) {
                List<String> split = StrUtil.split(attr, ":");
                BoolQueryBuilder innerQueryBuilder = QueryBuilders.boolQuery();
                innerQueryBuilder.must(QueryBuilders.matchQuery("attrName", split.get(0)));
                innerQueryBuilder.must(QueryBuilders.matchQuery("attrValueName", split.get(1)));
                boolQueryBuilder.filter(
                        QueryBuilders.boolQuery().must(
                                QueryBuilders.nestedQuery("attrList", innerQueryBuilder, ScoreMode.None)
                        )
                );
            }
        }

        // 关键字
        String keyText = searchParam.getKeyText();
        if (StrUtil.isNotEmpty(keyText)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", keyText).operator(Operator.OR));
            boolQueryBuilder.should(QueryBuilders.matchQuery("description", keyText).operator(Operator.OR));
        }

        searchSourceBuilder.query(boolQueryBuilder);

        // 分页
        Integer page = searchParam.getPage();
        Integer size = searchParam.getSize();
        searchSourceBuilder.from((page - 1) * size).size(size);

        // 排序规则
        String[] orders = searchParam.getOrders();
        if (ArrayUtil.isNotEmpty(orders)) {
            for (String order : orders) {
                List<String> split = StrUtil.split(order, ":");
                if (StrUtil.equals(split.get(0), "price")) {
                    searchSourceBuilder.sort(
                            split.get(0),
                            StrUtil.equals("asc", split.get(1)) ? SortOrder.ASC : SortOrder.DESC
                    );
                } else if (StrUtil.equals(split.get(0), "createTime")) {
                    searchSourceBuilder.sort(
                            split.get(0),
                            StrUtil.equals("asc", split.get(1)) ? SortOrder.ASC : SortOrder.DESC
                    );
                }
            }
        }
        searchSourceBuilder.sort("createTime", SortOrder.DESC);
        searchSourceBuilder.sort("_score", SortOrder.DESC);

        // 实施查询
        SearchRequest searchRequest = new SearchRequest("good");
        searchRequest.source(searchSourceBuilder);
        System.out.println("dsl:\n" + searchSourceBuilder);

        // 选择、排除字段
//        searchSourceBuilder.fetchSource(new String[]{"id", "defaultImg", "title", "price", "createTime"}, null);

        return searchRequest;
    }

    private SearchResponseVo parseResponse(SearchResponse response) {
        SearchResponseVo vo = new SearchResponseVo();
        SearchHits hits = response.getHits();
        // 总记录数
        vo.setTotal(hits.getTotalHits().value);
        // 商品数据
        SearchHit[] h = hits.getHits();
        List<Good> goodList = new ArrayList<>();
        if (ArrayUtil.isNotEmpty(h)) {
            for (SearchHit hit : h) {
                String sourceString = hit.getSourceAsString();
                Good good = JSONUtil.toBean(sourceString, Good.class);
                goodList.add(good);
            }
        }
        vo.setGoodList(goodList);
        return vo;
    }
}
