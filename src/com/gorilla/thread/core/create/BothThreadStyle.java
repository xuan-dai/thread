package com.gorilla.thread.core.create;

/**
 * @author xuan
 * @date 2020/10/09
 **/
public class BothThreadStyle {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("this is runnable");
            }
        }) {
            @Override
            public void run() {
                System.out.println("this is thread");
            }
        }.start();
    }
}
