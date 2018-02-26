package com.ayst.example.matrixview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ayst.matrixview.MatrixView;

public class MainActivity extends AppCompatActivity {

    private MatrixView mMatrixView;
    private int[] mExampleData = {10, 9, 8, 5, 15, 17, 19, 18, 16, 10, 10, 12, 13, 14, 15};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMatrixView = (MatrixView) findViewById(R.id.matrix_view);
        mMatrixView.setNullColor(getResources().getColor(R.color.black_30));
        mMatrixView.setActiveColor(getResources().getColor(R.color.colorAccent));
        mMatrixView.setSelectedColor(getResources().getColor(R.color.red));
        mMatrixView.setColumn(15);
        mMatrixView.setRow(20);
        mMatrixView.setColumnPadding(3);
        mMatrixView.setRowPadding(3);
        mMatrixView.setSupportEnterAnim(true);
        mMatrixView.setSupportSelectedAnim(true);
        mMatrixView.setEnterAnimInterval(50);
        mMatrixView.setSelectedAnimInterval(500);
        mMatrixView.setSupportEdit(true);
        mMatrixView.show(mExampleData); // Show chart
        mMatrixView.setSelected(5); // Set highlight column
    }
}
