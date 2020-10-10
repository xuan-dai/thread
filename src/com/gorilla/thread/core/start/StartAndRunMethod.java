package com.gorilla.thread.core.start;

/**
 * @author xuan
 * @date 2020/10/09
 **/
public class StartAndRunMethod {
    public static void main(String[] args) {
        // 由主线程执行
        Runnable runnable = () -> System.out.println(Thread.currentThread().getName());
        runnable.run();

        new Thread(runnable).run();

        // 由子线程执行
        // 同一线程不能两次调用 start 方法，线程状态 threadStatus
        // native start0()
        // 调用 start 方法并不一定是按顺序执行的
        new Thread(runnable).start();
    }
}
