package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author joe.ly
 * @date 2022/12/29
 */
public class MyStack {
    private Queue<Integer> queue = new LinkedList<>();
    private int topItem;
    public MyStack() {};
    public void push(int x) {
        queue.offer(x);
        topItem = x;
    }
    public int pop() {
        int size = queue.size();
        while (size > 2) {
            queue.add(queue.poll());
            size--;
        }
        queue.peek();
        topItem = queue.poll();
        queue.add(topItem);
        return queue.poll();
    }
    public int top() {
        return topItem;
    }
    public boolean empty() {
        return queue.isEmpty();
    }
}
