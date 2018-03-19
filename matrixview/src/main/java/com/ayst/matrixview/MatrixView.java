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
    private int mNegativeColor = 0x23000000;

    /**
     * Valid item color
     */
    private int mActiveColor = 0xb3ffffff;

    /**
     * Column highlighted color
     */
    private int mHighlightedColor = 0xfffdfe29;

    private int mPaintHighlightedColor = mHighlightedColor;

    /**
     * The number of rows
     */
    private int mRowNumber = 10;

    /**
     * The number of columns
     */
    private int mColumnNumber = 10;

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
    private int mHighlightedIndex = -1;

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

    /**
     * min value.
     */
    private int mMinValue = 0;

    /**
     * max value.
     */
    private int mMaxValue = 0;

    private int mRowBiases = 0;
    private int mColumnBiases = 0;

    private Paint mNegativePaint;
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

        mNegativeColor = a.getColor(R.styleable.MatrixView_negativeColor, mNegativeColor);
        mActiveColor = a.getColor(R.styleable.MatrixView_activeColor, mActiveColor);
        mHighlightedColor = a.getColor(R.styleable.MatrixView_highlightedColor, mHighlightedColor);
        mRowNumber = a.getInteger(R.styleable.MatrixView_rowNumber, mRowNumber);
        mColumnNumber = a.getInteger(R.styleable.MatrixView_columnNumber, mColumnNumber);
        mRowPadding = a.getDimensionPixelSize(R.styleable.MatrixView_rowPadding, mRowPadding);
        mColumnPadding = a.getDimensionPixelSize(R.styleable.MatrixView_columnPadding, mColumnPadding);
        mIsEnterAnim = a.getBoolean(R.styleable.MatrixView_enterAnim, mIsEnterAnim);
        mIsSelectedAnim = a.getBoolean(R.styleable.MatrixView_highlightedAnim, mIsSelectedAnim);
        mEnterAnimInterval = a.getInteger(R.styleable.MatrixView_enterAnimInterval, mEnterAnimInterval);
        mSelectedAnimInterval = a.getInteger(R.styleable.MatrixView_highlightedAnimInterval, mSelectedAnimInterval);
        mHighlightedIndex = a.getInteger(R.styleable.MatrixView_highlightedIndex, mHighlightedIndex);
        mIsSupportEdit = a.getBoolean(R.styleable.MatrixView_enableEdit, mIsSupportEdit);
        mMinValue = a.getInteger(R.styleable.MatrixView_minEditValue, 0);
        mMaxValue = a.getInteger(R.styleable.MatrixView_maxEditValue, mRowNumber);
        mMinValue = (mMinValue < 0 ? 0 : mMinValue);
        mMaxValue = (mMaxValue > mRowNumber ? mRowNumber : mMaxValue);
        a.recycle();

        init();
    }

    private void init() {
        this.setFocusable(mIsSupportEdit);
        this.setFocusableInTouchMode(mIsSupportEdit);

        mNegativePaint = new Paint();
        mNegativePaint.setAntiAlias(true);
        mNegativePaint.setStyle(Paint.Style.FILL);
        mNegativePaint.setColor(mNegativeColor);

        mActivePaint = new Paint();
        mActivePaint.setAntiAlias(true);
        mActivePaint.setStyle(Paint.Style.FILL);
        mActivePaint.setColor(mActiveColor);

        mArray = new int[mColumnNumber];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mItemWidth = (getWidth() - (mRowPadding * (mColumnNumber - 1))) / mColumnNumber;
        mItemHeight = (getHeight() - (mColumnPadding * (mRowNumber - 1))) / mRowNumber;

        mRowBiases = (getHeight() - mRowNumber * mItemHeight - (mColumnPadding * (mRowNumber - 1))) / 2;
        mColumnBiases = (getWidth() - mColumnNumber * mItemWidth - (mRowPadding * (mColumnNumber - 1))) / 2;

        for (int i = 0; i < mColumnNumber; i++) {
            for (int j = 0; j < mRowNumber; j++) {
                Rect rect = new Rect();
                rect.left = mColumnPadding * i + mItemWidth * i + mColumnBiases;
                rect.right = rect.left + mItemWidth;
                rect.top = mRowPadding * j + mItemHeight * j + mRowBiases;
                rect.bottom = rect.top + mItemHeight;
                if (mArray != null && i < mArray.length && (mRowNumber - j) <= mArray[i]) {
                    if (i == mHighlightedIndex) {
                        mActivePaint.setColor(mPaintHighlightedColor);
                    } else {
                        mActivePaint.setColor(mActiveColor);
                    }
                    canvas.drawRect(rect, mActivePaint);
                } else {
                    canvas.drawRect(rect, mNegativePaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsSupportEdit) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int width = getWidth();
            int height = getHeight();
            int row = mRowNumber;
            int column = mColumnNumber-1;

//            if (x < 0+mRowBiases || x > width-mRowBiases || y < 0+mColumnBiases || y > height-mColumnBiases) {
//                return true;
//            }

            for (int i = 0; i < mColumnNumber; i++) {
                if (x < (i + 1) * (mItemWidth + mColumnPadding)) {
                    column = i;
                    break;
                }
            }
            for (int i = 0; i < mRowNumber; i++) {
                if (y < (i + 1) * (mItemHeight + mRowPadding)) {
                    row = i;
                    break;
                }
            }

            int value = mRowNumber - row;
            mArray[column] = (value < mMinValue ? mMinValue : (value > mMaxValue ? mMaxValue : value));
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
                for (int i = 0; i < (mSrcArray.length < mColumnNumber ? mSrcArray.length : mColumnNumber); i++) {
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

        mPaintHighlightedColor = mActiveColor;
        mAnimCount = 0;
        mHighlightedIndex = -1;

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
    public void setHighlight(int index) {
        if (mHighlightedIndex != index) {
            mHighlightedIndex = index;
            mHandler.removeCallbacks(mSelectedAnimRunnable);
            mPaintHighlightedColor = mActiveColor;
            if (index >=0 && index < mColumnNumber) {
                if (mHighlightedIndex >= 0) {
                    if (mIsSelectedAnim) {
                        mHandler.postDelayed(mSelectedAnimRunnable, mSelectedAnimInterval);
                    } else {
                        mPaintHighlightedColor = mHighlightedColor;
                        invalidate();
                    }
                }
            } else {
                invalidate();
            }
        }
    }

    /**
     * Gets the current highlight column.
     *
     * @return
     */
    public int getHighlight() {
        return mHighlightedIndex;
    }

    /**
     * Set the negative item to display the color.
     *
     * @param color
     */
    public void setNegativeColor(int color) {
        mNegativeColor = color;
    }

    /**
     * Set the active item to display the color.
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
    public void setHighlightedColor(int color) {
        mHighlightedColor = color;
    }

    /**
     * Set the number of rows
     *
     * @param row
     */
    public void setRowNumber(int row) {
        mRowNumber = row;
    }

    /**
     * Set the number of column
     *
     * @param column
     */
    public void setColumnNumber(int column) {
        mColumnNumber = column;
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
    public void setSupportHighlightedAnim(boolean isSupport) {
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
    public void setHighlightedAnimInterval(int interval) {
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
     * Set edit min value.
     *
     * @param value
     */
    public void setMinValue(int value) {
        mMinValue = (value < 0 ? 0 : value);
    }

    /**
     * Set edit max value.
     *
     * @param value
     */
    public void setMaxValue(int value) {
        mMaxValue = (value > mRowNumber ? mRowNumber : value);
    }

    /**
     * Enter animation runnable
     */
    private Runnable mEnterAnimRunnable = new Runnable() {
        @Override
        public void run() {
            if (mAnimCount < mRowNumber) {
                mAnimCount++;
                for (int i = 0; i < (mSrcArray.length < mColumnNumber ? mSrcArray.length : mColumnNumber); i++) {
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
            if (mHighlightedIndex >= 0) {
                if (mPaintHighlightedColor == mHighlightedColor) {
                    mPaintHighlightedColor = mNegativeColor;
                } else {
                    mPaintHighlightedColor = mHighlightedColor;
                }
                invalidate();
                mHandler.postDelayed(this, mSelectedAnimInterval);
            }
        }
    };
}
