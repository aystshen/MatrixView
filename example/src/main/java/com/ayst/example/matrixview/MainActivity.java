package com.ayst.example.matrixview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    }
}
