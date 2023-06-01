package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joe.ly
 * @date 2023/1/16
 */
public class SolveNQueens {
    public static List<List<String>> solveNQueens(int n) {
        String[][] board = new String[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = ".";
            }
        }
        List<List<String>> res = new ArrayList<>();
        backtrace(board, 0, res);
        return res;
    }

    public static void backtrace(String[][] board, int row, List<List<String>> container) {
        if (row == board[0].length) {
            container.add(print(board));
            return;
        }
        for (int i = 0; i < board[0].length; i++) {
            if (!isValid(board, row, i)) {
                continue;
            }
            board[row][i] = "Q";
            backtrace(board, row + 1, container);
            board[row][i] = ".";
        }
    }
    public static List<String> print(String[][] board) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < board[0].length; i++) {
            String row = String.join("", board[i]);
            res.add(row);
        }
        return res;
    }

    public static boolean isValid(String[][] board, int row, int col) {
        int n = board[0].length;
        // 检查列是否重复
        for (int i = 0; i <= row; i++) {
            if ("Q".equals(board[i][col])) {
                return false;
            }
        }
        // 检查左上角是否重复
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if ("Q".equals(board[i][j])) {
                return false;
            }
        }
        // 检查右上角是否重复
        for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++) {
            if ("Q".equals(board[i][j])) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        List<List<String>> res = solveNQueens(1);
        System.out.println(res);

        // write unit test for solveNQueens function
        List<List<String>> res1 = solveNQueens(4);
        System.out.println(res1);
    }
}
