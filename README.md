# MatrixView
MatrixView is a matrix chart, similar to a histogram, is used to display data chart, it also supports editing function, draw the trajectory of fingers on the diagram according to the corresponding data path generation, chart display will show the animation for the first time, support a particular column highlighted or flash animation.

## Preview
![image](screenshots/device-2018-02-26-174120.png)


## Setup
- In Eclipse, just import the MatrixView as an Android library project. Project > Clean to generate the binaries you need, like R.java, etc.
- Then, just add MatrixView as a dependency to your existing project and you're good to go!

## How to Integrate this Library into Your Projects
### Simple Example
```
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
```

### XML Usage
If you decide to use MatrixView as a view, you can define it in your xml layouts like this:
 
	<com.ayst.matrixview.MatrixView   
        android:id="@+id/matrix_view"  
        android:layout_width="350dp"  
        android:layout_height="230dp"  
        android:layout_margin="20dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:accent_color="@color/gold_dark"
        app:active_color="@color/red"
        app:column="15"
        app:column_padding="3dp"
        app:edit="true"
        app:enter_anim="true"
        app:enter_anim_interval="50"
        app:null_color="@color/black_30"
        app:row="20"
        app:row_padding="3dp"
        app:selected_anim="true"
        app:selected_anim_interval="500"
        app:selected_index="5" />

NOTE:  

* `null_color` - Invalid item color
* `active_color` - Valid item color
* `accent_color` - Column highlighting color
* `row` - The number of rows
* `column` - The number of column
* `row_padding` - The row spacing
* `column_padding` - The column spacing
* `enter_anim` - Supported enter animation
* `selected_anim` - Supported selected animation
* `enter_anim_interval` - Enter the animation time interval
* `selected_anim_interval` - Selected the animation time interval
* `selected_index` - Select a column
* `edit` - Supported edit

## Developed By
* ayst.shen@foxmail.com

## License
	Copyright 2012-2014 Jeremy Feinstein

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.