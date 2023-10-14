package com.example.camerasudokusolver.ui.grid;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GridViewModel extends ViewModel {

    private int[][] gridValues;

    public GridViewModel() {
        gridValues = new int[9][9];
    }

    public int[][] getGridValues() {
        return gridValues;
    }
    public void setGridValues(int[][] gridValues) {
        System.out.println("[GridViewModel]->[setGridValues] this.gridValues");
        printGridStatus(this.gridValues);
        System.out.println("[GridViewModel]->[setGridValues] gridValues function argument");
        printGridStatus(gridValues);
        this.gridValues = gridValues;
    }

    private void printGridStatus(int[][] vals) {
        for (int i = 0; i < vals.length; i++) {
            for (int j = 0; j < vals.length; j++) {
                // Print the grid values as needed
                System.out.print(vals[i][j] + " ");
            }
            System.out.println(); // Move to the next line for a new row
        }
        System.out.println();
        System.out.println();
    }
}