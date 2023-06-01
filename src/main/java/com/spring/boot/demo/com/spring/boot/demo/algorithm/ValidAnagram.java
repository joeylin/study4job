package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author joe.ly
 * @date 2023/1/4
 */
public class ValidAnagram {
    public static boolean isAnagram(String s, String t) {
        char[] s1 = s.toCharArray();
        char[] t1 = t.toCharArray();
        Arrays.sort(s1);
        Arrays.sort(t1);

        return Arrays.toString(s1).equals(Arrays.toString(t1));
    }

    public static boolean isAnagram2(String s, String t) {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> t1 = new HashMap<>();

        for (char c : s.toCharArray()) {
            String a = String.valueOf(c);
            s1.put(a, s1.getOrDefault(a, 0) + 1);
        }
        for (char d: t.toCharArray()) {
            String b = String.valueOf(d);
            t1.put(b, t1.getOrDefault(b, 0) + 1);
        }

        // 比较两个map值是否相等
        return s1.equals(t1);
    }

    public static boolean isAnagram3(String s, String t) {
        int[] sCount = new int[26];
        int[] tCount = new int[26];

        for (char sc : s.toCharArray()) {
            sCount[sc - 'a']++;
        }
        for (char tc : t.toCharArray()) {
            tCount[tc - 'a']++;
        }
        return Arrays.equals(sCount, tCount);
    }

    public static void main(String[] args) {
        String s = "anagram";
        String t = "nagaram";

        boolean result = ValidAnagram.isAnagram3(s, t);
        System.out.println(result);
    }
}
