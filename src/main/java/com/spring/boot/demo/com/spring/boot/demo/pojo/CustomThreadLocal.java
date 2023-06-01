package com.spring.boot.demo.com.spring.boot.demo.pojo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author joe.ly
 * @date 2022/12/6
 */
public class CustomThreadLocal {
    private static final AtomicInteger nextHashCode = new AtomicInteger();
    private static final int hashCodeDelta = 0x61c88647;
    public final int hashCode = nextHashCode();
    private int nextHashCode() {
        return nextHashCode.addAndGet(hashCodeDelta);
    }

    public static void main(String[] args) {
        CustomThreadLocal threadLocal = new CustomThreadLocal();
        System.out.println(threadLocal.hashCode);
        CustomThreadLocal threadLocal1 = new CustomThreadLocal();
        System.out.println(threadLocal1.hashCode);
        CustomThreadLocal threadLocal2 = new CustomThreadLocal();
        System.out.println(threadLocal2.hashCode);
    }
}
