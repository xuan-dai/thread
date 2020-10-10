package com.gorilla.thread.core.wait;

/**
 * wait 需要先持有锁，调用会释放当前 monitor 锁
 * notify 需要持有锁，再随机唤醒一个该对象的 wait 调用方
 * notifyAll 需要持有锁，再随机唤醒一个该对象的 wait 调用方
 *
 * @author xuan
 * @date 2020/10/09
 **/
public class WaitAndNotifyAndNotifyAll {
    static class WaitAndNotify {
        public static void main(String[] args) throws InterruptedException {
            Object obj = new Object();
            Runnable runnable1 = () -> {
                synchronized (obj) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " 执行等待");
                        obj.wait();
                        System.out.println(Thread.currentThread().getName() + " 被唤醒");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            Runnable runnable2 = () -> {
                synchronized (obj) {
                    System.out.println(Thread.currentThread().getName() + " 执行 notify");
                    obj.notify();
                }
            };

            new Thread(runnable1).start();
            // 保证 wait 在 notify 之前执行，多线程运行的执行顺序问题，先 start 不代表先执行
            Thread.sleep(50);
            new Thread(runnable2).start();
        }
    }

    static class WaitAndNotifyAll {
        public static void main(String[] args) throws InterruptedException {
            Object obj = new Object();
            Runnable runnable1 = () -> {
                synchronized (obj) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " 执行等待");
                        obj.wait();
                        System.out.println(Thread.currentThread().getName() + " 被唤醒");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            Runnable runnable2 = () -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " 执行等待");
                    obj.wait();
                    System.out.println(Thread.currentThread().getName() + " 被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            Runnable runnable3 = () -> {
                synchronized (obj) {
                    System.out.println(Thread.currentThread().getName() + " 执行 notify");
                    obj.notifyAll();
                }
            };

            new Thread(runnable1).start();
            new Thread(runnable2).start();
            // 多线程运行的执行顺序问题
            Thread.sleep(50);
            new Thread(runnable3).start();
        }
    }
}
