package com.gorilla.thread.core.cache;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author xuan
 * @date 2020/10/13
 **/
public class MyCache<K, V> {
    // final 使得缓存在内存中被引用的地址不变
    // ConcurrentHashMap 保证线程安全
    private final Map<K, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<K, V> c;

    public MyCache(Computable<K, V> c) {
        this.c = c;
    }

    public V compute(K k) throws InterruptedException, ExecutionException {
        Future<V> f = cache.get(k);
        if (f == null) {
            // 耗时的场景，在多线程的情况下，避免会导致重复的计算
            Callable<V> callable = () -> c.computer(k);

            FutureTask<V> ft = new FutureTask<>(callable);

            // 原子性操作
            f = cache.putIfAbsent(k, ft);
            if (f == null) {
                f = ft;
                System.out.println("从FutureTask调用了计算函数");
                ft.run();
            }
        }

        return f.get();
    }

    public final static ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

    // 可过期缓存
    public V compute(K k, long seconds) throws InterruptedException, ExecutionException {
        if (seconds > 0) {
            executor.schedule(() -> {
                expire(k);
            }, seconds, TimeUnit.SECONDS);
        }
        return compute(k);
    }

    // 防止缓存雪崩
    public V computeRandomExpire(K k) throws ExecutionException, InterruptedException {
        long randomExpire = (long) (Math.random() * 10000);
        return compute(k, randomExpire);
    }

    private synchronized void expire(K k) {
        Future<V> future = cache.get(k);
        if (future != null) {
            if (!future.isDone()) {
                System.out.println("Future任务被取消");
                future.cancel(true);
            }
            System.out.println("过期时间到，缓存被清除");
            cache.remove(k);
        }
    }
}

class ExpensiveFunction implements Computable<String, Integer> {
    public Integer computer(String userId) throws Exception {
        TimeUnit.SECONDS.sleep(5);
        return Integer.valueOf(userId);
    }
}

interface Computable<K, V> {
    V computer(K k) throws Exception;
}

class Main {
    public static void main(String[] args) {
        MyCache<String, Integer> myCache = new MyCache<>(new ExpensiveFunction());
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService service = Executors.newFixedThreadPool(300);

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            service.submit(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "开始等待");
                    latch.await();

                    System.out.println(Thread.currentThread().getName() + "时间：" + System.currentTimeMillis() + "开始执行");
                    System.out.println(myCache.compute("123"));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }

        latch.countDown();
        service.shutdown();
        // 自旋等待线程池执行完毕
        while (!service.isTerminated()) {

        }
        System.out.println("总耗时：" + (System.currentTimeMillis() - begin));
    }
}