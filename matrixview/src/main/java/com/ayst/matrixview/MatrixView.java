package com.ayst.matrixview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * MatrixView is a matrix chart, similar to a histogram,
 * is used to display data chart, it also supports editing
 * function, draw the trajectory of fingers on the diagram
 * according to the corresponding data path generation,
 * chart display will show the animation for the first time,
 * support a particular column highlighted or flash animation.
 */
public class MatrixView extends View {

    /**
     * Invalid item color
     */
    private int mNullColor = 0x23000000;

    /**
     * Valid item color
     */
    private int mActiveColor = 0xb3ffffff;

    /**
     * Column highlighting color
     */
    private int mSelectedColor = 0xfffdfe29;

    private int mPaintSelectedColor = mSelectedColor;

    /**
     * The number of rows
     */
    private int mRow = 10;

    /**
     * The number of columns
     */
    private int mColumn = 10;

    /**
     * The row spacing
     */
    private int mRowPadding = 10;

    /**
     * The column spacing
     */
    private int mColumnPadding = 10;

    /**
     * The item width, Automatic calculation based on view width and column number.
     */
    private int mItemWidth = 0;

    /**
     * The item height, Automatic calculation based on view height and row number.
     */
    private int mItemHeight = 0;

    /**
     * Data used to populate a graph.
     */
    private int mArray[];

    private int mSrcArray[];

    /**
     * Enter the animation time interval.
     */
    private int mEnterAnimInterval = 50;

    /**
     * Selected the animation time interval.
     */
    private int mSelectedAnimInterval = 500;

    /**
     * Select a column
     */
    private int mSelectedIndex = -1;

    private int mAnimCount = 0;

    /**
     * Supported enter animation.
     */
    private boolean mIsEnterAnim = true;

    /**
     * Supported selected animation.
     */
    private boolean mIsSelectedAnim = true;

    /**
     * Supported edit.
     */
    private boolean mIsSupportEdit = false;

    private Paint mNullPaint;
    private Paint mActivePaint;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public MatrixView(Context context) {
        this(context, null);
    }

