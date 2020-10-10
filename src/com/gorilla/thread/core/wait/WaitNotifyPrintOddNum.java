package com.gorilla.thread.core.wait;

/**
 * 使用线程 wait & notify 打印 0-100 的奇偶数
 *
 * @author xuan
 * @date 2020/10/09
 **/
public class WaitNotifyPrintOddNum {

    private static int count = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(new TurningRunner()).start();

        new Thread(new TurningRunner()).start();
    }

    static class TurningRunner implements Runnable {

        @Override
        public void run() {
            while (count <= 100) {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + ":" + count++);

                    lock.notify();

                    if (count <= 100) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
