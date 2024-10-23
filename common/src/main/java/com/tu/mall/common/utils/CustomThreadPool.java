package com.tu.mall.common.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author JiFeiYe
 * @since 2024/10/23
 */
public class CustomThreadPool {

    private static volatile ThreadPoolExecutor poolExecutor; // 禁止指令重排序

    private CustomThreadPool() {
    }

    public static ThreadPoolExecutor getInstance() {
        if (poolExecutor == null) {
            synchronized (CustomThreadPool.class) {
                if (poolExecutor == null) {
                    poolExecutor = new ThreadPoolExecutor(
                            5, 10,
                            5, TimeUnit.MINUTES,
                            new ArrayBlockingQueue<>(10)
                    );
                }
            }
        }
        return poolExecutor;
    }

    public static void submit(Runnable task) {
        getInstance().submit(task);
    }

    public static void shutdown() {
        getInstance().shutdown();
    }
}
