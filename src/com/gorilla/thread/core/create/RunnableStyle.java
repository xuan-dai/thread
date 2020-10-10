package com.gorilla.thread.core.create;

/**
 * 推荐使用方法
 *
 * @author xuan
 * @date 2020/10/09
 **/
public class RunnableStyle implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " -- runnable style thread");
    }

    public static void main(String[] args) {
        new Thread(new RunnableStyle()).start();
    }
}
