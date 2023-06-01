package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.spring.boot.demo.com.spring.boot.demo.algorithm.ValidBst.TreeNode;

/**
 * @author joe.ly
 * @date 2023/1/9
 */
public class LowestCommonAncestor {
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

    static List<TreeNode> pathsForP = new ArrayList<>();
    static List<TreeNode> pathsForQ = new ArrayList<>();
    public static boolean findPath(TreeNode root, TreeNode target, List<TreeNode> container) {
        if (root == null) return false;

        container.add(root);
        if (root == target) return true;

        if (findPath(root.left, target, container)) {
            return true;
        }
        if (findPath(root.right, target, container)) {
            return true;
        }
        container.remove(container.size() - 1);
        return false;
    }

    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (findPath(root, p, pathsForP) && findPath(root, q, pathsForQ)) {
            for (int i = 0; i < Math.min(pathsForP.size(), pathsForQ.size()); i++) {
                if (pathsForP.get(i).val != pathsForQ.get(i).val) {
                    return pathsForP.get(i - 1);
                }
            }
            List<TreeNode> shorterPaths = pathsForP.size() > pathsForQ.size() ? pathsForQ : pathsForP;
            return shorterPaths.get(shorterPaths.size() - 1);
        }
        return null;
    }

    public static TreeNode lowestCommonAncestor1(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        if (root.val < p.val && root.val < q.val) {
            return lowestCommonAncestor1(root.right, p, q);
        } else if (root.val > p.val && root.val > q.val) {
            return lowestCommonAncestor1(root.left, p, q);
        } else {
            return root;
        }
    }

    public static TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        if (root == q || root == p) return root;

        TreeNode left = lowestCommonAncestor2(root.left, p, q);
        TreeNode right = lowestCommonAncestor2(root.right, p, q);

        if (left != null && right != null) {
            return root;
        } else if (left != null) {
            return left;
        } else if (right != null) {
            return right;
        }

        return null;
    }

    public static void breadthTreeRead(TreeNode root) {
        List<TreeNode> queue = new ArrayList<>();
        Set<TreeNode> visited = new HashSet<>();
        queue.add(root);
        visited.add(root);
        while (queue.size() != 0) {
            TreeNode node = queue.remove(0);
            if (node != null) {
                System.out.println(node.val);
                if (!visited.contains(node.left)) {
                    queue.add(node.left);
                    visited.add(node.left);
                }
                if (!visited.contains(node.right)) {
                    queue.add(node.right);
                    visited.add(node.right);
                }
            }
        }
    }
    public static void depthTreeNodeRead(TreeNode root) {
        if (root == null) return;
        
        System.out.println(root.val);
        if (root.left != null) {
            depthTreeNodeRead(root.left);
        }
        if (root.right != null) {
            depthTreeNodeRead(root.right);
        }
    }
    public static void depthTreeNodeRead1(TreeNode root) {

    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(0);
        root.left = new TreeNode(3);
        root.right = new TreeNode(8);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(6);
        root.right.right = new TreeNode(14);
        root.left.right.left = new TreeNode(4);
        root.left.right.right = new TreeNode(7);
        root.right.right.left = new TreeNode(13);

        depthTreeNodeRead(root);

        //TreeNode result = lowestCommonAncestor1(root, root.left, root.right);
        //System.out.println(result.val);
    }
}