    public MatrixView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MatrixView, 0, 0);

        mNullColor = a.getColor(R.styleable.MatrixView_null_color, mNullColor);
        mActiveColor = a.getColor(R.styleable.MatrixView_active_color, mActiveColor);
        mSelectedColor = a.getColor(R.styleable.MatrixView_accent_color, mSelectedColor);
        mRow = a.getInteger(R.styleable.MatrixView_row, mRow);
        mColumn = a.getInteger(R.styleable.MatrixView_column, mColumn);
        mRowPadding = a.getDimensionPixelSize(R.styleable.MatrixView_row_padding, mRowPadding);
        mColumnPadding = a.getDimensionPixelSize(R.styleable.MatrixView_column_padding, mColumnPadding);
        mIsEnterAnim = a.getBoolean(R.styleable.MatrixView_enter_anim, mIsEnterAnim);
        mIsSelectedAnim = a.getBoolean(R.styleable.MatrixView_selected_anim, mIsSelectedAnim);
        mEnterAnimInterval = a.getInteger(R.styleable.MatrixView_enter_anim_interval, mEnterAnimInterval);
        mSelectedAnimInterval = a.getInteger(R.styleable.MatrixView_selected_anim_interval, mSelectedAnimInterval);
        mSelectedIndex = a.getInteger(R.styleable.MatrixView_selected_index, mSelectedIndex);
        mIsSupportEdit = a.getBoolean(R.styleable.MatrixView_edit, mIsSupportEdit);
        a.recycle();

        init();
    }

    private void init() {
        this.setFocusable(mIsSupportEdit);
        this.setFocusableInTouchMode(mIsSupportEdit);

        mNullPaint = new Paint();
        mNullPaint.setAntiAlias(true);
        mNullPaint.setStyle(Paint.Style.FILL);
        mNullPaint.setColor(mNullColor);

        mActivePaint = new Paint();
        mActivePaint.setAntiAlias(true);
        mActivePaint.setStyle(Paint.Style.FILL);
        mActivePaint.setColor(mActiveColor);

        mArray = new int[mColumn];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mItemWidth = (getWidth() - (mRowPadding * (mColumn - 1))) / mColumn;
        mItemHeight = (getHeight() - (mColumnPadding * (mRow - 1))) / mRow;

        int rowBiases = (getHeight() - mRow * mItemHeight - (mColumnPadding * (mRow - 1))) / 2;
        int columnBiases = (getWidth() - mColumn * mItemWidth - (mRowPadding * (mColumn - 1))) / 2;

        for (int i = 0; i < mColumn; i++) {
            for (int j = 0; j < mRow; j++) {
                Rect rect = new Rect();
                rect.left = mColumnPadding * i + mItemWidth * i + columnBiases;
                rect.right = rect.left + mItemWidth;
                rect.top = mRowPadding * j + mItemHeight * j + rowBiases;
                rect.bottom = rect.top + mItemHeight;
                if (mArray != null && i < mArray.length && (mRow - j) <= mArray[i]) {
                    if (i == mSelectedIndex) {
                        mActivePaint.setColor(mPaintSelectedColor);
                    } else {
                        mActivePaint.setColor(mActiveColor);
                    }
                    canvas.drawRect(rect, mActivePaint);
                } else {
                    canvas.drawRect(rect, mNullPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsSupportEdit) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int row = 0;
            int column = 0;

            for (int i = 0; i < mColumn; i++) {
                if (x < (i + 1) * (mItemWidth + mColumnPadding)) {
                    column = i;
                    break;
                }
            }
            for (int i = 0; i < mRow; i++) {
                if (y < (i + 1) * (mItemHeight + mRowPadding)) {
                    row = i + 1;
                    break;
                }
            }

            mArray[column] = mRow - row;
            invalidate();

            return true;
        }

        return super.onTouchEvent(event);
    }

    /**
     * This method is called when the diagram needs to be displayed,
     * passing in the data that needs to be displayed.
     *
     * @param data
     */
    public void show(int[] data) {
        release();
        mSrcArray = data;
        if (mSrcArray != null && mSrcArray.length > 0) {
            if (mIsEnterAnim) {
                mHandler.postDelayed(mEnterAnimRunnable, mEnterAnimInterval);
            } else {
                for (int i = 0; i < (mSrcArray.length < mColumn ? mSrcArray.length : mColumn); i++) {
                    mArray[i] = mSrcArray[i];
                }
                invalidate();
            }
        }
    }

    /**
     * This method is called when you need to clear the existing data
     * on the diagram.
     */
    public void release() {
        mHandler.removeCallbacks(mEnterAnimRunnable);
        mHandler.removeCallbacks(mSelectedAnimRunnable);

        mPaintSelectedColor = mActiveColor;
        mAnimCount = 0;
        mSelectedIndex = -1;

        for (int i = 0; i < mArray.length; i++) {
            mArray[i] = 0;
        }
    }

    /**
     * When editing is supported, the editor calls this method to get
     * the edited data.
     *
     * @return
     */
    public int[] getArray() {
        return mArray;
    }

    /**
     * This method is called when a column is highlighted.
     *
     * @param index
     */
    public void setSelected(int index) {
        if (mSelectedIndex != index && mSrcArray != null && index < mSrcArray.length) {
            mSelectedIndex = index;
            mHandler.removeCallbacks(mSelectedAnimRunnable);
            mPaintSelectedColor = mActiveColor;
            if (mSelectedIndex >= 0) {
                if (mIsSelectedAnim) {
                    mHandler.postDelayed(mSelectedAnimRunnable, mSelectedAnimInterval);
                } else {
                    mPaintSelectedColor = mSelectedColor;
                    invalidate();
                }
            }
        }
    }

    /**
     * Gets the current highlight column.
     *
     * @return
     */
    public int getSelected() {
        return mSelectedIndex;
    }

    /**
     * Set the invalid item to display the color.
     *
     * @param color
     */
    public void setNullColor(int color) {
        mNullColor = color;
    }

    /**
     * Set the valid item to display the color.
     *
     * @param color
     */
    public void setActiveColor(int color) {
        mActiveColor = color;
    }

    /**
     * Set the highlighted item to display the color.
     *
     * @param color
     */
    public void setSelectedColor(int color) {
        mSelectedColor = color;
    }

    /**
     * Set the number of rows
     *
     * @param row
     */
    public void setRow(int row) {
        mRow = row;
    }

    /**
     * Set the number of column
     *
     * @param column
     */
    public void setColumn(int column) {
        mColumn = column;
    }

    /**
     * Set row spacing
     *
     * @param rowPadding
     */
    public void setRowPadding(int rowPadding) {
        mRowPadding = rowPadding;
    }

    /**
     * Set column spacing
     *
     * @param columnPadding
     */
    public void setColumnPadding(int columnPadding) {
        mColumnPadding = columnPadding;
    }

    /**
     * Set support for entry animation.
     *
     * @param isSupport
     */
    public void setSupportEnterAnim(boolean isSupport) {
        mIsEnterAnim = isSupport;
    }

    /**
     * Set support for highlighted animation.
     *
     * @param isSupport
     */
    public void setSupportSelectedAnim(boolean isSupport) {
        mIsSelectedAnim = isSupport;
    }

    /**
     * Set the time interval for enter animation.
     *
     * @param interval
     */
    public void setEnterAnimInterval(int interval) {
        mEnterAnimInterval = interval;
    }

    /**
     * Set the time interval for highlighted animation.
     *
     * @param interval
     */
    public void setSelectedAnimInterval(int interval) {
        mSelectedAnimInterval = interval;
    }

    /**
     * Set support edit function.
     *
     * @param isSupport
     */
    public void setSupportEdit(boolean isSupport) {
        mIsSupportEdit = isSupport;
    }

    /**
     * Enter animation runnable
     */
    private Runnable mEnterAnimRunnable = new Runnable() {
        @Override
        public void run() {
            if (mAnimCount < mRow) {
                mAnimCount++;
                for (int i = 0; i < (mSrcArray.length < mColumn ? mSrcArray.length : mColumn); i++) {
                    mArray[i] = (mArray[i] < mSrcArray[i] ? mArray[i] + 1 : mArray[i]);
                }
                invalidate();
                mHandler.postDelayed(this, mEnterAnimInterval);
            }
        }
    };

    /**
     * Highlighted animation runnable
     */
    private Runnable mSelectedAnimRunnable = new Runnable() {
        @Override
        public void run() {
            if (mSelectedIndex >= 0) {
                if (mPaintSelectedColor == mSelectedColor) {
                    mPaintSelectedColor = mNullColor;
                } else {
                    mPaintSelectedColor = mSelectedColor;
                }
                invalidate();
                mHandler.postDelayed(this, mSelectedAnimInterval);
            }
        }
    };
}
