package com.tu.mall;

import com.tu.mall.entity.Attribute;
import com.tu.mall.entity.AttributeValue;
import com.tu.mall.mapper.AttributeMapper;
import com.tu.mall.mapper.AttributeValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.*;

/**
 * @author JiFeiYe
 * @since 2024/10/22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestTransaction {

    private final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
            5, 10,
            5, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(5)
    );

    @Autowired
    private AttributeMapper attributeMapper;
    @Autowired
    private AttributeValueMapper attributeValueMapper;

    @Test
    public void TTest1() {
        CompletableFuture<Void> c1 = CompletableFuture.runAsync(() -> {
            System.out.println("aa");

            Attribute attribute = new Attribute().setName("测试名字");
            attributeMapper.insert(attribute);

//            int a = 1 / 0;
        }, poolExecutor).exceptionally(ex -> {
            throw new RuntimeException(ex.getMessage());
        });
        System.out.println("bb");
        try {
            c1.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("手动处理异常");
        }
        System.out.println("out");
    }

    @Test
    @Transactional
    public void TTest2() {
        System.out.println("123456");
        CompletableFuture<Void> c1 = CompletableFuture.runAsync(() -> {
            System.out.println("aa");
            Attribute attribute = new Attribute().setName("测试1");
            attributeMapper.insert(attribute);
            int a = 1 / 0;
        }, poolExecutor).exceptionally(ex -> {
            throw new RuntimeException(ex.getMessage());
        });
        CompletableFuture<Void> c2 = CompletableFuture.runAsync(() -> {
            System.out.println("bb");
            Attribute attribute = new Attribute().setName("测试2");
            attributeMapper.insert(attribute);
//            int a = 1 / 0;
        }, poolExecutor).exceptionally(ex -> {
            throw new RuntimeException(ex.getMessage());
        });
        CompletableFuture<Void> c3 = CompletableFuture.runAsync(() -> {
            System.out.println("cc");
            AttributeValue attributeValue = new AttributeValue().setName("cs1");
            attributeValueMapper.insert(attributeValue);
//            int a = 1 / 0;
        }, poolExecutor).exceptionally(ex -> {
            throw new RuntimeException(ex.getMessage());
        });
        System.out.println("dd");
        try {
            CompletableFuture.allOf(c1, c2, c3);
        } catch (Exception e) {
            System.out.println("手动处理异常");
        }
        System.out.println("out");
    }
}
