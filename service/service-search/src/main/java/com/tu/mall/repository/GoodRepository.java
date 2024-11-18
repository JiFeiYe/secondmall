package com.tu.mall.repository;

import com.tu.mall.entity.es.Good;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author JiFeiYe
 * @since 2024/10/17
 */
@Repository
public interface GoodRepository extends ElasticsearchRepository<Good, String> {
}
