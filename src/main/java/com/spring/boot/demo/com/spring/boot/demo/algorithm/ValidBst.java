package com.spring.boot.demo.com.spring.boot.demo.algorithm;

/**
 * @author joe.ly
 * @date 2023/1/8
 */
public class ValidBst {
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
    public boolean check(TreeNode curr, Integer min, Integer max) {
        if (curr == null) {
            return true;
        }
        if (curr.left != null) {
            if (curr.left.val > curr.val) {
                return false;
            }
            if (min != null && curr.left.val < min) {
                return false;
            }
        }
        if (curr.right != null) {
            if (curr.right.val < curr.val) {
                return false;
            }
            if (max != null && curr.right.val > max) {
                return false;
            }
        }
        return check(curr.left, min, curr.val) && check(curr.right, curr.val, max);
    }
    public boolean isValidBST(TreeNode root) {
        return check(root, null, null);
    }
    public static boolean validate(TreeNode node, int min, int max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;
        return validate(node.left, min, node.val) && validate(node.right, node.val, max);
    }
    public static boolean isValidBST1(TreeNode root) {
        return validate(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    static long pre = Long.MIN_VALUE;
    public static boolean isValidBST2(TreeNode root) {
        if (root == null) return true;
        // 访问左子树
        if (!isValidBST2(root.left)) return false;
        // 访问当前结点
        if (root.val <= pre) return false;
        pre = root.val;
        // isValidBST2
        if (!isValidBST2(root.right)) return false;
        // 均没问题 再返回true
        return true;
    }

    static long max = Long.MAX_VALUE;
    public static boolean isValidBST3(TreeNode root) {
        if (root == null) return true;
        if (!isValidBST3(root.right)) return false;
        if (root.val >= max) return false;
        max = root.val;
        if (!isValidBST3(root.left)) return false;
        return true;
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

        boolean result = ValidBst.isValidBST3(root);
        System.out.println(result);
    }
}
