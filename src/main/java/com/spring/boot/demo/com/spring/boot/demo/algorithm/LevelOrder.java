package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.spring.boot.demo.com.spring.boot.demo.algorithm.ValidBst.TreeNode;

/**
 * @author joe.ly
 * @date 2023/1/12
 */
public class LevelOrder {
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
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<TreeNode> queue = new ArrayList<>();
        TreeNode levelFlag = new TreeNode(0);
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> levelArray = new ArrayList<>();

        queue.add(root);
        queue.add(levelFlag);
        while (queue.size() != 0) {
            TreeNode node = queue.remove(0);
            if (node == levelFlag) {
                res.add(levelArray);
                if (queue.size() != 0) {
                    levelArray = new ArrayList<>();
                    queue.add(levelFlag);
                }
            } else {
                levelArray.add(node.val);

                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
        }
        return res;
    }

    public static List<List<Integer>> levelOrder1(TreeNode root) {
        // BSF batch process
        List<List<Integer>> res = new ArrayList<>();
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.addLast(root);
        while (!queue.isEmpty()) {
            int len = queue.size();
            List<Integer> levelArray = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                TreeNode node = queue.pollFirst();
                levelArray.add(node.val);
                if (node.left != null) queue.addLast(node.left);
                if (node.right != null) queue.addLast(node.right);
            }
            res.add(levelArray);
        }
        return res;
    }

    public static void levelOrderFunc(int level, TreeNode node, List<List<Integer>> container) {
        if (node == null) return;
        if (level >= container.size()) {
            container.add(new ArrayList<>());
        }
        List<Integer> levelArray = container.get(level);
        levelArray.add(node.val);

        if (node.left != null) {
            levelOrderFunc(level + 1, node.left, container);
        }
        if (node.right != null) {
            levelOrderFunc(level + 1, node.right, container);
        }
    }
    public static List<List<Integer>> levelOrder2(TreeNode root) {
        // DFS
        List<List<Integer>> res = new ArrayList<>();
        levelOrderFunc(0, root, res);
        return res;
    }

    public static List<List<Integer>> levelOrder3(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode levelFlag = new TreeNode(0);
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> levelArray = new ArrayList<>();

        queue.add(root);
        queue.add(levelFlag);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node == levelFlag) {
                res.add(levelArray);
                levelArray = new ArrayList<>();
                if (!queue.isEmpty()) {
                    queue.add(levelFlag);
                }
            } else {
                levelArray.add(node.val);
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(8);
        root.left = new TreeNode(3);
        root.right = new TreeNode(10);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(6);
        root.right.right = new TreeNode(14);
        root.left.right.left = new TreeNode(4);
        root.left.right.right = new TreeNode(7);
        root.right.right.left = new TreeNode(13);

        List<List<Integer>> res = levelOrder3(root);
        for (List<Integer> nums : res) {
            System.out.println("start new level");
            for (Integer num : nums) {
                System.out.println(num);
            }
        }
    }
}
