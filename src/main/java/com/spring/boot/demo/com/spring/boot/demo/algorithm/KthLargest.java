package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.spring.boot.demo.com.spring.boot.demo.algorithm.ReverseListContainer.ListNode;

/**
 * @author joe.ly
 * @date 2022/12/29
 */
public class KthLargest {
    private PriorityQueue<Integer> queue;
    private int k;
    public KthLargest(int k, int[] nums) {
        this.queue = new PriorityQueue<>(k);
        this.k = k;
        for (int num : nums) {
            queue.add(num);
        }
        while (this.queue.size() > k) {
            this.queue.poll();
        }
    }

    public void add(int e) {
        if (this.queue.size() < this.k) {
            this.queue.add(e);
        }

        int min = this.queue.peek();
        if (min > e) {
            return;
        }
        this.queue.poll();
        this.queue.add(e);
    }

    public static void main(String[] args) {
        PriorityQueue<Integer> queue = new PriorityQueue<>(5);
        queue.add(2);
        queue.add(3);
        queue.add(1);
        queue.add(5);

        for (int i = 0; i < queue.size(); i++) {
            System.out.println(queue.poll());
        }

        PriorityQueue<ListNode> q = new PriorityQueue<>(5, (a, b) -> b.val - a.val);
    }
}
