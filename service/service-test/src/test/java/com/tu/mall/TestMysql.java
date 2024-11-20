package com.tu.mall;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.api.SearchService;
import com.tu.mall.entity.SkuAttributeValue;
import com.tu.mall.entity.SkuImg;
import com.tu.mall.entity.SkuInfo;
import com.tu.mall.entity.es.SearchParam;
import com.tu.mall.entity.es.SearchResponseVo;
import com.tu.mall.mapper.SkuAttributeValueMapper;
import com.tu.mall.mapper.SkuImgMapper;
import com.tu.mall.mapper.SkuInfoMapper;
import com.tu.mall.service.ISkuInfoService;
import com.tu.mall.template.OSSTemplate;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author JiFeiYe
 * @since 2024/11/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMysql {

    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuImgMapper skuImgMapper;
    @Autowired
    private SkuAttributeValueMapper skuAttributeValueMapper;

    @Autowired
    private OSSTemplate ossTemplate;

    @DubboReference
    private SearchService searchService;
    @DubboReference
    private ISkuInfoService skuInfoService;

    @Test
    public void test1() throws IOException {
        List<SkuInfo> skuInfoList = skuInfoMapper.selectList(null);
        List<String> ids = skuInfoList.stream().map(SkuInfo::getId).collect(Collectors.toList());
        final String path = "C:\\Users\\LLeavee\\Pictures\\Screenshots\\GEM1.jpg";
        File file = new File(path);
        // 读取文件内容
        FileInputStream input = new FileInputStream(file);
        byte[] content = new byte[(int) file.length()];
        input.read(content);
        input.close();

        for (String id : ids) {
            List<SkuImg> skuImgList = new ArrayList<>();
            MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image/jpg", content);
            // 上传oss
            String url = ossTemplate.upload(multipartFile);
            SkuImg skuImg = new SkuImg();
            skuImg.setSkuId(id);
            skuImg.setName(url.substring(url.lastIndexOf("/") + 1));
            skuImg.setUrl(url);
            skuImg.setId(IdWorker.getIdStr(skuImg));
            skuImgList.add(skuImg);
            skuImgMapper.insert(skuImgList);
        }
    }

    @Test
    public void testEs() {
        Page<SkuInfo> skuInfoPage = skuInfoService.getGoods(1, 1001);
        List<SkuInfo> skuInfoList = skuInfoPage.getRecords();
        for (SkuInfo skuInfo : skuInfoList) {
            searchService.upperGoods(skuInfo);
        }
    }

    @Test
    public void testSearch() {
        SearchParam searchParam = new SearchParam();
        searchParam.setKeyText("apple");
        searchParam.setOrders(new String[]{"price:desc"});
        searchParam.setPage(1);
        searchParam.setSize(500);
        SearchResponseVo search = searchService.search(searchParam);
        String jsonStr = JSONUtil.toJsonStr(search);
        System.out.println("JSONUtil.formatJsonStr(jsonStr) = \n" + JSONUtil.formatJsonStr(jsonStr));
    }

    @Test
    public void testSetSkuInfoCategoryId() {
        LambdaUpdateWrapper<SkuInfo> luw = new LambdaUpdateWrapper<>();
        luw.set(SkuInfo::getCategoryId, "1848608321747771393");
        skuInfoMapper.update(luw);
    }

    // 生成商品属性数据
    @Test
    public void testSkuAttr() {
        List<SkuInfo> skuInfoList = skuInfoMapper.selectList(null);
        List<String> ids = skuInfoList.stream().map(SkuInfo::getId).collect(Collectors.toList());
        List<SkuAttributeValue> skuAttributeValueList = new ArrayList<>();

        Random random = new Random();
        for (String id : ids) {
            SkuAttributeValue a = new SkuAttributeValue();
            a.setSkuId(id);
            a.setAttrId("1");
            a.setAttrValueId(String.valueOf(random.nextInt(4) + 1));
            skuAttributeValueList.add(a);
        }
        for (String id : ids) {
            SkuAttributeValue a = new SkuAttributeValue();
            a.setSkuId(id);
            a.setAttrId("2");
            a.setAttrValueId(String.valueOf(random.nextInt(6) + 5));
            skuAttributeValueList.add(a);
        }
        skuAttributeValueMapper.insert(skuAttributeValueList);
    }
}
