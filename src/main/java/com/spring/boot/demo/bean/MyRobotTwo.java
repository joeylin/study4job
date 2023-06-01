package com.spring.boot.demo.bean;

/**
 * @author joe.ly
 * @date 2023/5/25
 */
public class MyRobotTwo implements Robot {
    @Override
    public String sayHello() {
        System.out.println("I am MyRobotTwo");
        return null;
    }
}
