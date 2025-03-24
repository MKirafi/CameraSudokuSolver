package com.example.camerasudokusolver.ui.grid;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public class SudokuSolver {
    private static int rows;
    private static int cols;
    private static int blockSizex;
    private static int blockSizey;
    private static int[][] currentGrid;

    public static Grid solve(Grid grid) {
        rows = grid.getRows();
        cols = grid.getCols();
        blockSizex = grid.getBlockSizex();
        blockSizey = grid.getBlockSizey();

        // Create a deep copy of the grid to modify
        currentGrid = copyGrid(grid.getGrid());

        // Attempt to solve
        if (solveRecursive()) {
            return new Grid(currentGrid, blockSizex, blockSizey); // Return solved grid
        }
        return grid; // If unsolvable, return original grid
    }

    private static boolean solveRecursive() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (currentGrid[row][col] == 0) {
                    List<Integer> possibleVals = generateShuffledNumbers();

                    for (int num : possibleVals) {
                        currentGrid[row][col] = num;
                        if (isValid(row, col) && solveRecursive()) {
                            return true;
                        }
                        currentGrid[row][col] = 0; // Reset if failed
                    }
                    return false; // No valid number found
                }
            }
        }
        return true; // Solved
    }

    private static List<Integer> generateShuffledNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= cols; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers, new Random(System.currentTimeMillis())); // Shuffle to randomize solving order
        return numbers;
    }

    private static boolean isValid(int row, int col) {
        return isRowValid(row) && isColumnValid(col) && isBlockValid(row, col);
    }

    private static boolean isRowValid(int row) {
        boolean[] seen = new boolean[cols + 1];
        for (int i = 0; i < cols; i++) {
            int num = currentGrid[row][i];
            if (num != 0) {
                if (seen[num]) return false;
                seen[num] = true;
            }
        }
        return true;
    }

    private static boolean isColumnValid(int col) {
        boolean[] seen = new boolean[rows + 1];
        for (int i = 0; i < rows; i++) {
            int num = currentGrid[i][col];
            if (num != 0) {
                if (seen[num]) return false;
                seen[num] = true;
            }
        }
        return true;
    }

    private static boolean isBlockValid(int row, int col) {
        boolean[] seen = new boolean[rows + 1]; // Max possible values = rows

        int startRow = (row / blockSizey) * blockSizey;
        int startCol = (col / blockSizex) * blockSizex;

        for (int i = startRow; i < startRow + blockSizey; i++) {
            for (int j = startCol; j < startCol + blockSizex; j++) {
                int num = currentGrid[i][j];
                if (num != 0) {
                    if (seen[num]) return false;
                    seen[num] = true;
                }
            }
        }
        return true;
    }

    private static int[][] copyGrid(int[][] original) {
        int[][] copy = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

}
