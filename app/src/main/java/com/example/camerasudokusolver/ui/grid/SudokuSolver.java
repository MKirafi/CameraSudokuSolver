package com.example.camerasudokusolver.ui.grid;

import com.example.camerasudokusolver.ui.grid.GridFragment;

public class SudokuSolver {
    private static int[][] solvedGrid = new int[9][9];
    private static int[][] currentGrid;
    private static int rows;
    private static int cols;

    public static boolean solveSudoku() {
        currentGrid = GridFragment.getGridValues();
        rows = solvedGrid.length;
        cols = rows;
        return solve();
    }
    private static boolean solve(){
        for(int row = 0; row < cols; row++){
            for(int column = 0; column < rows; column++){
                if(currentGrid[row][column] == 0){
                    for(int i = 1; i <= 9; i++){
                        currentGrid[row][column] = i;
                        if(valid(currentGrid, row, column) && solve()){
                            return true;
                        }
                        currentGrid[row][column] = 0;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    //Checks if the newly imputed value is valid in that spot
    private static boolean valid(int[][] currentGrid, int row, int column){
        return (rowcheck(currentGrid, row) && columncheck(currentGrid, column) && blockcheck(currentGrid, row, column));
    }

    //Checks if the given row is valid
    private static boolean rowcheck(int[][] currentGrid, int row){
        int[] values = new int[9];
        for (int i = 0; i < currentGrid[row].length; i++){
            for(int j = 0; j < values.length; j++){
                if (currentGrid[row][i] == values[j] && currentGrid[row][i] != 0){
                    return false;
                }

            }
            values[i] = currentGrid[row][i];
        }
        return true;
    }

    //Checks if the given column is valid
    private static boolean columncheck(int[][] currentGrid, int column){
        int[] values = new int[9];
        for (int i = 0; i < currentGrid.length; i++){
            for(int j = 0; j < values.length; j++){
                if (currentGrid[i][column] == values[j] && currentGrid[i][column] != 0){
                    return false;
                }
            }
            values[i] = currentGrid[i][column];
        }
        return true;
    }

    //Checks if the fiven Block of 3x3 is valid
    private static boolean blockcheck(int[][] currentGrid, int row, int column){
        int [] rows = new int[3];
        int [] columns = new  int[3];
        int [] values = new int[9];
        if(row == 0 || row == 1 || row == 2){
            rows = new int[]{0, 1, 2};
        }else if(row == 3 || row == 4 || row == 5){
            rows = new int[]{3, 4, 5};
        }else if(row == 6 || row == 7 || row == 8){
            rows = new int[]{6, 7, 8};
        }

        if(column == 0 || column == 1 || column == 2){
            columns = new int[]{0, 1, 2};
        }else if(column == 3 || column == 4 || column == 5){
            columns = new int[]{3, 4, 5};
        }else if(column == 6 || column == 7 || column == 8){
            columns = new int[]{6, 7, 8};
        }


        for(int i = rows[0]; i <= rows[2]; i++){
            for(int j = columns[0]; j <= columns[2]; j++){
                for(int k = 0; k < values.length; k++){
                    if (currentGrid[i][j] == values[k] && currentGrid[i][j] != 0){
                        return false;
                    }
                }
                for(int x = 0; x < values.length; x++){
                    if (values[x] == 0){
                        values[x] = currentGrid[i][j];
                        break;
                    }
                }
            }
        }
        return true;
    }


    public static int[][] getSolvedGrid() {
//        for (int i = 0; i < 9; i++) {
//            for (int j = 0; j < 9; j++) {
//                solvedGrid[i][j] = 7;
//            }
//        }
        solvedGrid = currentGrid;
        return solvedGrid;
    }

    // Method to print the current currentGrid status
    private void printGridStatus() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Print the currentGrid values as needed
                System.out.print(currentGrid[i][j] + " ");
            }
            System.out.println(); // Move to the next line for a new row
        }
        System.out.println(); // Separate the prints for different time intervals
    }

}
