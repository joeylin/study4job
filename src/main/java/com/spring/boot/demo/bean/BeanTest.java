package com.spring.boot.demo.bean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author joe.ly
 * @date 2022/10/23
 */
@Component
public class BeanTest {
    @Resource
    private ApplicationContext applicationContext;

    @PostConstruct
    public void runTest() {
        ConditionalBean bean = (ConditionalBean)applicationContext.getBean("conditionalBean");
        // 判断bean是否为空
        if (bean == null) {
            System.out.println(bean.getProvider());
        } else {
            System.out.println("blank");
        }
    }
}
