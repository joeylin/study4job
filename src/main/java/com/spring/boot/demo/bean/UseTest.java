package com.spring.boot.demo.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

/**
 * @author joe.ly
 * @date 2022/11/28
 */
@Component
public class UseTest {

    String name;

    @PostConstruct
    public void init() {
        String value = Optional.ofNullable("xxxxx").get();
        System.out.println(value);
    }

    public static void main(String[] args) {
        String value = Optional.ofNullable("xxxxx").get();
        System.out.println(value);

        List<String> arrays = new ArrayList<>();
        arrays.add("aaa");
        arrays.add("bbbb");
        List list = new ArrayList();
        list.add("cccc");
        list.add(1223);
        System.out.println(list.get(0));
        List<String> filteredName = arrays.stream().filter((name) -> name.contains("a")).collect(Collectors.toList());
        System.out.println(filteredName);

        List<String> aaa = new ArrayList<String>() {{add("aaaa");add("bbbb");}};
        String[] s = new String[]{"a","b"};

        UseTest use = new UseTest() {String name = "name";};

        List<String> testList = Arrays.asList("aaaa", "bbbb", "ccc");
        testList.add("ffff");
        System.out.println(testList.get(3));

    }
}
