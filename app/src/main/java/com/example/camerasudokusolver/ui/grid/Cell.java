package com.example.camerasudokusolver.ui.grid;

public class Cell {
    private final int row;
    private final int col;
    private final int block;
    private int[] possibleVals;
    private int curVal;

    public Cell(int col, int row, int block, int[] possibleVals, int curVal) {
        this.col = col;
        this.row = row;
        this.block = block;
        this.possibleVals = possibleVals;
        this.curVal = curVal;
    }

    public void setVal(int val) {
        curVal = val;
    }

    public int getVal() {
        return curVal;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getBlock() {
        return block;
    }

    public int[] getPossibleVals() {
        return possibleVals;
    }

    public void updatePossibleVals(int val, int pos) {
        possibleVals[pos] = val;
    }
}
