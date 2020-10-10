package com.gorilla.thread.core.create;

/**
 * @author xuan
 * @date 2020/10/09
 **/
public class ThreadStyle extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " -- thread style thread");
    }

    public static void main(String[] args) {
        new ThreadStyle().start();
    }
}
