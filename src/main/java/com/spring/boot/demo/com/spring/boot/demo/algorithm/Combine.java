package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author joe.ly
 * @date 2023/1/30
 */
public class Combine {
    public static List<List<Integer>> combine(int n, int k) {
        List<Integer> paths = new ArrayList<>();
        List<List<Integer>> res = new ArrayList<>();
        backtrace(paths, n, k, res);
        return res;
    }
    public static Map<String, Boolean> temp = new HashMap<>();
    public static void backtrace(List<Integer> paths, int n, int k, List<List<Integer>> container) {
        if (paths.size() == k) {
            // 2个参数，String::concat含义是：a.concat(b);
            // 1个参数，String::valueOf含义是：String.valueOf(a);
            String key = paths.stream().sorted().map(String::valueOf).reduce("", String::concat);
            if (!temp.getOrDefault(key, false)) {
                container.add(new ArrayList<>(paths));
                temp.put(key, true);
            }
            return;
        }
        for (int i = 1; i <= n; i++) {
            if (paths.contains(i)) {
                continue;
            }
            paths.add(i);
            backtrace(paths, n, k, container);
            paths.remove(paths.size() - 1);
        }
    }

    public static List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        backtraceSum3(k, n, new ArrayList<>(), res, new HashMap<>());
        return res;
    }
    public static void backtraceSum3(int k, int n, List<Integer> selected, List<List<Integer>> container, Map<String, Boolean> map) {
        int cur = selected.stream().reduce(0, Integer::sum);
        if (selected.size() == k) {
            String key = selected.stream().sorted().map(String::valueOf).reduce("", String::concat);
            if (cur == n && !map.getOrDefault(key, false)) {
                container.add(new ArrayList<>(selected));
                map.put(key, true);
            }
            return;
        }
        for (int i = 1; i < 9; i++) {
            if (selected.contains(i) || cur + i > n) {
                continue;
            }

            selected.add(i);
            backtraceSum3(k, n, selected, container, map);
            selected.remove((Integer)i);
        }
    }
    public static Map<Integer, String> map = new HashMap<>();
    static {
        map.put(2, "abc");
        map.put(3, "def");
        map.put(4, "ghi");
        map.put(5, "jkl");
        map.put(6, "mno");
        map.put(7, "pqrs");
        map.put(8, "tuv");
        map.put(9, "wxyz");
    }
    public static List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        List<Integer> digitsList = new ArrayList<>();
        for (char digit : digits.toCharArray()) {
            digitsList.add(Integer.parseInt(String.valueOf(digit)));
        }
        if (digits.equals("")) {
            return res;
        }
        backtraceForLetter(digitsList, "", res);
        return res;
    }
    public static void backtraceForLetter(List<Integer> digits, String letters, List<String> container) {
        if (digits.size() == 0) {
            container.add(letters);
            return;
        }
        Integer value = digits.remove(0);
        String options = map.get(value);
        for (char option : options.toCharArray()) {
            backtraceForLetter(new ArrayList<>(digits), letters + option, container);
        }
    }

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> res = new ArrayList<>();
        backtraceForSum(candidates, new ArrayList<>(), 0, target, res);
        return res;
    }
    public static Map<String, Boolean> sumMap = new HashMap<>();
    public static void backtraceForSum(int[] candidates, List<Integer> selected, int sum, int target, List<List<Integer>> container) {
        if (sum == target) {
            String key = selected.stream().sorted().map(String::valueOf).reduce("", String::concat);
            if (!sumMap.getOrDefault(key, false)) {
                container.add(new ArrayList<>(selected));
                sumMap.put(key, true);
            }
            return;
        }

        for (int i = 0; i < candidates.length; i++) {
            int candidate = candidates[i];
            if (sum + candidate > target) {
                break;
            }

            selected.add(candidate);
            backtraceForSum(candidates, selected, sum + candidate, target, container);
            selected.remove((Integer)candidate);
        }
    }

    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<Integer> candidatesList = new ArrayList<>();
        for (int i = 0; i < candidates.length; i++) {
            candidatesList.add(candidates[i]);
        }
        List<List<Integer>> res = new ArrayList<>();
        backtraceForSum2(candidatesList, new ArrayList<>(), 0, target, res);
        return res;
    }
    public static void backtraceForSum2(List<Integer> candidates, List<Integer> selected, int sum, int target, List<List<Integer>> container) {
        if (sum == target) {
            String key = selected.stream().sorted().map(String::valueOf).reduce("", String::concat);
            if (!sumMap.getOrDefault(key, false)) {
                container.add(new ArrayList<>(selected));
                sumMap.put(key, true);
            }
            return;
        }
        if (candidates.size() == 0) {
            return;
        }
        for (int i = 0; i < candidates.size(); i++) {
            int candidate = candidates.get(i);
            if (sum + candidate > target) {
                break;
            }
            if (i > 0 && candidates.get(i) == candidates.get(i - 1)) {
                continue;
            }
            selected.add(candidate);
            List<Integer> newCandidates = new ArrayList<>(candidates);
            newCandidates.remove(i);
            backtraceForSum2(newCandidates, selected, sum + candidate, target, container);
            selected.remove((Integer)candidate);
        }
    }

    public static void main(String[] args) {
        //List<List<Integer>> res = combine(4,2);
        //System.out.println(res);

        //List<List<Integer>> res1 = combinationSum3(4, 1);
        //System.out.println(res1);

        //List<String> res2 = letterCombinations("23");
        //System.out.println(res2);

        //int[] candidates = new int[] {2,3,6,7};
        //List<List<Integer>> res = combinationSum(candidates, 7);
        //System.out.println(res);

        int[] candidates = new int[] {10,1,2,7,6,1,5};
        List<List<Integer>> res = combinationSum2(candidates, 8);
        System.out.println(res);
    }
}
