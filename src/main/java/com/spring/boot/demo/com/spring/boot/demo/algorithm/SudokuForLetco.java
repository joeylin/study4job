package com.spring.boot.demo.com.spring.boot.demo.algorithm;

/**
 * @author joe.ly
 * @date 2023/2/13
 */
public class SudokuForLetco {
    public void solveSudoku(char[][] board) {
        backtrace(board, 0, 0);
    }
    private boolean backtrace(char[][] board, int row, int col) {
        if (col == 9) {
            return backtrace(board, row + 1, 0);
        }
        if (row == 9) {
            return true;
        }
        if (board[row][col] != '.') {
            return backtrace(board, row, col + 1);
        }
        for (char ch = '1'; ch <= '9'; ch++) {
            if (!isValid(board, row, col, ch)) {
                continue;
            }
            board[row][col] = ch;
            if (backtrace(board, row, col + 1)) {
                return true;
            }
            board[row][col] = '.';
        }
        return false;
    }
    private boolean isValid(char[][] board, int row, int col, char val) {
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
        SudokuForLetco sudoku = new SudokuForLetco();
        sudoku.solveSudoku(board);
        for (int i = 0; i < 9; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < 9; j++) {
                builder.append(board[i][j]);
                builder.append(' ');
            }
            System.out.println(builder);
        }
    }
}
