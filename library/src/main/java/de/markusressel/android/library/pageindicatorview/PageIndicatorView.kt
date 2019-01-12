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

import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import java.util.*

/**
 * Page indicator view
 *
 *
 * Used as a "current page" indicator for a multi page view (i.e. navigation drawer)
 *
 *
 * Created by Markus on 06.07.2016.
 */
class PageIndicatorView : LinearLayout {

    /**
     * Get currently active page
     *
     * @return index of currently active page
     */
    var currentPage = 0
        private set

    var pageCount = 0
        set(value) {
            if (value <= 0) {
                Log.w(TAG, "Page count must be greater than 0 but was $value!")
                return
            }
            field = value

            init()

            invalidate()
            requestLayout()
        }

    /**
     * The size (in pixel) of an active indicator
     */
    var activeIndicatorSize = 0F
        set(value) {
            field = value

            init()
            invalidate()
            requestLayout()
        }

    /**
     * The size (in pixel) of an inactive indicator
     */
    var inactiveIndicatorSize = 0F
        set(value) {
            field = value

            init()
            invalidate()
            requestLayout()
        }

    /**
     * The gap size (in pixel) between indicators
     */
    var indicatorGap = 0F
        set(value) {
            field = value

            init()
            invalidate()
            requestLayout()
        }

    /**
     * The fill color of an active indicator
     */
    @ColorInt
    var activeIndicatorFillColor = 0
        set(value) {
            field = value

            init()
            invalidate()
            requestLayout()
        }


    /**
     * The stroke color of an active indicator
     */
    @ColorInt
    var activeIndicatorStrokeColor = 0
        set(value) {
            field = value

            init()
            invalidate()
            requestLayout()
        }

    /**
     * The stroke width of an active indicator
     */
    var activeIndicatorStrokeWidth = 0F
        set(value) {
            field = value

            for (i in 0 until pageCount) {
                if (i == currentPage) {
                    val circle = indicatorViews[i]
                    circle.strokeWidth = value
                    break
                }
            }

            init()

            invalidate()
            requestLayout()
        }

    /**
     * The fill color of an inactive indicator
     */
    @ColorInt
    var inactiveIndicatorFillColor = 0
        set(value) {
            field = value

            init()
            invalidate()
            requestLayout()
        }

    /**
     * The stroke color of an inactive indicator
     */
    @ColorInt
    var inactiveIndicatorStrokeColor = 0
        set(value) {
            field = value

            init()
            invalidate()
            requestLayout()
        }

    /**
     * The stroke width of an inactive indicator
     */
    var inactiveIndicatorStrokeWidth = 0F
        set(value) {
            field = value

            for (i in 0 until pageCount) {
                if (i != currentPage) {
                    val circle = indicatorViews[i]
                    circle.strokeWidth = value
                }
            }

            init()

            invalidate()
            requestLayout()
        }

    private var onIndicatorClickedListener: OnIndicatorClickedListener? = null

    private val indicatorViews = ArrayList<CircleIndicatorView>()

    private val activeIndicator: CircleIndicatorView
        get() {
            val activeCircleIndicator = CircleIndicatorView(context)
            activeCircleIndicator.diameter = activeIndicatorSize
            activeCircleIndicator.fillColor = activeIndicatorFillColor
            activeCircleIndicator.strokeColor = activeIndicatorStrokeColor
            activeCircleIndicator.strokeWidth = activeIndicatorStrokeWidth

            return activeCircleIndicator
        }

