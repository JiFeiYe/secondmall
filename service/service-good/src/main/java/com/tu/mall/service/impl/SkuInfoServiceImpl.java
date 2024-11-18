package com.tu.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.mall.entity.SkuAttributeValue;
import com.tu.mall.entity.SkuImg;
import com.tu.mall.entity.SkuInfo;
import com.tu.mall.mapper.SkuAttributeValueMapper;
import com.tu.mall.mapper.SkuImgMapper;
import com.tu.mall.mapper.SkuInfoMapper;
import com.tu.mall.service.ISkuInfoService;
import com.tu.mall.template.OSSTemplate;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JiFeiYe
 * @since 2024/10/17
 */
@DubboService
@Transactional
public class SkuInfoServiceImpl implements ISkuInfoService {

    @Autowired
    private OSSTemplate ossTemplate;

    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuImgMapper skuImgMapper;
    @Autowired
    private SkuAttributeValueMapper skuAttributeValueMapper;

    @Override
    public SkuInfo saveGoods(SkuInfo skuInfo) {
        // 保存skuInfo
        {
            skuInfo.setStatus(0); // 尚未上架es
            skuInfoMapper.insert(skuInfo);
        }

        // 保存skuImg
        {
            List<MultipartFile> imgFileList = skuInfo.getImgFileList();
            if (imgFileList != null && CollUtil.isNotEmpty(imgFileList)) {
                List<SkuImg> skuImgList = new ArrayList<>();
                // 上传oss
                for (MultipartFile multipartFile : imgFileList) {
                    String url = ossTemplate.upload(multipartFile);
                    SkuImg skuImg = new SkuImg();
                    skuImg.setSkuId(skuInfo.getId());
                    skuImg.setName(url.substring(url.lastIndexOf("/") + 1));
                    skuImg.setUrl(url);
                    skuImg.setId(IdWorker.getIdStr(skuImg));
                    skuImgList.add(skuImg);
                }
                skuImgMapper.insert(skuImgList);
                skuInfo.setSkuImgList(skuImgList);
            }
        }

        // 保存sku_attribute_value
        {
            List<SkuAttributeValue> skuAttributeValueList = skuInfo.getSkuAttributeValueList();
            if (skuAttributeValueList != null && CollUtil.isNotEmpty(skuAttributeValueList)) {
                skuAttributeValueMapper.insert(skuAttributeValueList);
                skuInfo.setSkuAttributeValueList(skuAttributeValueList);
            }
        }
        return skuInfo;
    }

