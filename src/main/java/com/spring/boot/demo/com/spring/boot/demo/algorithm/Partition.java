package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author joe.ly
 * @date 2023/2/1
 */
public class Partition {
    // "aab"：[["a","a","b"],["aa","b"]]
    public static List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        List<String> strList = new ArrayList<>();
        for (char c : s.toCharArray()) {
            strList.add(String.valueOf(c));
        }
        backtrace(strList, new ArrayList<>(), res);
        return res;
    }
    public static void backtrace(List<String> strList, List<String> partitions, List<List<String>> container) {
        if (strList.size() == 0) {
            container.add(new ArrayList<>(partitions));
            return;
        }

        for (int i = 1; i <= strList.size(); i++) {
            List<String> subList = strList.subList(0, i);
            if (!valid(subList)) {
                continue;
            }
            String subStr = subList.stream().reduce("", String::concat);
            partitions.add(subStr);
            backtrace(strList.subList(i, strList.size()), partitions, container);
            partitions.remove(subStr);
        }
    }
    public static boolean valid(List<String> strList) {
        int start = 0;
        int end = strList.size() - 1;
        while (end >= start) {
            if (!strList.get(end).equals(strList.get(start))) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }

    public static List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        backtraceForIp(1, s, new ArrayList<>(), res);
        return res;
    }
    public static void backtraceForIp(int level, String s, List<Integer> partitions, List<String> container) {
        if (level == 4) {
            if (validNum(s, Integer.valueOf(s))) {
                partitions.add(Integer.valueOf(s));
                String ip = partitions.stream().map(String::valueOf).collect(Collectors.joining("."));
                container.add(ip);
                partitions.remove(partitions.size() - 1);
            }
            return;
        }
        int len = s.length();
        for (int i = 0; i <= len && i < 4; i++) {
            String sub = s.substring(0, i + 1);
            Integer num = Integer.valueOf(sub);
            if (!validNum(sub, num)) {
                break;
            }
            partitions.add(num);
            backtraceForIp(level + 1, s.substring(i + 1), partitions, container);
            partitions.remove(partitions.size() - 1);
        }
    }
    public static boolean validNum(String sub, Integer num) {
        if (num > 255 || num < 0) {
            return false;
        }
        if (sub.length() > 1 && "0".equals(sub.substring(0, 1))) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        //String s = "aab";
        //List<List<String>> res = partition(s);
        //System.out.println(res);

        String s = "25525511135";
        List<String> res = restoreIpAddresses(s);
        System.out.println(res);
    }
}
