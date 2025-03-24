package com.example.camerasudokusolver.ui.grid;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
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

import java.util.Locale;

public class GridFragment extends Fragment {
    private FragmentGridBinding binding;
    private GridViewModel viewModel;
    private EditText[][] editTexts;
    private int rows = 9, cols = 9; // Default Sudoku size

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGridBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Context mContext = requireContext();

        viewModel = new ViewModelProvider(requireActivity()).get(GridViewModel.class);

        // Initialize GridLayout and EditTexts
        initializeGrid(mContext);

        // Observe changes in the grid and update UI accordingly
        viewModel.getGrid().observe(getViewLifecycleOwner(), this::updateGridLayout);

        // Solve button logic
        binding.solveButton.setOnClickListener(view -> viewModel.solveGrid());

        // Clear button logic
        binding.clearButton.setOnClickListener(view -> viewModel.clearGrid());

        // Random fill button logic
        binding.randomFill.setOnClickListener(view -> viewModel.randomFill(binding.probabilityFillVal));

        return root;
    }

    // Initialize the GridLayout and dynamically create EditTexts
    private void initializeGrid(Context mContext) {
        editTexts = new EditText[rows][cols];
        binding.sudokuGrid.removeAllViews();
        binding.sudokuGrid.setRowCount(rows);
        binding.sudokuGrid.setColumnCount(cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                editTexts[i][j] = createEditText(mContext, i, j);
                binding.sudokuGrid.addView(editTexts[i][j]);
            }
        }
    }

    // Update EditTexts when the grid changes
    private void updateGridLayout(Grid grid) {
        int[][] gridValues = grid.getGrid();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int gridVal = gridValues[i][j];
                String currentText = editTexts[i][j].getText().toString();

                // Only update if the displayed value is different
                String newText = (gridVal == 0) ? "" : String.valueOf(gridVal);
                if (!currentText.equals(newText)) {
                    editTexts[i][j].setText(newText);
                }
            }
        }
    }

    // Create EditText with constraints and behavior
    private EditText createEditText(Context mContext, final int row, final int col) {
        EditText editText = new EditText(mContext);
        editText.setLayoutParams(new GridLayout.LayoutParams());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        editText.setGravity(Gravity.CENTER);
        editText.getLayoutParams().width = 100;
        editText.getLayoutParams().height = 130;

        // Set up a TextWatcher to dynamically update ViewModel
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String changedText = charSequence.toString();
                int value = changedText.isEmpty() ? 0 : Integer.parseInt(changedText);
                viewModel.updateCell(row, col, value); // Sync with ViewModel
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return editText;
    }
}