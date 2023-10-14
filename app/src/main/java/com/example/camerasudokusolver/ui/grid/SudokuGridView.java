package com.example.camerasudokusolver.ui.grid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SudokuGridView extends View {

    private Paint linePaint;

    public SudokuGridView(Context context) {
        super(context);
        init();
    }

    public SudokuGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SudokuGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(getResources().getColor(android.R.color.black));
        linePaint.setStrokeWidth(5); // Adjust line thickness as needed
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int startX = (getWidth() - 900) / 2;
        canvas.drawLine(startX + 300, 20, startX + 300, 130*9 + 20, linePaint);
        canvas.drawLine(startX + 600, 20, startX + 600, 130*9 + 20, linePaint);
        canvas.drawLine(startX, 410, startX + 900, 410, linePaint);
        canvas.drawLine(startX, 130*6  + 20, startX + 900, 130*6 + 20, linePaint);
        linePaint.setStrokeWidth(10);
        canvas.drawLine(startX, 20, startX + 900, 20, linePaint);
        canvas.drawLine(startX + 900, 20, startX + 900, 130*9 + 20, linePaint);
        canvas.drawLine(startX + 900, 130*9 + 20, startX, 130*9 + 20, linePaint);
        canvas.drawLine(startX, 130*9 + 20, startX, 20, linePaint);
        linePaint.setStrokeWidth(5);
        // Draw horizontal lines
//        for (int i = 1; i < 9; i++) {
//            float y = i * getHeight() / 9.0f;
//            canvas.drawLine(0, y, getWidth(), y, linePaint);
//        }

        // Draw vertical lines
//        for (int i = 1; i < 9; i++) {
//            float x = i * getWidth() / 9.0f;
//            canvas.drawLine(x, 0, x, getHeight(), linePaint);
//        }
    }
}
