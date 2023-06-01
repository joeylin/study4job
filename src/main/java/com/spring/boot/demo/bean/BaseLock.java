package com.spring.boot.demo.bean;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author joe.ly
 * @date 2022/12/21
 */
public class BaseLock {
    private AtomicBoolean guard = new AtomicBoolean(false);

    public void lock() {
        if (guard.compareAndSet(false, true)) {
            return;
        }
        while(true) {
            if (guard.compareAndSet(false, true)) {
                return;
            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void unlock() {
        guard.compareAndSet(true, false);
    }

    static class Increment {
        private int value = 0;
        private BaseLock lock = new BaseLock();

        public void safeInc() {
            lock.lock();
            value++;
            lock.unlock();
        }

        public void unsafeInc() {
            value++;
        }

        public int getValue() {
            return value;
        }

        public void unsafeIncBig(int max) {
            for (int i = 0; i < max; i++) {
                value++;
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        final Increment inc = new Increment();

        Thread thread1 = new Thread(() -> {
            inc.unsafeIncBig(1000);
        });
        Thread thread2 = new Thread(() -> {
            inc.unsafeIncBig(1000);
        });

        // run thread1 and thread2
        thread1.start();
        thread2.start();

        // wait for thread1 and thread2 to finish
        thread1.join();
        thread2.join();

        System.out.println(inc.getValue());

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";



    }
}
