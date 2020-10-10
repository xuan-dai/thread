package com.gorilla.thread.core.stop;

/**
 * 两种停止线程的生产最佳实践
 * 1. 传递中断，但程序仍在运行
 * 2. 恢复中断，程序停止
 * 线程存在一些无法响应中断的情况
 *
 * @author xuan
 * @date 2020/10/09
 **/
public class StopThread {
    // Java 线程是内核线程，由内核控制，状态的切换会照成比较大的性能消耗
    // 线程只在两种情况下停止 1. 任务执行完成 2. 线程程序执行异常
    // 使用 interrupt ，通过一个线程通知另外的线程停止，被通知的线程择机停止线程(除能响应interrupt方法外，线程需要实现收到停止通知后的操作)
    // 能响应 interrupt 的方法 sleep await join 等
    static class StopThreadWithoutSleep {
        public static void main(String[] args) throws Exception {
            Runnable runnable = () -> {
                int num = 0;
                // 只有线程添加了接收中断
                // while(num <= Integer.MAX_VALUE / 2) {
                while (!Thread.currentThread().isInterrupted() && num <= Integer.MAX_VALUE / 2) {
                    if (num % 10000 == 0) {
                        System.out.println(num + "是10000的倍数");
                    }
                    num++;
                }
                System.out.println("任务运行结束了");
            };

            Thread thread = new Thread(runnable);
            thread.start();
            Thread.sleep(1000);
            thread.interrupt();
        }
    }

    // 线程阻塞情况下的停止
    static class StopThreadWithSleep {
        public static void main(String[] args) throws Exception {
            Runnable runnable = () -> {
                try {
                    int num = 0;
                    // 只有线程添加了接收中断
                    while (!Thread.currentThread().isInterrupted() && num <= 10000) {
                        if (num % 100 == 0) {
                            System.out.println(num + "是100的倍数");
                        }
                        num++;
                    }
                    Thread.sleep(1000);
                    System.out.println("任务运行结束了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
            Thread.sleep(500);
            thread.interrupt();
        }

        static class StopThreadWithSleepEveryLoop {
            public static void main(String[] args) throws Exception {
                Runnable runnable = () -> {
                    try {
                        int num = 0;
                        // sleep 的过程中会自动响应 interrupt
                        while (num <= 100) {
                            if (num % 2 == 0) {
                                System.out.println(num + "是2的倍数");
                            }
                            Thread.sleep(10);
                            num++;
                        }
                        System.out.println("任务运行结束了");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();
                Thread.sleep(500);
                thread.interrupt();
            }
        }
    }

    // 传递中断
    static class StopThreadInProd {
        public static void main(String[] args) throws Exception {
            Runnable runnable = () -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        System.out.println("go");
                        throwInMethod();
                    } catch (InterruptedException e) {
                        // do something when get exception
                        System.out.println("线程接收到异常信息，do something");
                        e.printStackTrace();
                    }
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
            Thread.sleep(500);
            thread.interrupt();
        }

        private static void throwInMethod() throws InterruptedException {
            Thread.sleep(1000);
        }
    }

    // 恢复中断
    static class StopThreadInProd2 {
        public static void main(String[] args) throws Exception {
            Runnable runnable = () -> {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("线程中断");
                        break;
                    }
                    throwInMethod();
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
            Thread.sleep(500);
            thread.interrupt();
        }

        private static void throwInMethod() {
            try {
                System.out.println(System.currentTimeMillis());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 再次通知停止线程
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}
