package io.spridra.rpc.common.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 14:05
 * @Describe: 线程池工具类，在服务提供者一端执行异步任务
 * @Version: 1.0
 */

public class ServerThreadPool {
    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(
                16,16,600L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(65536));
    }

    public static void submit(Runnable task){
        threadPoolExecutor.submit(task);
    }

    public static void shutdown(){
        threadPoolExecutor.shutdown();
    }
}
