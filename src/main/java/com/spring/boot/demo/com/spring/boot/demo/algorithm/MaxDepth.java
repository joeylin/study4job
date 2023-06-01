package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.LinkedList;
import java.util.Queue;

import com.spring.boot.demo.com.spring.boot.demo.algorithm.LevelOrder.TreeNode;

/**
 * @author joe.ly
 * @date 2023/1/13
 */
public class MaxDepth {
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
    public int maxDepth(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int max = 0, min = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            max++;
            int len = queue.size();
            for (int i = 0; i < len; i++) {
                TreeNode node = queue.poll();
                if (node.right == null || node.left == null) {
                    min = Math.min(min, max);
                }
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
        }
        return max;
    }
    public int max = 0;
    public int min = Integer.MAX_VALUE;
    public void maxDepthFunc(int level, TreeNode node) {
        if (node == null) return;

        max = Math.max(max, level);
        min = Math.min(min, level);

        if (node.left != null) maxDepthFunc(level + 1, node.left);
        if (node.right != null) maxDepthFunc(level + 1, node.right);
    }
    public int maxDepthFunc1(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(maxDepthFunc1(node.left), maxDepthFunc1(node.right));
    }
    public int maxDepth1(TreeNode root) {
        maxDepthFunc(0, root);
        return max;
    }
}
