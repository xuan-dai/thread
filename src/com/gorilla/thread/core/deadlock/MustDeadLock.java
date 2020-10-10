package com.gorilla.thread.core.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 产生死锁的四个必要条件
 * 1. 互斥条件
 * 2. 请求与保持条件
 * 3. 不剥夺条件
 * 4. 循环等待条件
 * @author xuan
 * @date 2020/10/10
 **/
public class MustDeadLock implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        new Thread(new MustDeadLock(0)).start();
        new Thread(new MustDeadLock(1)).start();

        Thread.sleep(1000);

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        if (deadlockedThreads != null && deadlockedThreads.length != 0) {
            for (long deadlockedThread : deadlockedThreads) {
                ThreadInfo threadInfo = threadMXBean.getThreadInfo(deadlockedThread);
                System.out.println("发现死锁 " + threadInfo.getThreadName());
            }
        }
    }

    static Object object1 = new Object();
    static Object object2 = new Object();
    private int flag;

    public MustDeadLock(int flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        if (flag == 0) {
            synchronized (object1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (object2) {
                    System.out.println("线程1成功拿到两把锁");
                }
            }
        }

        if (flag == 1) {
            synchronized (object2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (object1) {
                    System.out.println("线程2成功拿到两把锁");
                }
            }
        }
    }

}
