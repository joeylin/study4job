package com.spring.boot.demo.bean;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson2.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author joe.ly
 * @date 2022/10/25
 */
@Component
public class CustomBean {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void test() {
        ConditionalBean bean = (ConditionalBean)applicationContext.getBean("conditionalBean");
        System.out.println("CustomBean by name");
        System.out.println(JSON.toJSONString(bean));

        ConditionalBean beanByType = applicationContext.getBean(ConditionalBean.class);
        System.out.println("CustomBean by type");
        System.out.println(JSON.toJSONString(beanByType));
    }
}
