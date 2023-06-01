package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.spring.boot.demo.com.spring.boot.demo.algorithm.LowestCommonAncestor.TreeNode;

/**
 * @author joe.ly
 * @date 2023/1/15
 */
public class TreeSearch {
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
    public void dfs(TreeNode node) {
        // 深度遍历，实际上是前序遍历
        if (node == null) return;
        System.out.println(node.val);

        if (node.left != null) dfs(node.left);
        if (node.right != null) dfs(node.right);
    }
    public void dfs1(TreeNode root) {
        Stack<TreeNode> stack= new Stack<>();
        stack.push(root);
        while (!stack.empty()) {
            TreeNode node = stack.pop();
            System.out.println(node.val);

            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }
    }
    public void bfs(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.println(node.val);

            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }

    }
}
