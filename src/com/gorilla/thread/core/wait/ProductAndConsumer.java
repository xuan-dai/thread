package com.gorilla.thread.core.wait;

import java.util.LinkedList;
import java.util.Random;

/**
 * @author xuan
 * @date 2020/10/09
 **/
public class ProductAndConsumer {

    public static void main(String[] args) {
        Storage storage = new Storage();
        new Thread(new Producer(storage)).start();
        new Thread(new Consumer(storage)).start();
    }

    static class Producer implements Runnable {

        private Storage storage;

        public Producer(Storage storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            while (true) {
                storage.put();
            }
        }
    }

    static class Consumer implements Runnable {

        private Storage storage;

        public Consumer(Storage storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            while (true) {
                storage.take();
            }
        }
    }

    static class Storage {

        private LinkedList<Integer> storage;
        private int capacity;

        public Storage() {
            this.capacity = 10;
            this.storage = new LinkedList<>();
        }

        public synchronized void put() {
            if (storage.size() < capacity) {
                storage.add(new Random().nextInt(10));
                System.out.println("仓库里有了" + storage.size() + "个产品。");
                notify();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public synchronized void take() {
            if (storage.size() > 0) {
                System.out.println("拿到了" + storage.poll() + "，现在仓库还剩下" + storage.size());
                notify();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
