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
import android.util.AttributeSet;
import android.view.View;

/**
 * Circle indicator for use with PageIndicatorView
 * <p/>
 * Created by Markus on 13.07.2016.
 */
public class CircleIndicatorView extends View {

    private static final int DEFAULT_DIAMETER = 10;
    private static final int DEFAULT_FILL_COLOR = Color.WHITE;
    private static final int DEFAULT_STROKE_COLOR = Color.WHITE;

    private float diameter;
    private int fillColor;
    private int strokeColor;

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
        indicatorStrokePaint.setStyle(Paint.Style.STROKE);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        //Get the width measurement
//        int widthSize = View.resolveSize(getDesiredWidth(), widthMeasureSpec);
//
//        //Get the height measurement
//        int heightSize = View.resolveSize(getDesiredHeight(), heightMeasureSpec);
//
//        //MUST call this to store the measurements
//        setMeasuredDimension(widthSize, heightSize);
//    }
//
//    private int getDesiredWidth() {
//        return (int) Math.ceil((double) diameter) + 1;
//    }
//
//    private int getDesiredHeight() {
//        return (int) Math.ceil((double) diameter) + 1;
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float radius = diameter / 2;

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
        diameter = newDiameter;

        invalidate();
        requestLayout();
    }

    /**
     * Get current fill color
     *
     * @return fill color
     */
    public int getFillColor() {
        return fillColor;
    }

    /**
     * Set fill color
     *
     * @param fillColor color
     */
    public void setFillColor(int fillColor) {
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
    public int getStrokeColor() {
        return strokeColor;
    }

    /**
     * Set stroke color
     *
     * @param strokeColor color
     */
    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        indicatorStrokePaint.setColor(strokeColor);

        invalidate();
        requestLayout();
    }
}
