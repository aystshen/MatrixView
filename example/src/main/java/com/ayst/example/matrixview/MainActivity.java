package com.ayst.example.matrixview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ayst.matrixview.MatrixView;

public class MainActivity extends AppCompatActivity {

    private MatrixView mMatrixView;
    private int[] mExampleData = {3, 4, 5, 15, 14, 10, 19, 18, 16, 15, 14, 12, 5, 4, 1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMatrixView = (MatrixView) findViewById(R.id.matrix_view);
        mMatrixView.setNegativeColor(getResources().getColor(R.color.black_30));
        mMatrixView.setActiveColor(getResources().getColor(R.color.colorAccent));
        mMatrixView.setHighlightedColor(getResources().getColor(R.color.red));
        mMatrixView.setColumnNumber(15);
        mMatrixView.setRowNumber(20);
        mMatrixView.setColumnPadding(3);
        mMatrixView.setRowPadding(3);
        mMatrixView.setSupportEnterAnim(true);
        mMatrixView.setSupportHighlightedAnim(true);
        mMatrixView.setEnterAnimInterval(50);
        mMatrixView.setHighlightedAnimInterval(500);
        mMatrixView.setSupportEdit(true);
        mMatrixView.setMinValue(0);
        mMatrixView.setMaxValue(20);
        mMatrixView.show(mExampleData); // Show chart
        mMatrixView.setHighlight(5); // Set highlight column

        ((Button)findViewById(R.id.btn_start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMatrixView.getHighlight() >= 0) {
                    mMatrixView.setHighlight(-1);
                } else {
                    mMatrixView.setHighlight(0);
                }
            }
        });

        ((Button)findViewById(R.id.btn_minus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] array = mMatrixView.getArray();
                int index = mMatrixView.getHighlight();
                if (array != null && index >= 0 && index < array.length) {
                    if (array[index] > 0) {
                        mMatrixView.updateHighlightValue(array[index]-1);
                    }
                }
            }
        });

        ((Button)findViewById(R.id.btn_plus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] array = mMatrixView.getArray();
                int index = mMatrixView.getHighlight();
                if (array != null && index >= 0 && index < array.length) {
                    if (array[index] < mMatrixView.getRowNumber()) {
                        mMatrixView.updateHighlightValue(array[index]+1);
                    }
                }
            }
        });
    }
}
