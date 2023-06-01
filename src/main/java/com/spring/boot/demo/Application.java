package com.spring.boot.demo;

import java.util.ServiceLoader;

import com.spring.boot.demo.bean.Robot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author joe.ly
 * @date 2022/10/17
 */
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("hello world!");
    }
}
