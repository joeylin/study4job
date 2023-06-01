package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayList;
import java.util.List;
/**
 * @author joe.ly
 * @date 2023/2/9
 */
public class PartitionGpt {
    public static List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        backtraceForIp(0, s, new ArrayList<>(), res);
        return res;
    }
    public static void backtraceForIp(int level, String s, List<String> partitions, List<String> container) {
        if (level == 4 && s.length() == 0) {
            container.add(String.join(".", partitions));
        }
        if (level == 4 || s.length() == 0) {
            return;
        }
        int len = s.length();
        for (int i = 1; i < 4 && i <= len; i++) {
            String sub = s.substring(0, i);
            if (validNum(sub)) {
                partitions.add(sub);
                backtraceForIp(level + 1, s.substring(i), partitions, container);
                partitions.remove(partitions.size() - 1);
            }
        }
    }
    public static boolean validNum(String sub) {
        int num = Integer.parseInt(sub);
        if (num > 255 || num < 0) {
            return false;
        }
        if (sub.length() > 1 && "0".equals(sub.substring(0, 1))) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String s = "25525511135";
        List<String> res = restoreIpAddresses(s);
        System.out.println(res);
    }
}
