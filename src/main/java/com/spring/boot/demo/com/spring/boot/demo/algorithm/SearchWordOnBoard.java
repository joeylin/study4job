package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author joe.ly
 * @date 2023/2/16
 */
public class SearchWordOnBoard {
    public boolean exist(char[][] board, String word) {
        if (word == null || "".equals(word)) return false;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boolean res = backtrace(board, word, i, j, 0);
                if (res) return true;
            }
        }
        return false;
    }
    public boolean backtrace(char[][] board, String word, int i, int j, int k) {
        if (k >= word.length()) return true;
        if (i < 0 || i >= board.length) return false;
        if (j < 0 || j >= board[0].length) return false;
        if (board[i][j] != word.charAt(k)) return false;

        board[i][j] += 256;
        boolean result = backtrace(board, word, i + 1, j, k + 1) || backtrace(board, word, i - 1, j, k + 1) ||
            backtrace(board, word, i, j + 1, k + 1) || backtrace(board, word, i, j - 1, k + 1);
        board[i][j] -= 256;
        return result;
    }

    class TrieNode {
        static final int ALPHABET_SIZE = 26;
        char val;
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        boolean isWordEnd = false;
        TrieNode(char val) {
            this.val = val;
        }
        TrieNode(){}
    }
    private void insert(TrieNode root, String word) {
        TrieNode currentNode = root;
        for (char c : word.toCharArray()) {
            if (currentNode.children[c - 'a'] == null) {
                currentNode.children[c - 'a'] = new TrieNode(c);
            }
            currentNode = currentNode.children[c - 'a'];
        }
        currentNode.isWordEnd = true;
    }

    public List<String> findWords(char[][] board, String[] words) {
        TrieNode root = new TrieNode();
        for (String word : words) {
            insert(root, word);
        }
        Set<String> res = new HashSet<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                backtraceForFindWords(board, i, j, root, "" , words, res);
            }
        }
        return new ArrayList<>(res);
    }
    public void backtraceForFindWords(char[][] board, int i, int j, TrieNode root, String word, String[] words, Set<String> container) {
        if (i < 0 || i >= board.length) return;
        if (j < 0 || j >= board[0].length) return;

        TrieNode node = root.children[board[i][j] - 'a'];
        if (node != null) {
            if (node.isWordEnd) {
                container.add(word + board[i][j]);
            }
            backtraceForFindWords(board, i + 1, j, node, word + board[i][j], words, container);
            backtraceForFindWords(board, i - 1, j, node, word + board[i][j], words, container);
            backtraceForFindWords(board, i, j - 1, node, word + board[i][j], words, container);
            backtraceForFindWords(board, i, j + 1, node, word + board[i][j], words, container);
        }
    }

    public static void main(String[] args) {
        //char[][] board = new char[][]{{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}};
        //SearchWordOnBoard searchWordOnBoard = new SearchWordOnBoard();
        //boolean result = searchWordOnBoard.exist(board, "ABCB");
        //System.out.println(result);

        char[][] board1 = new char[][]{{'o','a','b','n'},{'o','t','a','e'},{'a','h','k','r'},{'a','f','l','v'}};
        SearchWordOnBoard searchWordOnBoard = new SearchWordOnBoard();
        String[] words = new String[]{"oa","oaa"};
        List<String> res = searchWordOnBoard.findWords(board1, words);
        System.out.println(res);
    }
}
