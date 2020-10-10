package com.gorilla.thread.core.exception;

/**
 * @author xuan
 * @date 2020/10/10
 **/
public class MyUncaugthExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("捕获了 "+t.getName()+" 异常 " + e);
    }
}
