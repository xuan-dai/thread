package com.gorilla.thread.core.lock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 使用 AQS 实现一个共享锁
 *
 * @author xuan
 * @date 2020/10/13
 **/
public class OneShareLock {

    private final Sync sync = new Sync();

    public void signal() {
        sync.releaseShared(0);
    }

    public void await() {
        sync.acquireShared(0);
    }

    private class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected int tryAcquireShared(int arg) {
            return getState() == 1 ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1);

            return true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        OneShareLock lock = new OneShareLock();
        int no = 0;
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "开始等待");
                lock.await();
                System.out.println(Thread.currentThread().getName() + "开始执行");
            }, "线程" + no).start();
            no++;
        }
        Thread.sleep(5000);
        // 开闸
        lock.signal();
    }
}
