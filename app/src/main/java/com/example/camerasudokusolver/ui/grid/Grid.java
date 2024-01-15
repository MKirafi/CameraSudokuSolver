package com.example.camerasudokusolver.ui.grid;

import static java.lang.Math.max;
import java.util.Arrays;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class Grid {
    private int rows;
    private int cols;
    private int blockSizex;
    private int blockSizey;
    private int horizontalBlocks;
    private int verticalBlocks;
    private Cell[] cells;
    private Cell[][] blocks;

    // Grid constructor. Initializes grid properties and creates all the cells.
    public Grid(int rows, int cols, int blockSizex, int blockSizey) {
        this.rows = rows;
        this.cols = cols;
        this.blockSizex = blockSizex;
        this.blockSizey = blockSizey;
        this.horizontalBlocks = cols/blockSizex;
        this.verticalBlocks = rows/blockSizey;
        this.blocks = new Cell[horizontalBlocks*verticalBlocks][blockSizex*blockSizey];
        this.cells = new Cell[rows*cols];

        // Iterate and create every cell with its corresponding value
        // and fill the blocks representation.
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                int currentBlock = getBlockNum(row, col);
                int cellInBlock = getCellInBlock(row, col);

                // System.out.println("col: " + col + " row: " + row + " blocksizex: " + blockSizex + " blocksizey: " + blockSizey + " currentblock: " + currentBlock + " cellinblock: " + cellInBlock);
                int[] possibleVals = IntStream.rangeClosed(1, max(rows, cols)).toArray();
                Cell newCell = new Cell(col, row, currentBlock, possibleVals, 0);
                this.cells[(row * cols) + col] = newCell;
                this.blocks[currentBlock][cellInBlock] = newCell;
            }
        }
    }

    /* Current block # is going to be the rows of blocks up to the current block
     plus the remainder on that row. So divide current row by amount of vertical
     blocks and multiply with amount of horizontal blocks to get current vertical
     block # at the start of each row. Then divide current column by amount of
     horizontal blocks to get block # within current row. Add the two values to
     get the current block # within the grid. So row 5 column 5 would be 3 blocks
     (1 row) plus 1 block (remainder on current row), block 4.
     */
    private int getBlockNum(int row, int col) {
        return ((row / verticalBlocks) * horizontalBlocks) + (col / horizontalBlocks);
    }
    /* Same logic as blocks, except using modulus operator. By getting the remainder
     of the row/column divided by the block size we can determine where within a
     block we are.
     */
    private int getCellInBlock(int row, int col) {
        return ((row % blockSizey) * blockSizex) + (col % blockSizex);
    }

    private Cell[] getRow(int row, int col) {
        return Arrays.copyOfRange(cells, row * cols, (row * cols) + cols);
    }

    private Cell[] getCol(int row, int col) {
        Cell[] currentCol = new Cell[rows];
        for(int i = 0; i < cols; i++) {
            currentCol[i] = cells[(cols * i) + col];
            System.out.println("[getCol] index: " + (cols * i) + col + " cell row: " + currentCol[i].row + " cell col: " + currentCol[i].col);
        }
        return currentCol;
    }

    private Cell[] getBlock(int row, int col) {
        return blocks[getBlockNum(row, col)];
    }

    public int[][] getGrid() {
        int[][] grid = new int[rows][cols];

        for(int i = 0; i < rows*cols; i++) {
            grid[i/rows][i%cols] = cells[i].curVal;
        }

        return grid;
    }

    public void printGridStatus() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Print the grid values as needed
                System.out.print(cells[getCellNum(row, col)].curVal + " ");
            }
            System.out.println(); // Move to the next line for a new row
        }
        System.out.println();
        System.out.println();
    }

    public void printGridStatusDetailed() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Print the grid values as needed
                Cell curCell = cells[getCellNum(row, col)];
                System.out.print("cellnum: " + getCellNum(row, col) +
                                " Val: " + curCell.curVal +
                                " Row: " + curCell.row +
                                " Col: " + curCell.col +
                                " block: " + curCell.block +
                                " | ");
            }
            System.out.println(); // Move to the next line for a new row
        }
        System.out.println();
        System.out.println();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Print the grid values as needed
                Cell curCell = cells[getCellNum(row, col)];
                System.out.print("cellnum: " + getCellNum(row, col) +
                                " possible values: " + Arrays.toString(curCell.possibleVals) + " ");
            }
            System.out.println(); // Move to the next line for a new row
        }
        System.out.println();
        System.out.println();
    }

    public int getCellNum(int row, int col) {
        return (row * cols) + col;
    }

    public boolean checkCellValid(int row, int col) {
        return checkNumGroup("row", row, col) &&
                checkNumGroup("col", row, col) &&
                checkNumGroup("block", row, col);
    }

    public Cell[] getNumGroup(String numGroup, int row, int col) {
        System.out.println("Getting numgroup " + numGroup + " at row: " + row + " and col: " + col);
        Cell[] curNumGroup = new Cell[1];
        switch(numGroup) {
            case "row":
                curNumGroup = getRow(row, col);
                break;
            case "col":
                curNumGroup = getCol(row, col);
                break;
            case "block":
                curNumGroup = getBlock(row, col);
                break;
            default:
                System.out.println("[getNumGroup] Invalid numGroup type: " + numGroup);
        }
        System.out.println("Retrieved " + numGroup + " for cell " + getCellNum(row, col) + ", length: " + curNumGroup.length);
        return curNumGroup;
    }

    // Takes as input the type of group to test for (row, col, block) and retrieves that group.
    // Subsequently checks for duplicate values due to the property that sets can't have duplicates.
    public boolean checkNumGroup(String numGroup, int row, int col) {
        Cell[] curNumGroup = getNumGroup(numGroup, row, col);
        Set<Integer> set = new HashSet<>();
        for (Cell cell : curNumGroup) {
            int cellVal =  cell.curVal;
            //System.out.println("[checkNumGroup]: Checking cell " + getCellNum(row, col) + " with value " + cellVal + " against cell "  + getCellNum(cell.row, cell.col) + " with value " + cell.curVal + ". Current set contents:");
            //for (Integer value : set) {
            //    System.out.print(value + " ");
            //}
            //System.out.println();
            // Check if the number is invalid or the element is already in the set (duplicate)
            if(getCellNum(row, col) == getCellNum(cell.row, cell.col) || cellVal == 0) {
                continue;
            }
            if ((cellVal < 1 || cellVal > max(rows, cols)) || !set.add(cellVal)) {
                System.out.println("[checkNumGroup] checking " + numGroup + " failed at row "
                                    + cell.row + " col " + cell.col + " with value " + cellVal);
                return false;
            }
        }

        return true;
    }

    public void setCell(int row, int col, int val) {
        // Set cell value
        cells[getCellNum(row, col)].curVal = val;

        // If the value isn't valid or is 0 (not filled), no need to update possible values
        // of associated number groups.
        if(val < 1 || val > max(rows, cols)) {
            return;
        }
        // Get associated number groups to update possible values (remove current)
        Cell[] curRow = getNumGroup("row", row, col);
        Cell[] curCol = getNumGroup("col", row, col);
        Cell[] curBlock = getNumGroup("block", row, col);
        Cell[][] curNumGroups = new Cell[][]{curRow, curCol, curBlock};

        for(Cell[] numGroup : curNumGroups) {
            for(Cell cell : numGroup) {
                /*
                ============~~~~~~~~~~~~~~~============
                ============~~~~~Alert~~~~~============
                ============~~~~~~~~~~~~~~~============

                Null check below is TEMPORARY! Fix it!
                 */
//                if(cell == null) {
//                    continue;
//                }
                // Given that possibleVals is ordered and we know the value we want to update
                // we can directly access it in the array and set it to 0.
                cell.possibleVals[val-1] = 0;
                System.out.println("cell index: " + getCellNum(cell.row, cell.col) + " possible vals: " + Arrays.toString(cell.possibleVals) + " val removed: " + val);
            }
        }
    }

    public Cell getCellByIndex(int index) {
        return cells[index];
    }


    public void testRandomFill(int probability) {
        Random random = new Random(System.currentTimeMillis());
        Cell curCell;
        for (int i = 0; i < 81; i++) {
            curCell = cells[i];
            double randomValue = random.nextDouble();
            if (randomValue <= ((float)probability / 100)) {
                int randomNumber = random.nextInt(9) + 1;
                int trycounter = 0;
                while(true) {
                    boolean numberFound = false;
                    for(int value : curCell.possibleVals) {
                        if(value == randomNumber) numberFound = true;
                    }
                    if(numberFound) {
                        System.out.println("found: " + i + " " + Arrays.toString(curCell.possibleVals) + " " + randomNumber);
                        break;
                    }
                    System.out.println("not found: " + i + " " + Arrays.toString(curCell.possibleVals) + " " + randomNumber);
                    randomNumber = random.nextInt(9) + 1;
                    trycounter++;

                    if(trycounter > 30) {
                        randomNumber = 0;
                        break;
                    }
                }
                setCell(curCell.row, curCell.col, randomNumber);
                System.out.println("cell valid: " + checkCellValid(curCell.row, curCell.col));
            }
        }
        printGridStatus();
        printGridStatusDetailed();
    }
}


