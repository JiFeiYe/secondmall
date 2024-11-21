package com.tu.mall;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.api.SearchService;
import com.tu.mall.entity.SkuAttributeValue;
import com.tu.mall.entity.SkuImg;
import com.tu.mall.entity.SkuInfo;
import com.tu.mall.entity.UserAddress;
import com.tu.mall.entity.es.SearchParam;
import com.tu.mall.entity.es.SearchResponseVo;
import com.tu.mall.entity.map.AddressResult;
import com.tu.mall.entity.map.Districts;
import com.tu.mall.mapper.SkuAttributeValueMapper;
import com.tu.mall.mapper.SkuImgMapper;
import com.tu.mall.mapper.SkuInfoMapper;
import com.tu.mall.mapper.UserAddressMapper;
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
import java.util.HashMap;
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
    private UserAddressMapper userAddressMapper;

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

    private final String key = "8bcac94af0919c132a5cc37ad41255fa";
    private final String url = "https://restapi.amap.com/v3/config/district?parameters";

    @Test
    public void testGaoDeMap() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("key", key);
        paramsMap.put("keywords", "广东省");
        paramsMap.put("subdistrict", 2);
        paramsMap.put("page", 1);
        paramsMap.put("output", "JSON");
        String res = HttpUtil.get(url, paramsMap);
//        System.out.println("JSONUtil.formatJsonStr(res) = \n" + JSONUtil.formatJsonStr(res));
        AddressResult addressResult = JSONUtil.toBean(res, AddressResult.class);
        System.out.println(addressResult);
    }

    // 生成地址数据
    @Test
    public void testAddress() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("key", key);
        paramsMap.put("subdistrict", 3);
        paramsMap.put("page", 1);
        paramsMap.put("output", "JSON");
        String GDAddress = HttpUtil.get(url, paramsMap);
//        System.out.println("JSONUtil.formatJsonStr(GDAddress) = \n" + JSONUtil.formatJsonStr(GDAddress));
        AddressResult addressResult = JSONUtil.toBean(GDAddress, AddressResult.class);
        List<UserAddress> userAddressList = userAddressMapper.selectList(null);

        int index = 0;
        for (Districts districts : addressResult.getDistricts().get(0).getDistricts()) { // 跳过第一层 level==country
            if (index > 99) {
                break;
            }

            int i = 0;
            for (; i < 5; i++) {
                if (index > 99) {
                    break;
                }

                String province = districts.getName();
                if (districts.getDistricts().size() < 5 ) {
                    break;
                }
                userAddressList.get(index).setProvince(province);
                userAddressList.get(index).setDetailAddress(province);
                System.out.println(province);

                if (CollUtil.isNotEmpty(districts.getDistricts())) {
                    Districts subDistricts1 = districts.getDistricts().get(i);
                    String city = subDistricts1.getName();
                    userAddressList.get(index).setCity(city);
                    userAddressList.get(index).setDetailAddress(province + city);

                    if (CollUtil.isNotEmpty(subDistricts1.getDistricts())) {
                        Districts subDistricts2 = subDistricts1.getDistricts().get(0);
                        String district = subDistricts2.getName();
                        userAddressList.get(index).setDistrict(district);
                        userAddressList.get(index).setDetailAddress(province + city + district);

                        if (CollUtil.isNotEmpty(subDistricts2.getDistricts())) {
                            Districts subDistricts3 = subDistricts2.getDistricts().get(0);
                            userAddressList.get(index).setDetailAddress(
                                    province + city + district + subDistricts3.getName()
                            );
                        }
                    }
                }
                index++;
            }
        }
        String jsonStr = JSONUtil.toJsonStr(userAddressList);
        System.out.println("JSONUtil.formatJsonStr(jsonStr) = \n" + JSONUtil.formatJsonStr(jsonStr));
        userAddressMapper.updateById(userAddressList);
    }


    @Test
    public void testAddress2() {
        String s = generateRandomCoordinates(10);
        System.out.println("s = " + s);
    }

    /**
     * 生成指定数量的随机中国境内经纬度坐标。
     *
     * @param count 需要生成的坐标点数量
     * @return 包含所有坐标点的字符串，坐标点之间用'|'分隔
     */
    public static String generateRandomCoordinates(int count) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            // 生成随机经度（Longitude）
            double longitude = 73.663024 + (135.047432 - 73.663024) * random.nextDouble();
            // 生成随机纬度（Latitude）
            double latitude = 3.861216 + (53.568195 - 3.861216) * random.nextDouble();
            // 格式化坐标值，保留六位小数
            String formattedLong = String.format("%.6f", longitude);
            String formattedLat = String.format("%.6f", latitude);
            // 将坐标添加到结果字符串中
            result.append(formattedLong).append(",").append(formattedLat);
            // 如果不是最后一个坐标点，则添加分隔符
            if (i < count - 1) {
                result.append("|");
            }
        }
        return result.toString();
    }
}
