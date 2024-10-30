package com.tu.mall.service.impl;

import com.tu.mall.entity.AppReview;
import com.tu.mall.mapper.AppReviewMapper;
import com.tu.mall.service.IAppReviewService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author JiFeiYe
 * @since 2024/10/30
 */
@DubboService
public class AppReviewServiceImpl implements IAppReviewService {

    @Autowired
    private AppReviewMapper appReviewMapper;

    @Override
    public void setAppReview(AppReview appReview) {
        appReviewMapper.insert(appReview);
    }
}
