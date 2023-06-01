package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author joe.ly
 * @date 2023/1/15
 */
public class GenerateParenthesis {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    public static void gen(int left, int right, int n, String result, List<String> container) {
        if (left == n && right == n) {
            container.add(result);
            return;
        }
        if (left < n) {
            gen(left + 1, right, n, result + "(", container);
        }
        if (left > right && right < n) {
            gen(left, right + 1, n,result + ")", container);
        }
    }
    public static List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        gen(0, 0, n, "", res);
        return res;
    }
    public static void main(String[] args) {
        List<String> res = generateParenthesis(3);
        for (String item : res) {
            System.out.println(item);
        }

        List<Integer> selects = new ArrayList<>();
        selects.add(1);
        selects.add(2);
        selects.add(3);
        List<List<Integer>> result = new ArrayList<>();
        backTrace(new ArrayList<>(), selects, result);
        for (List<Integer> paths : result) {
            System.out.println(paths.toString());
        }
    }

    public static void backTrace(List<Integer> paths, List<Integer> selects, List<List<Integer>> container) {
        if (selects == null || selects.size() == 0) {
            container.add(new ArrayList<>(paths));
            return;
        }
        for (Integer select : selects) {
            paths.add(select);
            List<Integer> newSelects = new ArrayList<>(selects);
            newSelects.remove(select);
            backTrace(paths, newSelects, container);
            paths.remove(select);
        }
    }

}
