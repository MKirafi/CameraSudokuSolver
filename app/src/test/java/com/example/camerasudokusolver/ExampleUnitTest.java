package com.example.camerasudokusolver;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

import com.example.camerasudokusolver.ui.grid.Cell;
import com.example.camerasudokusolver.ui.grid.Grid;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
//    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

//    @Test
    public void testGrid() {
        Grid testGrid = new Grid(9, 9, 3, 3);
        testGrid.printGridStatus();
        testGrid.setCell(0, 0, 1);
        testGrid.setCell(0, 1, 2);
        testGrid.setCell(0, 2, 3);
        testGrid.setCell(0, 3, 4);
        testGrid.setCell(0, 4, 5);
        testGrid.setCell(0, 5, 6);
        testGrid.setCell(0, 6, 7);
        testGrid.setCell(0, 7, 8);
        testGrid.setCell(0, 8, 9);
        testGrid.printGridStatus();
        testGrid.printGridStatusDetailed();
        testGrid.checkCellValid(0, 1);
    }

    @Test
    public void testRandomFill() {
        Random random = new Random(System.currentTimeMillis());
        Grid testGrid = new Grid(9, 9, 3, 3);
        Cell curCell;
        for (int i = 0; i < 81; i++) {
            curCell = testGrid.getCellByIndex(i);
            double randomValue = random.nextDouble();
            if (randomValue <= 1) {
                int randomNumber = random.nextInt(9) + 1;
                int trycounter = 0;
                while(true) {
                    boolean numberFound = false;
                    for(int value : curCell.getPossibleVals()) {
                        if(value == randomNumber) numberFound = true;
                    }
                    if(numberFound) {
                        System.out.println("found: " + i + " " + Arrays.toString(curCell.getPossibleVals()) + " " + randomNumber);
                        break;
                    }
                    System.out.println("not found: " + i + " " + Arrays.toString(curCell.getPossibleVals()) + " " + randomNumber);
                    randomNumber = random.nextInt(9) + 1;
                    trycounter++;

                    if(trycounter > 30) {
                        randomNumber = 0;
                        break;
                    }
                }
                testGrid.setCell(curCell.getRow(), curCell.getCol(), randomNumber);
                System.out.println("cell valid: " + testGrid.checkCellValid(curCell.getRow(), curCell.getCol()));
            }
        }
        testGrid.printGridStatus();
        testGrid.printGridStatusDetailed();
    }
}