package com.gorilla.thread.core.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

/**
 * @author xuan
 * @date 2020/10/10
 **/
public class DiningPhilosophers {
    static class Philosopher implements Runnable {
        private final Object leftChopstick;
        private final Object rightChopstick;

        public Philosopher(Object leftChopstick, Object rightChopstick) {
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
        }

        public void action(String action) throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + " " + action);
            Thread.sleep((long) (Math.random() * 10));
        }

        @Override
        public void run() {
            while (true) {
                try {
                    action("thinking");
                    synchronized (leftChopstick) {
                        action("拿起左边的筷子");
                        synchronized (rightChopstick) {
                            action("拿起右边的筷子");
                            action("开始吃饭");
                            action("放下右边的筷子");
                        }
                        action("放下左边的筷子");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] chopsticks = new Object[philosophers.length];
        for (int i = 0; i < chopsticks.length; i++) {
            chopsticks[i] = new Object();
        }
        for (int i = 0; i < philosophers.length; i++) {
            Philosopher philosopher = new Philosopher(chopsticks[i], chopsticks[(i + 1) % chopsticks.length]);
            philosophers[i] = philosopher;
            new Thread(philosopher, "哲学家" + (i + 1) + "号").start();
        }

        TimeUnit.SECONDS.sleep(100);

        // 检测死锁的发生
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        if (deadlockedThreads != null && deadlockedThreads.length > 0) {
            for (int i = 0; i < deadlockedThreads.length; i++) {
                ThreadInfo threadInfo = threadMXBean.getThreadInfo(deadlockedThreads[i]);
                System.out.println("发生死锁 " + threadInfo.getThreadName());
            }
        }
    }
}
