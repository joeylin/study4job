package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.Stack;

/**
 * @author joe.ly
 * @date 2022/12/29
 */
public class MyQueue {
    public MyQueue() {}

    private Stack<Integer> inputStack = new Stack<>();
    private Stack<Integer> outputStack = new Stack<>();


    public void push(int x) {
        inputStack.push(x);
    }
    public int pop() {
        if (outputStack.empty()) {
            while(!inputStack.empty()) {
                outputStack.push(inputStack.pop());
            }
        }
        return outputStack.pop();
    }
    public int peek() {
        if (outputStack.empty()) {
            while(!inputStack.empty()) {
                outputStack.push(inputStack.pop());
            }
        }
        return outputStack.peek();
    }
    public boolean empty() {
        return inputStack.empty() && outputStack.empty();
    }
}
