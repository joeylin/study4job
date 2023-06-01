package com.spring.boot.demo.com.spring.boot.demo.impl;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Data;

/**
 * @author joe.ly
 * @date 2023/5/6
 */
public class CustomCache {
    public static void main(String[] args) throws Exception {
        LRUCache<String, String> lruCache = new LRUCache<String,String>(60);
        for (int i = 0; i < 60; i++) {
            String key = "aaaa" + i;
            lruCache.put(key, "bbbbb" + i, System.currentTimeMillis() + 1000 * 15);
            System.out.println(lruCache.get(key));
        }
        System.out.println("start to get value");
        for (int j = 0; j < 60; j++) {
            String key = "aaaa" + j;
            System.out.println(key + " " + lruCache.get(key));
        }

        lruCache.destroy();

        Callable<String> callable = () -> "null";
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> future = executorService.submit(callable);
    }
    private static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new IllegalArgumentException();
        }
        return reference;
    }
    static class Cache implements Comparable<Cache> {
        private Object key;
        private Object value;
        private long createdTime;
        private long lastAccessTime;
        private long writeTime;
        private long expireTime;
        private Integer hitCount = 0;

        @Override
        public int compareTo(Cache o) {
            return hitCount.compareTo(o.hitCount);
        }

        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public long getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(long createdTime) {
            this.createdTime = createdTime;
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }

        public void setLastAccessTime(long lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }

        public Integer getHitCount() {
            return hitCount;
        }

        public void setHitCount(Integer hitCount) {
            this.hitCount = hitCount;
        }

        public long getWriteTime() {
            return writeTime;
        }

        public void setWriteTime(long writeTime) {
            this.writeTime = writeTime;
        }
    }
    static class LRUCache<K, V> {
        private int size;
        private ConcurrentHashMap concurrentHashMap;
        private ScheduledExecutorService scheduledExecutorService;

        public LRUCache(LocalCacheBuilder builder) {
            this.size = builder.initialCapacity;
        }

        public LRUCache(int capacity) {
            this.size = capacity;
            this.concurrentHashMap = new ConcurrentHashMap<>(capacity);
            this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
            this.scheduledExecutorService.scheduleAtFixedRate(() -> expireKey(), 0, 10, TimeUnit.SECONDS);
        }

        public Object get(K key) {
            checkNotNull(key);
            if (concurrentHashMap.isEmpty()) return null;
            if (!concurrentHashMap.containsKey(key)) {
                System.out.println("not contains :" + key);
                return null;
            }

            Cache cache = (Cache)concurrentHashMap.get(key);
            if (cache == null) return null;
            if (cache.getExpireTime() - System.currentTimeMillis() <= 0) {
                System.out.println("cache expired: " + cache.getKey() + " " + cache.getExpireTime());
                return null;
            }

            cache.setHitCount(cache.getHitCount() + 1);
            cache.setLastAccessTime(System.currentTimeMillis());
            return cache.getValue();
        }

        public void put(K key, V value, long expire) {
            checkNotNull(key);
            checkNotNull(value);

            if (concurrentHashMap.containsKey(key)) {
                Cache oldCache = (Cache)concurrentHashMap.get(key);
                oldCache.setValue(value);
                oldCache.setExpireTime(expire);
                oldCache.setWriteTime(System.currentTimeMillis());
                oldCache.setLastAccessTime(System.currentTimeMillis());
                return;
            }
            if (isFull()) {
                Object kickedKey = getKickedKey();
                concurrentHashMap.remove(kickedKey);
            }
            Cache cache = new Cache();
            cache.setKey(key);
            cache.setValue(value);
            cache.setWriteTime(System.currentTimeMillis());
            cache.setExpireTime(expire);
            cache.setCreatedTime(System.currentTimeMillis());
            cache.setLastAccessTime(System.currentTimeMillis());
            concurrentHashMap.put(key, cache);
        }
        private boolean isFull() {
            return concurrentHashMap.size() >= size;
        }
        private Object getKickedKey() {
            Cache cache = (Cache)Collections.min(concurrentHashMap.values());
            return cache.getKey();
        }

        public void expireKey() {
            if (concurrentHashMap.isEmpty()) return;
            long current = System.currentTimeMillis();
            for (Object entry : concurrentHashMap.entrySet()) {
                Cache cache = (Cache)entry;
                if (cache.getExpireTime() - current <= 0) {
                    concurrentHashMap.remove(cache.getKey());
                }
            }
        }
        public void destroy() {
            this.scheduledExecutorService.shutdown();
        }
    }

    static class GuavaLocalCache<K, V> {
        public static void main(String[] args) throws ExecutionException {
            com.google.common.cache.Cache<String, String> localCache = CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(20)
                .expireAfterWrite(17, TimeUnit.SECONDS)
                .build();
            String key = "key";
            localCache.put(key, "v");
            localCache.get(key, () -> "aaaa");
            localCache.getIfPresent("aaaa");

            LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(20)
                .expireAfterWrite(17, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) {
                        return "aaaa";
                    }
                });
            loadingCache.get("aaaa");
        }
    }

    static class LocalCacheBuilder<K, V> {
        private int initialCapacity;
        private int maximumSize;
        private long expireAfterWrite;
        // 因为这里是静态方法，所以无法读到K V的泛型定义
        public static LocalCacheBuilder<Object, Object> newBuilder() {
            return new LocalCacheBuilder<>();
        }

        public LocalCacheBuilder initialCapacity(int value) {
            this.initialCapacity = value;
            return this;
        }
        public LocalCacheBuilder maximumSize(int value) {
            this.maximumSize = value;
            return this;
        }
        public LocalCacheBuilder expireAfterWrite(long value) {
            this.expireAfterWrite = value;
            return this;
        }

        public LRUCache<K, V> build() {
            // 这里泛型参数没有填写表示返回值来推断，这里的返回值为K,V，LRUCache会被自动填写如该参数
            return new LRUCache<>(this);
        }
    }
    @Data
    class CallableCache<K,V> {
        private int initialCapacity;
        private int maxSize;
        private Callable<V> loader;
        private long expireMillis;
        private ConcurrentHashMap<K, CacheEntry<K,V>> cacheMap;
        private ConcurrentHashMap<K, Object> locks;

        public CallableCache(CallableCacheBuild<K,V> builder) {
            this.initialCapacity = builder.getInitialCapacity();
            this.maxSize = builder.getMaxSize();
            this.loader = builder.getLoader();
            this.expireMillis = builder.getExpireMillis();
            this.cacheMap = new ConcurrentHashMap<>(this.initialCapacity);
            this.locks = new ConcurrentHashMap<>();
        }
        // 在读多写少的场景，自动清理机制会影响读性能，提供
        public void cleanUp() {
            long currentTime = System.currentTimeMillis();
            for (CacheEntry entry : cacheMap.values()) {
                if (entry != null && isExpire(entry, currentTime)) {
                    cacheMap.remove(entry.getKey());
                }
            }
        }
        // 本地缓存的场景，不用抛异常，
        public V get(K key) {
            CacheEntry<K, V> entry = cacheMap.get(key);
            long currentTime = System.currentTimeMillis();
            if (entry != null && isExpire(entry, currentTime)) {
                return loadValue(key);
            }
            entry.setLastAccessTime(currentTime);
            entry.getHitCount().incrementAndGet();
            return entry.getValue();
        }
        private boolean isExpire(CacheEntry<K,V> entry, long currentTime) {
            return currentTime - entry.getWriteTime() >= expireMillis;
        }
        private V loadValue(K key) {
            Object lock = locks.computeIfAbsent(key, (k) -> new Object());
            try {
                synchronized(lock) {
                    CacheEntry<K,V> entry = cacheMap.get(key);
                    long currentTime = System.currentTimeMillis();
                    if (entry == null || isExpire(entry, currentTime)) {
                        V newValue = loader.call();
                        if (newValue != null) {
                            if (isFull()) {
                                K kickedKey = getKickedKey();
                                cacheMap.remove(kickedKey);
                            }
                            cacheMap.put(key, new CacheEntry<>(key, newValue, currentTime));
                            return newValue;
                        }
                    } else {
                        entry.setLastAccessTime(currentTime);
                        entry.getHitCount().incrementAndGet();
                        return entry.getValue();
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return null;
        }
        private K getKickedKey() {
            CacheEntry<K,V> entry = Collections.min(cacheMap.values());
            return entry.getKey();
        }
        private boolean isFull() {
            return cacheMap.size() > maxSize;
        }
    }
    @Data
    class CallableCacheBuild<K,V> {
        private int initialCapacity;
        private int maxSize;
        private Callable<V> loader;
        private long expireMillis;
        public CallableCacheBuild<K,V> initialCapacity(int value) {
            this.initialCapacity = value;
            return this;
        }
        public CallableCacheBuild<K,V> maxSize(int value) {
            this.maxSize = value;
            return this;
        }
        public CallableCacheBuild<K,V> expireMillis(long value) {
            this.expireMillis = value;
            return this;
        }
        public CallableCache<K,V> build(Callable<V> loader) {
            this.loader = loader;
            return new CallableCache<>(this);
        }
    }
    @Data
    class CacheEntry<K,V> implements Comparable<CacheEntry> {
        private K key;
        private V value;
        private long lastAccessTime;
        private long writeTime;
        private AtomicInteger hitCount;

        public CacheEntry(K key, V value, long currentTime) {
            this.key = key;
            this.value = value;
            this.lastAccessTime = currentTime;
            this.writeTime = currentTime;
        }

        @Override
        public int compareTo(CacheEntry entry) {
            return this.hitCount.get() - entry.getHitCount().get();
        }
    }
}
