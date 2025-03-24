package com.example.camerasudokusolver.ui.grid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class GridViewModel extends AndroidViewModel {
    private final MutableLiveData<Grid> gridLiveData = new MutableLiveData<>();
    private final SharedPreferences prefs;

    public GridViewModel(Application application) {
        super(application);
        prefs = application.getSharedPreferences("SudokuPrefs", Context.MODE_PRIVATE);
        loadGrid();
    }

    public LiveData<Grid> getGrid() {
        return gridLiveData;
    }

    public void setGrid(Grid newGrid) {
        gridLiveData.setValue(newGrid);
        saveGrid();
    }

    public void updateCell(int row, int col, int value) {
        Grid grid = gridLiveData.getValue();
        if (grid != null) {
            grid.setCell(row, col, value);
            gridLiveData.setValue(grid);
            saveGrid();
        }
    }

    public void solveGrid() {
        Grid grid = gridLiveData.getValue();
        if (grid != null) {
            Grid solvedGrid = SudokuSolver.solve(grid);
            setGrid(solvedGrid);
        }
    }

    public void clearGrid() {
        setGrid(new Grid(9, 9, 3, 3)); // Reset grid
    }

    public void randomFill(EditText probabilityFillVal) {
        clearGrid(); // Reset the grid first

        Grid grid = gridLiveData.getValue();
        if (grid == null) return;

        // Validate probability input
        String probabilityStr = probabilityFillVal.getText().toString();
        int probability = (probabilityStr.isEmpty()) ? 25 : Integer.parseInt(probabilityStr);

        // Ensure probability is within 0-100%
        probability = Math.max(0, Math.min(probability, 100));
        probabilityFillVal.setText(String.valueOf(probability));

        // Fill the grid randomly
        if (grid.testRandomFill(probability)) {
            grid.printGridStatus();
            gridLiveData.setValue(grid); // Only update LiveData if the grid was changed
        }
    }


    private void saveGrid() {
        SharedPreferences.Editor editor = prefs.edit();
        Grid grid = gridLiveData.getValue();

        if (grid == null || grid.getGrid() == null) {
            return; // Avoid saving if grid is uninitialized
        }

        int[][] gridData = grid.getGrid();

        // Save the grid values as a comma-separated string
        StringBuilder sb = new StringBuilder();
        for (int[] row : gridData) {
            for (int cell : row) sb.append(cell).append(",");
        }
        editor.putString("grid", sb.toString());

        // Store grid properties
        editor.putInt("rows", grid.getRows());
        editor.putInt("cols", grid.getCols());
        editor.putInt("blockSizex", grid.getBlockSizex());
        editor.putInt("blockSizey", grid.getBlockSizey());

        editor.apply();
    }


    private void loadGrid() {
        String savedGrid = prefs.getString("grid", "");
        int rows = prefs.getInt("rows", 9);
        int cols = prefs.getInt("cols", 9);
        int blockSizex = prefs.getInt("blockSizex", 3);
        int blockSizey = prefs.getInt("blockSizey", 3);

        if (!savedGrid.isEmpty()) {
            String[] values = savedGrid.split(",");
            if (values.length != rows * cols) {
                gridLiveData.setValue(new Grid(rows, cols, blockSizex, blockSizey)); // Reset grid if data is invalid
                return;
            }

            int[][] loadedGrid = new int[rows][cols];
            int index = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    loadedGrid[i][j] = Integer.parseInt(values[index++]);
                }
            }

            // Use the constructor with loaded properties
            gridLiveData.setValue(new Grid(loadedGrid, blockSizex, blockSizey));
        } else {
            gridLiveData.setValue(new Grid(rows, cols, blockSizex, blockSizey)); // Default empty grid
        }
    }

}