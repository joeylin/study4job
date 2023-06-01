package com.spring.boot.demo.com.spring.boot.demo.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.common.util.concurrent.RateLimiter;

/**
 * @author joe.ly
 * @date 2023/5/6
 */
public class CustomRateLimiter {
    public static void main(String[] args) {
        // todo: 令牌桶思路
        SimpleRateLimiterV2 simple = new SimpleRateLimiterV2();
        for (int i = 0; i < 100; i++) {
            boolean result = simple.canInvoke();
            System.out.println(result);
        }
        int success = 0;
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(100l);
            } catch (Exception e) {}
            if (simple.canInvoke()) {
                success++;
            }
        }
        System.out.println(success);
    }

    static class SimpleRateLimiter {
        public static void main(String[] args) {
            // 问题1：无界队列可能造成内存泄漏
            // 问题2：每次调用都遍历队列，性能较差
            // 问题3：并发问题导致总数大与上限，不是线程安全
        }
        private static final int TIME_WINDOW = 1; // 10分钟窗口
        private static final int MAX_CALLS = 2; // 最大调用次数
        Queue<LocalDateTime> callTimestamps = new LinkedList<>();
        synchronized public boolean canInvoke() {
            LocalDateTime now = LocalDateTime.now();
            // todo: 这里只能用while，否则会导致队列内过期数据清理过慢（因为并发加入会造成超过MAX_CALLS）
            while(!callTimestamps.isEmpty() && ChronoUnit.MINUTES.between(callTimestamps.peek(),now) > TIME_WINDOW) {
                callTimestamps.poll();
            }
            if (callTimestamps.size() >= MAX_CALLS) {
                return false;
            }

            callTimestamps.add(now);
            return true;
        }
    }
    static class SimpleRateLimiterV1 {
        private static final int TIME_WINDOW_SECOND = 10;
        private static final int MAX_CALL_PER_TICK = 2;
        private static AtomicInteger callCount = new AtomicInteger(0);
        private static long lastTriggerTime = System.currentTimeMillis();

        public boolean canInvoke() {
            // todo: 并发问题，在更新的瞬间会导致并发的数据量没有被统计到
            // todo: 这个实现是固定窗口，不能保证任意的时间距离内的qps都小于MAX_CALL_PER_TICK
            if (System.currentTimeMillis() - lastTriggerTime > TIME_WINDOW_SECOND * 1000) {
                callCount.set(0);
                lastTriggerTime = System.currentTimeMillis();
            }
            return callCount.incrementAndGet() <= MAX_CALL_PER_TICK;
        }
    }
    static class SimpleRateLimiterV2 {
        private static final int MAX_CALL = 10;
        private static final int TIME_WINDOW = 1000;
        private static final int SUB_WINDOW_SIZE = 10;
        private static SubWindow[] subWindows = new SubWindow[SUB_WINDOW_SIZE];

        static {
            long currentTimestamp = System.currentTimeMillis();
            for (int i = 0; i < SUB_WINDOW_SIZE; i++) {
                subWindows[i] = new SubWindow(currentTimestamp, 0);
            }
        }

        public boolean canInvoke() {
            long currentTimestamps = System.currentTimeMillis();
            int currentIndex = (int)currentTimestamps % SUB_WINDOW_SIZE / (TIME_WINDOW / SUB_WINDOW_SIZE);
            int sum = 0;
            for (int i = 0; i < subWindows.length; i++) {
                SubWindow subWindow = subWindows[i];
                if (currentTimestamps - subWindow.getTime() >= TIME_WINDOW) {
                    subWindow.setTime(currentTimestamps);
                    subWindow.setNumber(0);
                }
                if (currentIndex == i && currentTimestamps - subWindow.getTime() < TIME_WINDOW) {
                    subWindow.setNumber(subWindow.getNumber() + 1);
                }
                sum += subWindow.getNumber();
            }
            return sum < MAX_CALL;
        }

        static class SubWindow {
            long time;
            int number;

            public SubWindow(long time, int number) {
                this.time = time;
                this.number = number;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }
        }
    }

    static class LeakBucketRateLimiter {
        // 问题1：该算法本身不支持突发流量（添加满的时候直接丢弃）
        // 问题2：水漏得不均衡，
        private static final int FLOW_RATE = 2;
        private static final int CAPACITY = 10;
        private static AtomicInteger water = new AtomicInteger(0);
        private static long lastCallTimestamp = System.currentTimeMillis();

        public boolean canInvoke() {
            if (water.get() == 0) {
                lastCallTimestamp = System.currentTimeMillis();
                water.addAndGet(1);
                return true;
            }

            long current = System.currentTimeMillis();
            int waterLeft = water.get() - (int)((current - lastCallTimestamp) / 1000) * FLOW_RATE;
            water.set(Math.max(0, waterLeft));
            lastCallTimestamp = current;

            if (water.get() < CAPACITY) {
                water.addAndGet(1);
                return true;
            } else {
                return false;
            }
        }


    }
    static class ArrayRateLimiter {
        private static final int MAX_COUNT = 10;
        private static final int TIME_WINDOW_MINUTE = 1;
        private static AtomicInteger startIndex = new AtomicInteger(0);
        private static AtomicInteger endIndex = new AtomicInteger(0);
        private static AtomicInteger callCount = new AtomicInteger(0);
        private static LocalDateTime[] timestamps = new LocalDateTime[MAX_COUNT];

        public boolean canInvoke() {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime oldestTimestamps = timestamps[startIndex.get() % MAX_COUNT];

            if (oldestTimestamps != null && ChronoUnit.MINUTES.between(oldestTimestamps, now) > TIME_WINDOW_MINUTE) {
                startIndex.getAndIncrement();
                callCount.decrementAndGet();
            }
            if (callCount.get() > MAX_COUNT) {
                return false;
            }
            timestamps[endIndex.getAndIncrement() % MAX_COUNT] = now;
            callCount.getAndIncrement();
            return true;
        }
    }
    static class TokenBucketRateLimiter {
        private static final int BUCKET_CAPACITY = 60;
        private static final int TOKEN_PER_SECOND = 10;
        private static final int MAX_RETRIES = 10;
        private static AtomicInteger tokenBucket = new AtomicInteger(BUCKET_CAPACITY);
        private static ScheduledExecutorService tokenRefillScheduler = Executors.newScheduledThreadPool(1);

        static {
            tokenRefillScheduler.scheduleAtFixedRate(() -> {
                tokenBucket.updateAndGet(prev -> Math.min(BUCKET_CAPACITY, prev + TOKEN_PER_SECOND));
            }, 0, 1, TimeUnit.SECONDS);
        }
        public boolean canInvoke() {
            if (tokenBucket.get() > 0) {
                int currentReties = MAX_RETRIES;
                while (currentReties > 0) {
                    currentReties--;
                    if (tokenBucket.get() <= 0) {
                        return false;
                    }
                    if (tokenBucket.compareAndSet(tokenBucket.get(), tokenBucket.get() - 1)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    static class GuavaRateLimiter {
        public static final RateLimiter rateLimiter = RateLimiter.create(10);
        public boolean canInvoke() {
            if (rateLimiter.tryAcquire()) {
                return true;
            }
            return false;
        }
    }
}
