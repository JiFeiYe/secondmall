package com.tu.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JiFeiYe
 * @since 2024/10/17
 */
@DubboService
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
    public SkuInfo saveGoods(String userId, SkuInfo skuInfo) {
        // 保存skuInfo
        skuInfo.setUserId(Long.valueOf(userId));
        skuInfo.setStatus(0); // 尚未上架es
        long skuId = IdWorker.getId(skuInfo);
        skuInfo.setId(skuId);
        skuInfoMapper.insert(skuInfo);

        // 保存skuImg
        List<MultipartFile> imgList = skuInfo.getImgFileList();
        List<SkuImg> imgs = new ArrayList<>();
        // 上传oss
        for (MultipartFile multipartFile : imgList) {
            String url = ossTemplate.upload(multipartFile);
            SkuImg skuImg = new SkuImg();
            skuImg.setSkuId(skuId);
            skuImg.setName(url.substring(url.lastIndexOf("/") + 1));
            skuImg.setUrl(url);
            skuImg.setId(IdWorker.getId(skuImg));
            imgs.add(skuImg);
        }
        skuImgMapper.insert(imgs);
        skuInfo.setSkuImgList(imgs);

        // 保存sku_attribute_value
        skuInfo.getSkuAttributeValueList().forEach(skuAttributeValue -> {
            skuAttributeValueMapper.insert(skuAttributeValue);
        });

        return skuInfo;
    }

    @Override
    public IPage<SkuInfo> getGoods(String userId, Integer page, Integer size) {
        IPage<SkuInfo> skuInfoIpage = new Page<>(page, size);
        LambdaQueryWrapper<SkuInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SkuInfo::getUserId, userId);
        return skuInfoMapper.selectPage(skuInfoIpage, lqw);
    }

    @Override
    public void updateGoods(String userId, SkuInfo skuInfo) {
        // 更新skuInfo
        {
            skuInfo.setUserId(Long.valueOf(userId));
            skuInfoMapper.updateById(skuInfo);
        }

        // 更新skuImg（文件形式的直接上传oss并插入数据库）
        {
            List<SkuImg> imgs = new ArrayList<>();
            skuInfo.getImgFileList().forEach(file -> {
                String url = ossTemplate.upload(file);
                SkuImg skuImg = new SkuImg();
                skuImg.setSkuId(skuInfo.getId());
                skuImg.setName(url.substring(url.lastIndexOf("/") + 1));
                skuImg.setUrl(url);
                imgs.add(skuImg);
            });
            skuImgMapper.insert(imgs);
            // 删除与skuImgList无法匹配的表中数据（局限于某一个skuId）
            List<SkuImg> skuImgList = skuInfo.getSkuImgList();
            List<Long> ids = skuImgList.stream().map(SkuImg::getId).collect(Collectors.toList());
            LambdaQueryWrapper<SkuImg> lqw = new LambdaQueryWrapper<>();
            lqw.eq(SkuImg::getSkuId, skuInfo.getId())
                    .notIn(SkuImg::getId, ids);
            skuImgMapper.delete(lqw);
        }

        // 更新sku_attribute_value
        {
            List<Long> ids = skuInfo.getSkuAttributeValueList()
                    .stream()
                    .map(SkuAttributeValue::getId)
                    .collect(Collectors.toList());
            LambdaQueryWrapper<SkuAttributeValue> lqw = new LambdaQueryWrapper<>();
            lqw.eq(SkuAttributeValue::getSkuId, skuInfo.getId())
                    .notIn(SkuAttributeValue::getId, ids);
            skuAttributeValueMapper.delete(lqw);
        }
    }

    @Override
    public void delGoods(Long skuId) {
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
}
