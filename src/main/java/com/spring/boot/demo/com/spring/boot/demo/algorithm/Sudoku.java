package com.spring.boot.demo.com.spring.boot.demo.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author joe.ly
 * @date 2023/2/10
 */
public class Sudoku {
    public static char[][] completeSudoku(char[][] board) {
        backtrace(0, 0, board);
        return board;
    }
    public static boolean backtrace(int row, int col, char[][] board) {
        if (col == 9) {
            return backtrace(row + 1, 0, board);
        }
        if (row == 9) {
            return true;
        }
        if (board[row][col] != '.') {
            return backtrace(row, col + 1, board);
        }
        for (char i = '1'; i <= '9'; i++) {
            if (!validateValue(row, col, board, i)) {
                continue;
            }
            board[row][col] = i;
            if (backtrace(row, col + 1, board)) {
                return true;
            }
            board[row][col] = '.';
        }
        return false;
    }
    public static boolean validateValue(int row, int col, char[][] board, char val) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == val) return false;
            if (board[i][col] == val) return false;
            if (board[(row / 3) * 3 + i / 3][(col / 3) * 3 + i % 3] == val) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        char[][] board = new char[][] {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        char[][] res = completeSudoku(board);
        for (int i = 0; i < 9; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < 9; j++) {
                builder.append(res[i][j]);
                builder.append(' ');
            }
            System.out.println(builder);
        }
    }

    public boolean isValidSudoku1(char[][] board) {
        // 记录某行，某位数字是否已经被摆放
        boolean[][] row = new boolean[9][9];
        // 记录某列，某位数字是否已经被摆放
        boolean[][] col = new boolean[9][9];
        // 记录某 3x3 宫格内，某位数字是否已经被摆放
        boolean[][] block = new boolean[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int num = board[i][j] - '1';
                    int blockIndex = i / 3 * 3 + j / 3;
                    if (row[i][num] || col[j][num] || block[blockIndex][num]) {
                        return false;
                    } else {
                        row[i][num] = true;
                        col[j][num] = true;
                        block[blockIndex][num] = true;
                    }
                }
            }
        }
        return true;
    }

    public boolean isValidSudoku2(char[][] board) {
        boolean[][] row = new boolean[9][9];
        boolean[][] col = new boolean[9][9];
        boolean[][] block = new boolean[9][9];

        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != '.') {
                    int num = board[i][j] - '1';
                    int blockIndex = i / 3 * 3 + j / 3;
                    if (row[i][num] || col[j][num] || block[blockIndex][num]) {
                        return false;
                    } else {
                        row[i][num] = true;
                        col[j][num] = true;
                        block[blockIndex][num] = true;
                    }
                }
            }
        }
        return true;
    }
}
