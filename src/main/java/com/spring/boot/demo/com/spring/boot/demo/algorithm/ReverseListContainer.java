package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author joe.ly
 * @date 2022/12/26
 */
public class ReverseListContainer {
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    public static ListNode reverseList(ListNode head) {
        ListNode next = head.next;
        head.next = null;
        while(next != null) {
            ListNode tmp = next.next;
            next.next = head;
            head = next;
            next = tmp;
        }
        return head;
    }

    public static void main(String[] args) {
        // generate 5 list nodes
        ListNode head = new ListNode(1);
        ListNode next = head;
        for (int i = 2; i < 6; i++) {
            next.next = new ListNode(i);
            next = next.next;
        }

        // print list nodes
        for (ListNode node = head; node != null; node = node.next) {
            System.out.println(node.val);
        }

        ListNode current = ReverseListContainer.reverseKGroup2(head, 3);
        System.out.println("after reverse");
        for (ListNode node = current; node != null; node = node.next) {
            System.out.println(node.val);
        }
    }

    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode next = head.next;
        head.next = swapPairs(next.next);
        next.next = head;
        return next;
    }

    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode fast = head.next.next;
        ListNode slow = head.next;

        for (; fast != null && fast.next != null; fast = fast.next.next, slow = slow.next) {
            if (fast == slow) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCyCle3(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCyCle2(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        Map<Integer, ListNode> listNodeMap = new HashMap<>();
        while(head == null) {
            if (listNodeMap.containsKey(head.val)) {
                return true;
            }
            listNodeMap.put(head.val, head);
            head = head.next;
        }
        return false;
    }

    public static ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || head.next == null || k == 1) {
            return head;
        }
        // 检查长度是否大于等于k，否则返回head
        ListNode loop = head;
        int count = k;
        while (loop != null && count > 0) {
            loop = loop.next;
            count--;
        }
        if (count > 0) {
            return head;
        }

        // 翻转长度为k的链表
        ListNode prev = null;
        ListNode curr = head;
        ListNode next = head;
        int i = k;

        while (i > 0) {
            next = curr.next;
            curr.next = prev;

            prev = curr;
            curr = next;
            i--;
        }

        // 翻转完之后，最后一个节点对应的是下一组翻转的首节点
        head.next = reverseKGroup(curr, k);

        // 返回首节点
        return prev;
    }

    public static ListNode reverseKGroup2(ListNode head, int k) {
        ListNode dummy = new ListNode(0), prev = dummy, curr = head, next;
        dummy.next = head;
        int length = 0;
        while(head != null) {
            length++;
            head = head.next;
        }
        for (int i = 0; i < length / k; i++) {
            for(int j = 0; j < k - 1; j++) {
                next = curr.next;
                curr.next = next.next;
                next.next = prev.next;
                prev.next = next;
            }
            prev = curr;
            curr = prev.next;
        }
        return dummy.next;
    }

    private static boolean checkSize(ListNode head, int k) {
        while (head != null && k > 0) {
            head = head.next;
            k--;
        }
        if (k > 0) {
            return false;
        }
        return true;
    }
    private static Map<String, ListNode> doReverse(ListNode head, int k) {
        ListNode last = head;
        ListNode curr = head.next;
        ListNode next = null;
        while (k > 1) {
            next = curr.next;
            curr.next = head;

            head = curr;
            curr = next;

            k--;
        }
        HashMap<String, ListNode> listNodeHashMap = new HashMap<>();
        listNodeHashMap.put("first", curr);
        listNodeHashMap.put("next", last);

        return listNodeHashMap;
    }

    public static boolean isValid(String s) {
        Stack<String> stack = new Stack<>();
        Map<String, String> patterns = new HashMap<>();
        patterns.put(")", "(");
        patterns.put("]", "[");
        patterns.put("}", "{");

        for (int i = 0; i < s.length(); i++) {
            String c = String.valueOf(s.charAt(i));
            if (patterns.containsKey(c)) {
                if (stack.empty()) {
                    return false;
                }
                String needMatchValue = stack.peek();
                if (patterns.get(c).equals(needMatchValue)) {
                    stack.pop();
                } else {
                    return false;
                }
            } else {
                stack.push(c);
            }
        }

        return stack.empty();
    }
}
