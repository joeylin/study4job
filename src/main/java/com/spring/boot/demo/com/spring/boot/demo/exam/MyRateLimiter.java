package com.spring.boot.demo.com.spring.boot.demo.exam;

import java.util.concurrent.TimeUnit;

import org.springframework.util.StopWatch;

/**
 * @author joe.ly
 * @date 2023/5/15
 */
public class MyRateLimiter {
    public static void main(String[] args) {
        MySmoothRateLimiter rateLimiter = MySmoothRateLimiter.create(5);

        long current = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            rateLimiter.acquire(3);
            System.out.println(System.currentTimeMillis() - current);
        }
        System.out.println("total used: ");
        System.out.println(System.currentTimeMillis() - current);
        String.valueOf(5);
    }
    static class MySmoothRateLimiter {
        private int maxPermits;
        private int storedPermits = 0;
        private long nextFreeTicketMillis = 0L;
        private long ticketIntervalMillis;

        private MyStopWatch stopWatch;

        public static MySmoothRateLimiter create(int addPermitsPerSecond) {
            return new MySmoothRateLimiter(addPermitsPerSecond, 5);
        }

        public MySmoothRateLimiter(int addPermitsPerSecond, int maxPermits) {
            this.ticketIntervalMillis = 1000 / addPermitsPerSecond;
            this.maxPermits = maxPermits;
            // 需要加id
            this.stopWatch = new MyStopWatch();
            this.stopWatch.start();
        }

        public long acquire(int permits) {
            checkNotNull(permits);
            long waitMillis = getWaitTime(permits);
            sleepMillisUninterrupted(waitMillis);
            return waitMillis;
        }
        private void sleepMillisUninterrupted(long waitMillis) {
            try {
                Thread.sleep(waitMillis);
            } catch (Exception e) {
                throw new RuntimeException("Sleep error");
            }
        }
        private long getWaitTime(int requirePermits) {
            long nowMillis = stopWatch.readMillis();
            synchronized (this) {
                reSync(nowMillis);
                long retValue = this.nextFreeTicketMillis;
                int storedPermitsToSpend = Math.min(storedPermits, requirePermits);
                long nextRequestMillis = (requirePermits - storedPermitsToSpend) * ticketIntervalMillis;
                this.storedPermits = storedPermits - storedPermitsToSpend;
                this.nextFreeTicketMillis += nextRequestMillis;
                return Math.max(retValue - nowMillis, 0);
            }
        }
        private void reSync(long nowMillis) {
            if (nowMillis > nextFreeTicketMillis) {
                long permits = (nowMillis - nextFreeTicketMillis) / ticketIntervalMillis;
                this.storedPermits = Math.min(maxPermits, (int)permits);
                this.nextFreeTicketMillis = nowMillis;
            }
        }
        public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
            long nowMillis = stopWatch.readMillis();
            if (!canAcquire(nowMillis, unit.toMillis(timeout))) {
                return false;
            } else {
                long waitMillis = getWaitTime(permits);
                sleepMillisUninterrupted(waitMillis);
                return true;
            }
        }
        public boolean canAcquire(long nowMillis, long timeoutMillis) {
            long waitMillis = Math.max(nowMillis, nextFreeTicketMillis) - nowMillis;
            return waitMillis < timeoutMillis;
        }
        private void checkNotNull(int permits) {
            if (permits <= 0) {
                throw new IllegalArgumentException("permits need be greater than 0, current: " + permits);
            }
        }
    }
    static class MyStopWatch {
        private long startTimeMillis;
        public void start() {
            this.startTimeMillis = System.currentTimeMillis();
        }
        public long readMillis() {
            return System.currentTimeMillis() - this.startTimeMillis;
        }
    }
}
