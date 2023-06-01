package com.spring.boot.demo.com.spring.boot.demo.exam;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Data;
import org.springframework.util.StopWatch;

/**
 * @author joe.ly
 * @date 2023/5/15
 */
public class MyLocalCache {
    public static void main(String[] args) throws Exception {
        LocalLoadingCache<String, String> loadingCache = LocalLoadingCacheBuilder.<String,String>newBuilder()
            .initialCapacity(10)
            .maxSize(20)
            .expireMillis(1000 * 30)
            .build((k) -> k);

        loadingCache.get("aaaa");
        loadingCache.get("bbbb");
        System.out.println(loadingCache.get("aaaa"));
        System.out.println(loadingCache.get("bbbb"));
    }
     static class LocalLoadingCache<K,V> {
        private int initialCapacity;
        private int maxSize;
        private long expireMillis;
        private ParamCallable<K,V> loader;

        private ConcurrentHashMap<K, CacheEntry<K,V>> cacheMap;
        private ConcurrentHashMap<K, Object> locks;

        public LocalLoadingCache(LocalLoadingCacheBuilder<K,V> builder) {
            this.initialCapacity = builder.initialCapacity;
            this.expireMillis = builder.expireMillis;
            this.maxSize = builder.maxSize;
            this.loader = builder.loader;

            this.cacheMap = new ConcurrentHashMap<>(this.initialCapacity);
            this.locks = new ConcurrentHashMap<>();
        }
        public V get(K key) {
            CacheEntry<K,V> entry = cacheMap.get(key);
            long current = System.currentTimeMillis();
            if (entry == null || isExpire(entry, current)) {
                return loadValue(key);
            }
            entry.setLastAccessTime(current);
            entry.getHitCount().incrementAndGet();
            return entry.getValue();
        }
        private boolean isFull() {
            return cacheMap.size() > maxSize;
        }
        private boolean isExpire(CacheEntry entry, long currentTime) {
            return entry.lastAccessTime - currentTime > expireMillis;
        }
        private K getKickedKey() {
            CacheEntry<K,V> entry = Collections.min(cacheMap.values());
            return entry.getKey();
        }
        private V loadValue(K key) {
            Object lock = locks.computeIfAbsent(key, (k) -> new Object());
            try {
                synchronized(lock) {
                    CacheEntry<K,V> entry = cacheMap.get(key);
                    long current = System.currentTimeMillis();
                    if (entry == null || isExpire(entry, current)) {
                        V newValue = loader.load(key);
                        if (newValue != null) {
                            if (isFull()) {
                                K kickedKey = getKickedKey();
                                cacheMap.remove(kickedKey);
                            }
                            cacheMap.put(key, new CacheEntry(key, newValue, current,
                                entry == null ? 0 : entry.getHitCount().get()));
                            return newValue;
                        }
                    } else {
                        return entry.getValue();
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return null;
        }
    }
    @Data
    static class CacheEntry<K,V> implements Comparable<CacheEntry> {
        private K key;
        private V value;
        private long lastAccessTime;
        private long writeTime;
        private AtomicInteger hitCount;

        public CacheEntry(K key, V value, long currentMillis, int hitCount) {
            this.key = key;
            this.value = value;
            this.lastAccessTime = currentMillis;
            this.writeTime = currentMillis;
            this.hitCount = new AtomicInteger(hitCount);
        }

        @Override
        public int compareTo(CacheEntry entry) {
            return this.hitCount.get() - entry.hitCount.get();
        }
    }
    static class LocalLoadingCacheBuilder<K,V> {
        private int initialCapacity;
        private int maxSize;
        private long expireMillis;
        private ParamCallable<K,V> loader;

        static <K,V> LocalLoadingCacheBuilder<K,V> newBuilder() {
            return new LocalLoadingCacheBuilder<>();
        }
        public LocalLoadingCacheBuilder<K,V> initialCapacity(int value) {
            this.initialCapacity = value;
            return this;
        }
        public LocalLoadingCacheBuilder<K,V> maxSize(int value) {
            this.maxSize = value;
            return this;
        }
        public LocalLoadingCacheBuilder<K,V> expireMillis(long value) {
            this.expireMillis = value;
            return this;
        }
        public LocalLoadingCache<K,V> build(ParamCallable<K,V> loader) {
            this.loader = loader;
            return new LocalLoadingCache<>(this);
        }
    }
     interface ParamCallable<K,V> {
        V load(K key);
    }
}
