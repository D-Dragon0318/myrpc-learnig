package io.spridra.rpc.consumer.common.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-29 17:35
 * @Describe: 服务消费者线程池
 * @Version: 1.0
 */

public class ClientThreadPool {

    private static ThreadPoolExecutor threadPoolExecutor;

    static{
        threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
    }
    public static void submit(Runnable task){
        threadPoolExecutor.submit(task);
    }

    public static void shutdown() {
        threadPoolExecutor.shutdown();
    }
}
