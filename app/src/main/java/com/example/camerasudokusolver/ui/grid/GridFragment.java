package com.example.camerasudokusolver.ui.grid;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.camerasudokusolver.databinding.FragmentGridBinding;

import java.util.Arrays;
import java.util.stream.Stream;

public class GridFragment extends Fragment {

    private FragmentGridBinding binding;
    EditText[][] editTexts;
    private static int[][] gridValues;
    private Handler handler;
    private GridViewModel viewModel;
    private GridLayout gridLayout;
    private Context mContext;

    private int rows = 9;
    private int cols = 9;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGridBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = requireContext();
        gridLayout = binding.sudokuGrid;

        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(GridViewModel.class);
        gridValues = viewModel.getGridValues();
        editTexts = new EditText[rows][cols];
        // Programmatically create EditText elements and add them to the GridLayout
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                editTexts[i][j] = createEditText(mContext, i , j);
                gridLayout.addView(editTexts[i][j]);
            }
        }

        rows = gridValues[0].length;
        cols = rows;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int gridVal = gridValues[i][j];
                if (gridVal == 0) {
                    editTexts[i][j].setText("");
                } else {
                    editTexts[i][j].setText(Integer.toString(gridVal));
                }
            }
        }
        System.out.println("[GridFragment]->[onCreateView] grid after setting values from GridViewModel");
        printGridStatus();

        // Set an OnClickListener for the button
        binding.solveButton.setOnClickListener(view -> {
            // Call the onSolveClick function when the button is clicked
            onSolveClick();
        });

        binding.clearButton.setOnClickListener(view -> {
            onClearClick();
        });

        binding.randomFill.setOnClickListener(view -> {
            onRandomFillClick();
        });

        // Initialize the handler and schedule the task to log grid status every second
//        handler = new Handler(Looper.getMainLooper());
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                printGridStatus();
//                handler.postDelayed(this, 1000); // Repeat every 1000 milliseconds (1 second)
//            }
//        }, 1000); // Initial delay 1000 milliseconds (1 second)

        return root;
    }

    private void onSolveClick() {
        boolean solveResult = SudokuSolver.solveSudoku();
        if (solveResult) {
            System.out.println("[Solver] Grid solved!");
            Toast.makeText(mContext, "Grid solved!", Toast.LENGTH_SHORT).show();
            gridValues = SudokuSolver.getSolvedGrid();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    editTexts[i][j].setText(Integer.toString(gridValues[i][j]));
                }
            }
        } else {
            System.out.println("[Solver] No solution");
            Toast.makeText(mContext, "No solution.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClearClick() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gridValues[i][j] = 0;
                editTexts[i][j].setText("");
            }
        }
        System.out.println("[GridFragment]->[onClearClick] Grid after cleared");
        printGridStatus();
    }

    private void onRandomFillClick() {
//        standard sudoku grid
        gridValues = new int[][]{
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 0, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        Grid grid = new Grid(9, 9, 3, 3);
        String probability = binding.probabilityFillVal.getText().toString();
        if(probability.equals("")) {
            grid.testRandomFill(25);
            binding.probabilityFillVal.setText("25");
        } else {
            grid.testRandomFill(Integer.parseInt(probability));
        }
        gridValues = grid.getGrid();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int gridVal = gridValues[i][j];
                editTexts[i][j].setText(gridVal == 0 ? "" : Integer.toString(gridValues[i][j]));
            }
        }
    }


    // Utility method to create an EditText with desired properties
    private EditText createEditText(Context mContext, final int row, final int col) {
        EditText editText = new EditText(mContext);
        // Set layout parameters, input type, etc. as needed
        editText.setLayoutParams(new GridLayout.LayoutParams());
        editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        editText.getLayoutParams().width = 100;
        editText.getLayoutParams().height = 130;
        editText.setGravity(Gravity.CENTER);
        // Set the maxLength property to 1
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

        // Set up a TextWatcher to dynamically update gridValues
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Not needed in this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String changedText = charSequence.toString();
                if(changedText != null && !changedText.trim().isEmpty()) {
                    System.out.println("[GridFragment]->[onTextChanged] new text:" + changedText);
                    gridValues[row][col] = Integer.parseInt(changedText);
                } else {
                    gridValues[row][col] = 0;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed in this case
            }
        });

        return editText;
    }

    // Method to print the current grid status
    private void printGridStatus() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Print the grid values as needed
                System.out.print(gridValues[i][j] + " ");
            }
            System.out.println(); // Move to the next line for a new row
        }
        System.out.println();
        System.out.println();
    }

    public static int[][] getGridValues() {
        return gridValues;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        System.out.println("[GridFragment]->[onSaveInstanceState]");
        printGridStatus();
        viewModel.setGridValues(gridValues);
        for (EditText[] row : editTexts) {
            for (EditText editText : row) {
                gridLayout.removeView(editText);
            }
        }
    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        System.out.println("[onViewStateRestored]");
        gridValues = viewModel.getGridValues();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
//        handler.removeCallbacksAndMessages(null);
    }
}