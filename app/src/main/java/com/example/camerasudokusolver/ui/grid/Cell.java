package com.example.camerasudokusolver.ui.grid;

public class Cell {
    public final int row;
    public final int col;
    public final int block;
    public int[] possibleVals;
    public int curVal;

    public Cell(int col, int row, int block, int[] possibleVals, int curVal) {
        this.col = col;
        this.row = row;
        this.block = block;
        this.possibleVals = possibleVals;
        this.curVal = curVal;
    }
}