    @Override
    public Page<SkuInfo> getGoods(String userId, Integer page, Integer size) {
        Page<SkuInfo> skuInfopage = new Page<>(page, size);
        LambdaQueryWrapper<SkuInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SkuInfo::getUserId, userId);
        skuInfopage = skuInfoMapper.selectPage(skuInfopage, lqw);
        List<SkuInfo> skuInfoList = new ArrayList<>();
        for (SkuInfo skuInfo : skuInfopage.getRecords()) {
            // 补充图片信息
            String skuId = skuInfo.getId();
            LambdaQueryWrapper<SkuImg> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(SkuImg::getSkuId, skuId);
            List<SkuImg> skuImgList = skuImgMapper.selectList(lqw1);
            skuInfo.setSkuImgList(skuImgList);
            // 补充属性信息
            LambdaQueryWrapper<SkuAttributeValue> lqw2 = new LambdaQueryWrapper<>();
            lqw2.eq(SkuAttributeValue::getSkuId, skuId);
            List<SkuAttributeValue> skuAttributeValueList = skuAttributeValueMapper.selectList(lqw2);
            skuInfo.setSkuAttributeValueList(skuAttributeValueList);

            skuInfoList.add(skuInfo);
        }
        skuInfopage.setRecords(skuInfoList);
        return skuInfopage;
    }

    @Override
    public Page<SkuInfo> getGoods(Integer page, Integer size) {
        Page<SkuInfo> skuInfopage = new Page<>(page, size);
        skuInfopage = skuInfoMapper.selectPage(skuInfopage, null);
        List<SkuInfo> skuInfoList = new ArrayList<>();
        for (SkuInfo skuInfo : skuInfopage.getRecords()) {
            // 补充图片信息
            String skuId = skuInfo.getId();
            LambdaQueryWrapper<SkuImg> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(SkuImg::getSkuId, skuId);
            List<SkuImg> skuImgList = skuImgMapper.selectList(lqw1);
            if (CollUtil.isNotEmpty(skuImgList))
                skuInfo.setSkuImgList(skuImgList);
            // 补充属性信息
            LambdaQueryWrapper<SkuAttributeValue> lqw2 = new LambdaQueryWrapper<>();
            lqw2.eq(SkuAttributeValue::getSkuId, skuId);
            List<SkuAttributeValue> skuAttributeValueList = skuAttributeValueMapper.selectList(lqw2);
            if (CollUtil.isNotEmpty(skuAttributeValueList))
                skuInfo.setSkuAttributeValueList(skuAttributeValueList);

            skuInfoList.add(skuInfo);
        }
        skuInfopage.setRecords(skuInfoList);
        return skuInfopage;
    }

    @Override
    public SkuInfo getGoods(String skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        {
            LambdaQueryWrapper<SkuImg> lqw = new LambdaQueryWrapper<>();
            lqw.eq(SkuImg::getSkuId, skuId);
            SkuImg skuImg = skuImgMapper.selectOne(lqw);
            List<SkuImg> skuImgList = new ArrayList<>();
            skuImgList.add(skuImg);
            skuInfo.setSkuImgList(skuImgList);
        }
        {
            LambdaQueryWrapper<SkuAttributeValue> lqw = new LambdaQueryWrapper<>();
            lqw.eq(SkuAttributeValue::getSkuId, skuId);
            List<SkuAttributeValue> skuAttributeValues = skuAttributeValueMapper.selectList(lqw);
            skuInfo.setSkuAttributeValueList(skuAttributeValues);
        }
        return skuInfo;
    }

    @Override
    public void updateGoods(String userId, SkuInfo skuInfo) {
        // 更新skuInfo
        {
            skuInfo.setUserId(userId);
            skuInfoMapper.updateById(skuInfo);
        }

        // 更新skuImg（文件形式的直接上传oss并插入数据库）
        {
            List<SkuImg> imgs = new ArrayList<>();
            List<MultipartFile> imgFileList = skuInfo.getImgFileList();
            if (imgFileList != null && CollUtil.isNotEmpty(imgFileList)) {
                imgFileList.forEach(file -> {
                    String url = ossTemplate.upload(file);
                    SkuImg skuImg = new SkuImg();
                    skuImg.setSkuId(skuInfo.getId());
                    skuImg.setName(url.substring(url.lastIndexOf("/") + 1));
                    skuImg.setUrl(url);
                    imgs.add(skuImg);
                });
                skuImgMapper.insert(imgs);
            }
            // 删除与skuImgList无法匹配的表中数据（局限于某一个skuId）
            List<SkuImg> skuImgList = skuInfo.getSkuImgList();
            if (skuImgList != null) {
                if (CollUtil.isNotEmpty(imgs)) {
                    skuImgList.addAll(imgs);
                }
                List<String> ids = skuImgList.stream().map(SkuImg::getId).collect(Collectors.toList());
                LambdaQueryWrapper<SkuImg> lqw = new LambdaQueryWrapper<>();
                lqw.eq(SkuImg::getSkuId, skuInfo.getId())
                        .notIn(SkuImg::getId, ids);
                skuImgMapper.delete(lqw);
            } else { // 删除全部
                List<SkuImg> skuImgs = new ArrayList<>();
                if (CollUtil.isNotEmpty(imgs)) {
                    skuImgs.addAll(imgs);
                }
                List<String> ids = skuImgs.stream().map(SkuImg::getId).collect(Collectors.toList());
                LambdaQueryWrapper<SkuImg> lqw = new LambdaQueryWrapper<>();
                lqw.eq(SkuImg::getSkuId, skuInfo.getId())
                        .notIn(SkuImg::getId, ids);
                skuImgMapper.delete(lqw);
            }
            LambdaQueryWrapper<SkuImg> lqw = new LambdaQueryWrapper<>();
            lqw.eq(SkuImg::getSkuId, skuInfo.getId());
            skuInfo.setSkuImgList(skuImgMapper.selectList(lqw));
        }

        // 更新sku_attribute_value
        {
            List<SkuAttributeValue> skuAttributeValueList = skuInfo.getSkuAttributeValueList();
            if (skuAttributeValueList != null) {
                List<String> ids = skuAttributeValueList
                        .stream()
                        .map(SkuAttributeValue::getId)
                        .collect(Collectors.toList());
                LambdaQueryWrapper<SkuAttributeValue> lqw = new LambdaQueryWrapper<>();
                lqw.eq(SkuAttributeValue::getSkuId, skuInfo.getId())
                        .notIn(SkuAttributeValue::getId, ids);
                skuAttributeValueMapper.delete(lqw);
                LambdaQueryWrapper<SkuAttributeValue> lqw1 = new LambdaQueryWrapper<>();
                lqw1.eq(SkuAttributeValue::getSkuId, skuInfo.getId());
                skuInfo.setSkuAttributeValueList(skuAttributeValueMapper.selectList(lqw1));
            } else { // 删除全部
                LambdaQueryWrapper<SkuAttributeValue> lqw = new LambdaQueryWrapper<>();
                lqw.eq(SkuAttributeValue::getSkuId, skuInfo.getId());
                skuAttributeValueMapper.delete(lqw);
            }
        }
    }

    @Override
    public void delGoods(String skuId) {
        // 删除sku_attribute_value
        {
            LambdaQueryWrapper<SkuAttributeValue> lqw = new LambdaQueryWrapper<>();
            lqw.eq(SkuAttributeValue::getSkuId, skuId);
            skuAttributeValueMapper.delete(lqw);
        }

        // 删除图片
        {
            LambdaQueryWrapper<SkuImg> lqw = new LambdaQueryWrapper<>();
            lqw.eq(SkuImg::getSkuId, skuId);
            List<SkuImg> imgs = skuImgMapper.selectList(lqw);
            imgs.forEach(img -> {
                ossTemplate.delete(img.getName());
            });
            skuImgMapper.delete(lqw);
        }

        // 删除商品
        {
            skuInfoMapper.deleteById(skuId);
        }
    }

    @Override
    public String getUserId(String skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        return String.valueOf(skuInfo.getUserId());
    }
}
