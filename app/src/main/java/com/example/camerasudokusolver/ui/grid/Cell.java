package com.example.camerasudokusolver.ui.grid;

public class Cell {
    private int row;
    private int col;
    private int block;
    private int[] possibleVals;
    private int curVal;

    public Cell(int row, int col, int block, int[] possibleVals, int curVal) {
        this.row = row;
        this.col = col;
        this.block = block;
        this.possibleVals = possibleVals;
        this.curVal = curVal;
    }

    public int[] getPossibleVals() {
        return this.possibleVals;
    }
    public void setPossibleVals(int[] possibleVals) {
        this.possibleVals = possibleVals;
    }
}
