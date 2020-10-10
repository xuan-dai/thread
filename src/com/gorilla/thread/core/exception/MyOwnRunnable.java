package com.gorilla.thread.core.exception;

/**
 * @author xuan
 * @date 2020/10/10
 **/
public class MyOwnRunnable implements Runnable {
    @Override
    public void run() {
        method();
    }

    public void method (){
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaugthExceptionHandler());
        new Thread(new MyOwnRunnable(),"线程-1").start();
        new Thread(new MyOwnRunnable(),"线程-2").start();
        new Thread(new MyOwnRunnable(),"线程-3").start();
    }
}
