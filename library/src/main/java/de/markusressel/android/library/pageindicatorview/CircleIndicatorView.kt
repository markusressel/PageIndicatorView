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

package de.markusressel.android.library.pageindicatorview

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Circle indicator for use with PageIndicatorView
 *
 *
 * Created by Markus on 13.07.2016.
 */
internal class CircleIndicatorView : View {

    /**
     * The diameter of this Indicator
     */
    var diameter: Float = 0.toFloat()
        set(value) {
            field = value

            invalidate()
            requestLayout()
        }

    /**
     * Indicator fill color
     */
    @ColorInt
    var fillColor: Int = 0
        set(value) {
            field = value
            indicatorFillPaint.color = value

            invalidate()
            requestLayout()
        }


    /**
     * Indicator stroke color
     */
    @ColorInt
    var strokeColor: Int = 0
        set(value) {
            field = value
            indicatorStrokePaint.color = value

            invalidate()
            requestLayout()
        }

    /**
     * Indicator stroke width
     */
    var strokeWidth: Float = 0.toFloat()
        set(value) {
            if (value < 0) {
                Log.w(TAG, "invalid range, param has to be >= 0 but was $value")
            }

            field = value
            indicatorStrokePaint.strokeWidth = value

            invalidate()
            requestLayout()
        }

    private var indicatorFillPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = fillColor
        style = Paint.Style.FILL
    }
    private var indicatorStrokePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = strokeColor
        strokeWidth = strokeWidth
        style = Paint.Style.STROKE
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        readArguments(context, attrs)
        init()
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        readArguments(context, attrs)
        init()
    }

    private fun readArguments(context: Context, attrs: AttributeSet?) {
        // read XML attributes
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CircleIndicatorView,
                0, 0)

        try {
            diameter = a.getDimensionPixelSize(R.styleable.CircleIndicatorView_civ_diameter, DEFAULT_DIAMETER).toFloat()
            fillColor = a.getColor(R.styleable.CircleIndicatorView_civ_fillColor, DEFAULT_FILL_COLOR)
            strokeColor = a.getColor(R.styleable.CircleIndicatorView_civ_strokeColor, DEFAULT_STROKE_COLOR)
            strokeWidth = a.getDimensionPixelSize(R.styleable.CircleIndicatorView_civ_strokeWidth, DEFAULT_STROKE_WIDTH).toFloat()
        } finally {
            a.recycle()
        }
    }

    private fun init() {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val viewWidth = Math.ceil(diameter.toDouble()).toInt() + Math.ceil(strokeWidth.toDouble()).toInt() * 2 + this.paddingLeft + this.paddingRight
        val viewHeight = Math.ceil(diameter.toDouble()).toInt() + Math.ceil(strokeWidth.toDouble()).toInt() * 2 + this.paddingTop + this.paddingBottom

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        //Measure Width
        val width = when (widthMode) {
            View.MeasureSpec.EXACTLY -> //Must be this size
                widthSize
            View.MeasureSpec.AT_MOST -> //Can't be bigger than...
                Math.min(viewWidth, widthSize)
            else -> //Be whatever you want
                viewWidth
        }

        //Measure Height
        val height = when {
            heightMode == View.MeasureSpec.EXACTLY || widthMode == View.MeasureSpec.EXACTLY -> //Must be this size
                heightSize
            heightMode == View.MeasureSpec.AT_MOST -> //Can't be bigger than...
                Math.min(viewHeight, heightSize)
            else -> //Be whatever you want
                viewHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val x = (width / 2).toFloat()
        val y = (height / 2).toFloat()
        val radius = diameter / 2.0f

        canvas.drawCircle(x, y, radius, indicatorFillPaint)
        canvas.drawCircle(x, y, radius, indicatorStrokePaint)
    }

    companion object {
        private const val TAG = "CircleIndicatorView"

        private const val DEFAULT_DIAMETER = 10
        private const val DEFAULT_FILL_COLOR = Color.WHITE
        private const val DEFAULT_STROKE_WIDTH = 1
        private const val DEFAULT_STROKE_COLOR = Color.WHITE
    }
}
