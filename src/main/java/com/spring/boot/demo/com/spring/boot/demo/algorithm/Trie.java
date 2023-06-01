package com.spring.boot.demo.com.spring.boot.demo.algorithm;

/**
 * @author joe.ly
 * @date 2023/2/16
 */
public class Trie {
    static final int ALPHABET_SIZE = 26;
    class TrieNode {
        char val;
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        boolean isWordEnd = false;
        public TrieNode(char val) {
            this.val = val;
        }
        public TrieNode() {}
    }
    private TrieNode start = new TrieNode();
    public Trie() {
    }

    public void insert(String word) {
        if (word == null || "".equals(word)) return;
        TrieNode curr = start;
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (curr.children[c - 'a'] == null) {
                curr.children[c - 'a'] = new TrieNode(chars[i]);
            }
            curr = curr.children[c - 'a'];
        }
        curr.isWordEnd = true;
    }

    public boolean search(String word) {
        if (word == null || "".equals(word)) return false;
        TrieNode node = findLastNodeOfPrefix(word);
        if (node != null && node.isWordEnd) return true;
        return false;
    }

    // refactor this function more readable
    private TrieNode findLastNodeOfPrefix(String prefix) {
        TrieNode currentNode = start;
        char[] chars = prefix.toCharArray();
        for (char prefixChar : chars) {
            TrieNode node = currentNode.children[prefixChar - 'a'];
            if (node == null) return null;
            currentNode = node;
        }
        return currentNode;
    }

    public boolean startsWith(String prefix) {
        if (prefix == null || "".equals(prefix)) return true;
        TrieNode node = findLastNodeOfPrefix(prefix);
        if (node != null) return true;
        return false;
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        System.out.println(trie.search("apple"));   // 返回 True
        System.out.println(trie.search("app"));     // 返回 False
        System.out.println(trie.startsWith("app")); // 返回 True
        trie.insert("app");
        System.out.println(trie.search("app"));     // 返回 True
    }
}
