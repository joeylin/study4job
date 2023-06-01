package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author joe.ly
 * @date 2023/1/3
 */
public class MaxSlidingWindow {
    static final class MyQueue {
        private Deque<Integer> que = new ArrayDeque<>(); // 使用deque来实现单调队列
        // 每次弹出的时候，比较当前要弹出的数值是否等于队列出口元素的数值，如果相等则弹出。
        // 同时pop之前判断队列当前是否为空。
        public void pop(int value) {
            if (!que.isEmpty() && value == que.getFirst()) {
                que.removeFirst();
            }
        }
        // 如果push的数值大于入口元素的数值，那么就将队列后端的数值弹出，直到push的数值小于等于队列入口元素的数值为止。
        // 这样就保持了队列里的数值是单调从大到小的了。
        public void push(int value) {
            while (!que.isEmpty() && value > que.getLast()) {
                que.removeLast();
            }
            que.addLast(value);
        }
        // 查询当前队列里的最大值 直接返回队列前端也就是front就可以了。
        private int front() {
            return que.getFirst();
        }
    }

    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length == 0) {
            return new int[0];
        }
        MyQueue que = new MyQueue();
        int len = nums.length - k + 1;
        int count = 0;
        for (int i = 0; i < k; i++) {
            que.push(nums[i]);
        }

        int[] result = new int[len];
        result[count] = que.front();
        count++;

        for (int i = k; i < nums.length; i++) {
            que.pop(nums[i - k]);
            que.push(nums[i]);
            result[count++] = que.front();
        }
        return result;
    }

    public static int[] maxSlidingWindow2(int[] nums, int k) {
        if (nums.length == 0) {
            return new int[0];
        }
        Deque<Integer> win = new ArrayDeque<>();
        int[] res = new int[nums.length - k + 1];
        for (int i = 0; i < nums.length; i++) {
            if (i >= k && win.getFirst() == i - k ) {
                win.removeFirst();
            }
            while (!win.isEmpty() && nums[win.getLast()] < nums[i]) {
                win.removeLast();
            }
            win.addLast(i);
            if (i >= k - 1) {
                res[i - k + 1] = nums[win.getFirst()];
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {-7,-8,7,5,7,1,6,0};
        int k = 4;

        int[] res = maxSlidingWindow2(nums, k);
        for (int i = 0; i < res.length; i++) {
            System.out.println(res[i]);
        }
    }
}
