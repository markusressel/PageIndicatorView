/*
 * Copyright (c) 2016 Markus Ressel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.markusressel.android.library.pageindicatorview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Circle indicator for use with PageIndicatorView
 * <p/>
 * Created by Markus on 13.07.2016.
 */
class CircleIndicatorView extends View {

    private static final String TAG = "CircleIndicatorView";

    private static final int DEFAULT_DIAMETER = 10;
    private static final int DEFAULT_FILL_COLOR = Color.WHITE;
    private static final int DEFAULT_STROKE_WIDTH = 1;
    private static final int DEFAULT_STROKE_COLOR = Color.WHITE;

    private float diameter;
    private int fillColor;
    private int strokeColor;
    private float strokeWidth;

    private Paint indicatorFillPaint;
    private Paint indicatorStrokePaint;

    public CircleIndicatorView(Context context) {
        this(context, null);
    }

    public CircleIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        readArguments(context, attrs);
        init();
    }

    @TargetApi(21)
    public CircleIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        readArguments(context, attrs);
        init();
    }

    private void readArguments(Context context, AttributeSet attrs) {
        // read XML attributes
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleIndicatorView,
                0, 0);

        try {
            diameter = a.getDimensionPixelSize(R.styleable.CircleIndicatorView_civ_diameter, DEFAULT_DIAMETER);
            fillColor = a.getColor(R.styleable.CircleIndicatorView_civ_fillColor, DEFAULT_FILL_COLOR);
            strokeColor = a.getColor(R.styleable.CircleIndicatorView_civ_strokeColor, DEFAULT_STROKE_COLOR);
            strokeWidth = a.getDimensionPixelSize(R.styleable.CircleIndicatorView_civ_strokeWidth, DEFAULT_STROKE_WIDTH);
        } finally {
            a.recycle();
        }
    }

    private void init() {
        indicatorFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorFillPaint.setColor(fillColor);
        indicatorFillPaint.setStyle(Paint.Style.FILL);

        indicatorStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorStrokePaint.setColor(strokeColor);
        indicatorStrokePaint.setStrokeWidth(strokeWidth);
        indicatorStrokePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = (int) Math.ceil(diameter) + (int) Math.ceil(strokeWidth) * 2 + this.getPaddingLeft() + this.getPaddingRight();
        int viewHeight = (int) Math.ceil(diameter) + (int) Math.ceil(strokeWidth) * 2 + this.getPaddingTop() + this.getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(viewWidth, widthSize);
        } else {
            //Be whatever you want
            width = viewWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(viewHeight, heightSize);
        } else {
            //Be whatever you want
            height = viewHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float radius = diameter / 2.0f;

        canvas.drawCircle(x, y, radius, indicatorFillPaint);
        canvas.drawCircle(x, y, radius, indicatorStrokePaint);
    }

    /**
     * Get current diameter value
     *
     * @return current diameter
     */
    public float getDiameter() {
        return diameter;
    }

    /**
     * Set new Diameter of this Indicator
     *
     * @param newDiameter new diameter value
     */
    public void setDiameter(float newDiameter) {
        this.diameter = newDiameter;

        invalidate();
        requestLayout();
    }

    /**
     * Get current fill color
     *
     * @return fill color
     */
    @ColorInt
    public int getFillColor() {
        return fillColor;
    }

    /**
     * Set fill color
     *
     * @param fillColor color
     */
    public void setFillColor(@ColorInt int fillColor) {
        this.fillColor = fillColor;
        indicatorFillPaint.setColor(fillColor);

        invalidate();
        requestLayout();
    }

    /**
     * Get current stroke color
     *
     * @return stroke color
     */
    @ColorInt
    public int getStrokeColor() {
        return strokeColor;
    }

    /**
     * Set stroke color
     *
     * @param strokeColor color
     */
    public void setStrokeColor(@ColorInt int strokeColor) {
        this.strokeColor = strokeColor;
        indicatorStrokePaint.setColor(strokeColor);

        invalidate();
        requestLayout();
    }

    /**
     * Get current stroke width
     *
     * @return stroke width in pixel
     */
    public float getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * Set the stroke width in pixel
     * <p>
     * {@inheritDoc }
     *
     * @param strokeWidth stroke width in pixel
     */
    public void setStrokeWidth(float strokeWidth) {
        if (strokeWidth < 0) {
            Log.w(TAG, "invalid range, param has to be >= 0 but was " + strokeWidth);
        }

        this.strokeWidth = strokeWidth;
        indicatorStrokePaint.setStrokeWidth(strokeWidth);

        invalidate();
        requestLayout();
    }
}