    private val inactiveIndicator: CircleIndicatorView
        get() {
            val inactiveCircleIndicator = CircleIndicatorView(context)
            inactiveCircleIndicator.diameter = inactiveIndicatorSize
            inactiveCircleIndicator.fillColor = inactiveIndicatorFillColor
            inactiveCircleIndicator.strokeColor = inactiveIndicatorStrokeColor
            inactiveCircleIndicator.strokeWidth = inactiveIndicatorStrokeWidth

            return inactiveCircleIndicator
        }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        readArguments(context, attrs)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        readArguments(context, attrs)
    }

    private fun readArguments(context: Context, attrs: AttributeSet?) {
        // read XML attributes
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.PageIndicatorView,
                0, 0)

        try {
            currentPage = a.getInt(R.styleable.PageIndicatorView_piv_initialPageIndex, DEFAULT_CURRENT_PAGE_INDEX)
            pageCount = a.getInt(R.styleable.PageIndicatorView_piv_pageCount, DEFAULT_PAGE_COUNT)

            activeIndicatorSize = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_activeIndicatorFillSize,
                    Math.round(DimensionHelper.pxFromDp(context, DEFAULT_ACTIVE_INDICATOR_SIZE_DP))).toFloat()
            activeIndicatorFillColor = a.getColor(R.styleable.PageIndicatorView_piv_activeIndicatorFillColor, DEFAULT_ACTIVE_INDICATOR_FILL_COLOR)
            activeIndicatorStrokeColor = a.getColor(R.styleable.PageIndicatorView_piv_activeIndicatorStrokeColor, activeIndicatorFillColor)
            activeIndicatorStrokeWidth = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_activeIndicatorStrokeWidth,
                    Math.round(DimensionHelper.pxFromDp(context, DEFAULT_ACTIVE_INDICATOR_STROKE_WIDTH_DP))).toFloat()

            inactiveIndicatorSize = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_inactiveIndicatorFillSize,
                    Math.round(DimensionHelper.pxFromDp(context, DEFAULT_INACTIVE_INDICATOR_SIZE_DP))).toFloat()
            inactiveIndicatorFillColor = a.getColor(R.styleable.PageIndicatorView_piv_inactiveIndicatorFillColor, DEFAULT_INACTIVE_INDICATOR_FILL_COLOR)
            inactiveIndicatorStrokeColor = a.getColor(R.styleable.PageIndicatorView_piv_inactiveIndicatorStrokeColor, inactiveIndicatorFillColor)
            inactiveIndicatorStrokeWidth = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_inactiveIndicatorStrokeWidth,
                    Math.round(DimensionHelper.pxFromDp(context, DEFAULT_INACTIVE_INDICATOR_STROKE_WIDTH_DP))).toFloat()

            indicatorGap = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_indicatorGap,
                    Math.round(DimensionHelper.pxFromDp(context, DEFAULT_INDICATOR_GAP_DP))).toFloat()
        } finally {
            a.recycle()
        }

        init()
    }

    private fun init() {
        createIndicators()
        gravity = Gravity.CENTER
    }

    private fun createIndicators() {
        removeAllViews()
        indicatorViews.clear()

        val indicatorSize = Math.max(activeIndicatorSize, inactiveIndicatorSize).toInt() +
                Math.max(activeIndicatorStrokeWidth, inactiveIndicatorStrokeWidth).toInt() + 1

        for (i in 0 until pageCount) {
            val indicator = when (i) {
                currentPage -> activeIndicator
                else -> inactiveIndicator
            }

            indicator.setOnClickListener {
                if (onIndicatorClickedListener != null) {
                    onIndicatorClickedListener!!.onIndicatorClicked(this@PageIndicatorView, i)
                }
            }

            addView(indicator, indicatorSize, indicatorSize)

            val lp = indicator.layoutParams as LinearLayout.LayoutParams

            val isFirst = i == 0
            val isLast = i == pageCount - 1
            if (!isFirst) {
                lp.leftMargin = indicatorGap.toInt() / 2
            }
            if (!isLast) {
                lp.rightMargin = indicatorGap.toInt() / 2
            }
            indicator.layoutParams = lp

            indicatorViews.add(indicator)
        }
    }

    private fun animateToActiveIndicator(index: Int) {
        val indicatorView = indicatorViews[index]

        val sizeAnimator = ValueAnimator.ofObject(
                FloatEvaluator(), indicatorView.diameter, activeIndicatorSize)
        sizeAnimator.duration = ANIMATION_DURATION.toLong()
        sizeAnimator.addUpdateListener { animation -> indicatorView.diameter = animation.animatedValue as Float }

        val fillColorAnimator = ValueAnimator.ofObject(
                ArgbEvaluator.newInstance, indicatorView.fillColor, activeIndicatorFillColor)
        fillColorAnimator.duration = ANIMATION_DURATION.toLong()
        fillColorAnimator.addUpdateListener { animation -> indicatorView.fillColor = animation.animatedValue as Int }

        val strokeColorAnimator = ValueAnimator.ofObject(
                ArgbEvaluator.newInstance, indicatorView.strokeColor, activeIndicatorStrokeColor)
        strokeColorAnimator.duration = ANIMATION_DURATION.toLong()
        strokeColorAnimator.addUpdateListener { animation -> indicatorView.strokeColor = animation.animatedValue as Int }

        val strokeWidthAnimator = ValueAnimator.ofObject(
                FloatEvaluator(), indicatorView.strokeWidth, activeIndicatorStrokeWidth)
        strokeWidthAnimator.duration = ANIMATION_DURATION.toLong()
        strokeWidthAnimator.addUpdateListener { animation -> indicatorView.strokeWidth = animation.animatedValue as Float }

        sizeAnimator.start()
        fillColorAnimator.start()
        strokeColorAnimator.start()
        strokeWidthAnimator.start()
    }

    private fun animateToInactiveIndicator(index: Int) {
        val indicatorView = indicatorViews[index]

        val sizeAnimator = ValueAnimator.ofObject(
                FloatEvaluator(), indicatorView.diameter, inactiveIndicatorSize)
        sizeAnimator.duration = ANIMATION_DURATION.toLong()
        sizeAnimator.addUpdateListener { animation -> indicatorView.diameter = animation.animatedValue as Float }

        val fillColorAnimator = ValueAnimator.ofObject(
                ArgbEvaluator.newInstance, indicatorView.fillColor, inactiveIndicatorFillColor)
        fillColorAnimator.duration = ANIMATION_DURATION.toLong()
        fillColorAnimator.addUpdateListener { animation -> indicatorView.fillColor = animation.animatedValue as Int }

        val strokeColorAnimator = ValueAnimator.ofObject(
                ArgbEvaluator.newInstance, indicatorView.strokeColor, inactiveIndicatorStrokeColor)
        strokeColorAnimator.duration = ANIMATION_DURATION.toLong()
        strokeColorAnimator.addUpdateListener { animation -> indicatorView.strokeColor = animation.animatedValue as Int }

        val strokeWidthAnimator = ValueAnimator.ofObject(
                FloatEvaluator(), indicatorView.strokeWidth, inactiveIndicatorStrokeWidth)
        strokeWidthAnimator.duration = ANIMATION_DURATION.toLong()
        strokeWidthAnimator.addUpdateListener { animation -> indicatorView.strokeWidth = animation.animatedValue as Float }

        sizeAnimator.start()
        fillColorAnimator.start()
        strokeColorAnimator.start()
        strokeWidthAnimator.start()
    }

    /**
     * Set the size of an active indicator
     *
     * @param activeIndicatorSize size in pixel
     */
    fun setActiveIndicatorSize(activeIndicatorSize: Int) {
        this.activeIndicatorSize = activeIndicatorSize.toFloat()
    }

    /**
     * Set current page (indicator will be updated accordingly)
     *
     * @param index    Index of current page
     * @param animated index change will be animated if set to true
     */
    fun setCurrentPage(index: Int, animated: Boolean) {
        if (index >= pageCount || index < 0) {
            Log.w(TAG, "Invalid index! PageCount: $pageCount, requested index: $index")
            return
        }

        if (animated) {
            animateToInactiveIndicator(currentPage)
            animateToActiveIndicator(index)
        } else {
            val currentIndicatorView = indicatorViews[currentPage]
            currentIndicatorView.diameter = inactiveIndicatorSize
            currentIndicatorView.fillColor = inactiveIndicatorFillColor
            currentIndicatorView.strokeColor = inactiveIndicatorStrokeColor
            currentIndicatorView.strokeWidth = inactiveIndicatorStrokeWidth

            val newIndicatorView = indicatorViews[index]
            newIndicatorView.diameter = activeIndicatorSize
            newIndicatorView.fillColor = activeIndicatorFillColor
            newIndicatorView.strokeColor = activeIndicatorStrokeColor
            newIndicatorView.strokeWidth = activeIndicatorStrokeWidth
        }

        currentPage = index

        invalidate()
        requestLayout()
    }

    /**
     * Set a listener to OnClick events from indicators.
     *
     * @param listener OnIndicatorClickedListener
     */
    fun setOnIndicatorClickedListener(listener: OnIndicatorClickedListener) {
        onIndicatorClickedListener = listener
    }

    companion object {
        private const val TAG = "PageIndicatorView"

        private const val ANIMATION_DURATION = 250
        private const val DEFAULT_INDICATOR_GAP_DP = 5f // dp values will be converted to pixels at runtime
        private const val DEFAULT_ACTIVE_INDICATOR_SIZE_DP = 7f
        private const val DEFAULT_ACTIVE_INDICATOR_STROKE_WIDTH_DP = 0f
        private const val DEFAULT_INACTIVE_INDICATOR_SIZE_DP = 5f
        private const val DEFAULT_INACTIVE_INDICATOR_STROKE_WIDTH_DP = 0f
        private const val DEFAULT_PAGE_COUNT = 1
        private const val DEFAULT_CURRENT_PAGE_INDEX = 0
        private const val DEFAULT_ACTIVE_INDICATOR_FILL_COLOR = Color.WHITE
        private const val DEFAULT_INACTIVE_INDICATOR_FILL_COLOR = Color.GRAY
    }
}