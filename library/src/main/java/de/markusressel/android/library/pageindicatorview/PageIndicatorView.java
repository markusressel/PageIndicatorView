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

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Page indicator view
 * <p/>
 * Used as a "current page" indicator for a multi page view (i.e. navigation drawer)
 * <p/>
 * Created by Markus on 06.07.2016.
 */
public class PageIndicatorView extends LinearLayout {

    private static final String TAG = "PageIndicatorView";

    private static final int ANIMATION_DURATION = 250;
    private static final float DEFAULT_INDICATOR_GAP_DP = 5; // dp values will be converted to pixels at runtime
    private static final float DEFAULT_ACTIVE_INDICATOR_SIZE_DP = 7;
    private static final float DEFAULT_ACTIVE_INDICATOR_STROKE_WIDTH_DP = 0;
    private static final float DEFAULT_INACTIVE_INDICATOR_SIZE_DP = 5;
    private static final float DEFAULT_INACTIVE_INDICATOR_STROKE_WIDTH_DP = 0;
    private static final int DEFAULT_PAGE_COUNT = 1;
    private static final int DEFAULT_CURRENT_PAGE_INDEX = 0;
    private static final int DEFAULT_ACTIVE_INDICATOR_FILL_COLOR = Color.WHITE;
    private static final int DEFAULT_INACTIVE_INDICATOR_FILL_COLOR = Color.GRAY;

    private int currentPage;
    private int pageCount;
    private float activeIndicatorSize;
    private float inactiveIndicatorSize;
    private float indicatorGap;
    private int activeIndicatorFillColor;
    private int activeIndicatorStrokeColor;
    private float activeIndicatorStrokeWidth;
    private int inactiveIndicatorFillColor;
    private int inactiveIndicatorStrokeColor;
    private float inactiveIndicatorStrokeWidth;

    private OnIndicatorClickedListener onIndicatorClickedListener;

    private ArrayList<CircleIndicatorView> indicatorViews = new ArrayList<>();

    public PageIndicatorView(Context context) {
        this(context, null);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        readArguments(context, attrs);
    }

    @TargetApi(21)
    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        readArguments(context, attrs);
    }

    private void readArguments(Context context, AttributeSet attrs) {
        // read XML attributes
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PageIndicatorView,
                0, 0);

        try {
            currentPage = a.getInt(R.styleable.PageIndicatorView_piv_initialPageIndex, DEFAULT_CURRENT_PAGE_INDEX);
            pageCount = a.getInt(R.styleable.PageIndicatorView_piv_pageCount, DEFAULT_PAGE_COUNT);

            activeIndicatorSize = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_activeIndicatorFillSize,
                    Math.round(DimensionHelper.pxFromDp(context, DEFAULT_ACTIVE_INDICATOR_SIZE_DP)));
            activeIndicatorFillColor = a.getColor(R.styleable.PageIndicatorView_piv_activeIndicatorFillColor, DEFAULT_ACTIVE_INDICATOR_FILL_COLOR);
            activeIndicatorStrokeColor = a.getColor(R.styleable.PageIndicatorView_piv_activeIndicatorStrokeColor, activeIndicatorFillColor);
            activeIndicatorStrokeWidth = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_activeIndicatorStrokeWidth,
                    Math.round(DimensionHelper.pxFromDp(context, DEFAULT_ACTIVE_INDICATOR_STROKE_WIDTH_DP)));

            inactiveIndicatorSize = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_inactiveIndicatorFillSize,
                    Math.round(DimensionHelper.pxFromDp(context, DEFAULT_INACTIVE_INDICATOR_SIZE_DP)));
            inactiveIndicatorFillColor = a.getColor(R.styleable.PageIndicatorView_piv_inactiveIndicatorFillColor, DEFAULT_INACTIVE_INDICATOR_FILL_COLOR);
            inactiveIndicatorStrokeColor = a.getColor(R.styleable.PageIndicatorView_piv_inactiveIndicatorStrokeColor, inactiveIndicatorFillColor);
            inactiveIndicatorStrokeWidth = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_inactiveIndicatorStrokeWidth,
                    Math.round(DimensionHelper.pxFromDp(context, DEFAULT_INACTIVE_INDICATOR_STROKE_WIDTH_DP)));

            indicatorGap = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_indicatorGap,
                    Math.round(DimensionHelper.pxFromDp(context, DEFAULT_INDICATOR_GAP_DP)));
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        createIndicators();
        setGravity(Gravity.CENTER);
    }

    private void createIndicators() {
        removeAllViews();
        indicatorViews.clear();

        int indicatorSize = (int) Math.max(activeIndicatorSize, inactiveIndicatorSize) +
                (int) Math.max(activeIndicatorStrokeWidth, inactiveIndicatorStrokeWidth) + 1;

        for (int i = 0; i < pageCount; i++) {
            boolean isFirst = (i == 0);
            boolean isLast = (i == pageCount - 1);

            final int currentIndex = i;

            CircleIndicatorView indicator;
            if (i == currentPage) {
                indicator = getActiveIndicator();
            } else {
                indicator = getInactiveIndicator();
            }

            final PageIndicatorView pageIndicatorView = this;
            indicator.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onIndicatorClickedListener != null) {
                        onIndicatorClickedListener.onIndicatorClicked(pageIndicatorView, currentIndex);
                    }
                }
            });

            addView(indicator, indicatorSize, indicatorSize);

            LayoutParams lp = (LayoutParams) indicator.getLayoutParams();
            if (!isFirst) {
                lp.leftMargin = (int) indicatorGap / 2;
            }
            if (!isLast) {
                lp.rightMargin = (int) indicatorGap / 2;
            }
            indicator.setLayoutParams(lp);

            indicatorViews.add(indicator);
        }
    }

    private CircleIndicatorView getActiveIndicator() {
        CircleIndicatorView activeCircleIndicator = new CircleIndicatorView(getContext());
        activeCircleIndicator.setDiameter(activeIndicatorSize);
        activeCircleIndicator.setFillColor(activeIndicatorFillColor);
        activeCircleIndicator.setStrokeColor(activeIndicatorStrokeColor);
        activeCircleIndicator.setStrokeWidth(activeIndicatorStrokeWidth);

        return activeCircleIndicator;
    }

    private CircleIndicatorView getInactiveIndicator() {
        CircleIndicatorView inactiveCircleIndicator = new CircleIndicatorView(getContext());
        inactiveCircleIndicator.setDiameter(inactiveIndicatorSize);
        inactiveCircleIndicator.setFillColor(inactiveIndicatorFillColor);
        inactiveCircleIndicator.setStrokeColor(inactiveIndicatorStrokeColor);
        inactiveCircleIndicator.setStrokeWidth(inactiveIndicatorStrokeWidth);

        return inactiveCircleIndicator;
    }

    private void animateToActiveIndicator(int index) {
        final CircleIndicatorView indicatorView = indicatorViews.get(index);

        ValueAnimator sizeAnimator = ValueAnimator.ofObject(
                new FloatEvaluator(), indicatorView.getDiameter(), activeIndicatorSize);
        sizeAnimator.setDuration(ANIMATION_DURATION);
        sizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indicatorView.setDiameter((float) animation.getAnimatedValue());
            }
        });

        ValueAnimator fillColorAnimator = ValueAnimator.ofObject(
                new ArgbEvaluator(), indicatorView.getFillColor(), activeIndicatorFillColor);
        fillColorAnimator.setDuration(ANIMATION_DURATION);
        fillColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indicatorView.setFillColor((int) animation.getAnimatedValue());
            }
        });

        ValueAnimator strokeColorAnimator = ValueAnimator.ofObject(
                new ArgbEvaluator(), indicatorView.getStrokeColor(), activeIndicatorStrokeColor);
        strokeColorAnimator.setDuration(ANIMATION_DURATION);
        strokeColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indicatorView.setStrokeColor((int) animation.getAnimatedValue());
            }
        });

        ValueAnimator strokeWidthAnimator = ValueAnimator.ofObject(
                new FloatEvaluator(), indicatorView.getStrokeWidth(), activeIndicatorStrokeWidth);
        strokeWidthAnimator.setDuration(ANIMATION_DURATION);
        strokeWidthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indicatorView.setStrokeWidth((float) animation.getAnimatedValue());
            }
        });

        sizeAnimator.start();
        fillColorAnimator.start();
        strokeColorAnimator.start();
        strokeWidthAnimator.start();
    }

    private void animateToInactiveIndicator(int index) {
        final CircleIndicatorView indicatorView = indicatorViews.get(index);

        ValueAnimator sizeAnimator = ValueAnimator.ofObject(
                new FloatEvaluator(), indicatorView.getDiameter(), inactiveIndicatorSize);
        sizeAnimator.setDuration(ANIMATION_DURATION);
        sizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indicatorView.setDiameter((float) animation.getAnimatedValue());
            }
        });

        ValueAnimator fillColorAnimator = ValueAnimator.ofObject(
                new ArgbEvaluator(), indicatorView.getFillColor(), inactiveIndicatorFillColor);
        fillColorAnimator.setDuration(ANIMATION_DURATION);
        fillColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indicatorView.setFillColor((int) animation.getAnimatedValue());
            }
        });

        ValueAnimator strokeColorAnimator = ValueAnimator.ofObject(
                new ArgbEvaluator(), indicatorView.getStrokeColor(), inactiveIndicatorStrokeColor);
        strokeColorAnimator.setDuration(ANIMATION_DURATION);
        strokeColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indicatorView.setStrokeColor((int) animation.getAnimatedValue());
            }
        });

        ValueAnimator strokeWidthAnimator = ValueAnimator.ofObject(
                new FloatEvaluator(), indicatorView.getStrokeWidth(), inactiveIndicatorStrokeWidth);
        strokeWidthAnimator.setDuration(ANIMATION_DURATION);
        strokeWidthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indicatorView.setStrokeWidth((float) animation.getAnimatedValue());
            }
        });

        sizeAnimator.start();
        fillColorAnimator.start();
        strokeColorAnimator.start();
        strokeWidthAnimator.start();
    }

    /**
     * Get the current fill color of an active indicator
     *
     * @return color
     */
    @ColorInt
    public int getActiveIndicatorFillColor() {
        return activeIndicatorFillColor;
    }

    /**
     * Set the fill color of an active indicator
     *
     * @param activeIndicatorFillColor color
     */
    public void setActiveIndicatorFillColor(@ColorInt int activeIndicatorFillColor) {
        this.activeIndicatorFillColor = activeIndicatorFillColor;

        init();
        invalidate();
        requestLayout();
    }

    /**
     * Get the current stroke color of an active indicator
     *
     * @return color
     */
    @ColorInt
    public int getActiveIndicatorStrokeColor() {
        return activeIndicatorStrokeColor;
    }

    /**
     * Set the stroke color of an active indicator
     *
     * @param activeIndicatorStrokeColor color
     */
    public void setActiveIndicatorStrokeColor(@ColorInt int activeIndicatorStrokeColor) {
        this.activeIndicatorStrokeColor = activeIndicatorStrokeColor;

        init();
        invalidate();
        requestLayout();
    }

    /**
     * Get the current size of an active indicator
     *
     * @return size in pixel
     */
    public float getActiveIndicatorSize() {
        return activeIndicatorSize;
    }

    /**
     * Set the size of an active indicator
     *
     * @param activeIndicatorSize size in pixel
     */
    public void setActiveIndicatorSize(int activeIndicatorSize) {
        this.activeIndicatorSize = activeIndicatorSize;

        init();
        invalidate();
        requestLayout();
    }

    /**
     * Get the current fill color of an inactive indicator
     *
     * @return color
     */
    @ColorInt
    public int getInactiveIndicatorFillColor() {
        return inactiveIndicatorFillColor;
    }

    /**
     * Set the fill color of an inactive indicator
     *
     * @param inactiveIndicatorFillColor color
     */
    public void setInactiveIndicatorFillColor(@ColorInt int inactiveIndicatorFillColor) {
        this.inactiveIndicatorFillColor = inactiveIndicatorFillColor;

        init();
        invalidate();
        requestLayout();
    }

    /**
     * Get the current stroke color of an inactive indicator
     *
     * @return color
     */
    @ColorInt
    public int getInactiveIndicatorStrokeColor() {
        return inactiveIndicatorStrokeColor;
    }

    /**
     * Set the stroke color of an inactive indicator
     *
     * @param inactiveIndicatorStrokeColor color
     */
    public void setInactiveIndicatorStrokeColor(@ColorInt int inactiveIndicatorStrokeColor) {
        this.inactiveIndicatorStrokeColor = inactiveIndicatorStrokeColor;

        init();
        invalidate();
        requestLayout();
    }

    /**
     * Get the current size of an inactive indicator
     *
     * @return size in pixel
     */
    public float getInactiveIndicatorSize() {
        return inactiveIndicatorSize;
    }

    /**
     * Set the size of an inactive indicator
     *
     * @param inactiveIndicatorSize size in pixel
     */
    public void setInactiveIndicatorSize(float inactiveIndicatorSize) {
        this.inactiveIndicatorSize = inactiveIndicatorSize;

        init();
        invalidate();
        requestLayout();
    }

    /**
     * Get the current gap size between indicators
     *
     * @return gap size in pixel
     */
    public float getIndicatorGap() {
        return indicatorGap;
    }

    /**
     * Set the gap size between indicators
     *
     * @param indicatorGap gap size in pixel
     */
    public void setIndicatorGap(float indicatorGap) {
        this.indicatorGap = indicatorGap;

        init();
        invalidate();
        requestLayout();
    }

    /**
     * Get amount of pages
     *
     * @return amount of pages
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * Set amount of pages
     *
     * @param pageCount amount of pages
     */
    public void setPageCount(int pageCount) {
        if (pageCount <= 0) {
            Log.w(TAG, "Page count must be greater than 0!");
            return;
        }
        this.pageCount = pageCount;

        init();

        invalidate();
        requestLayout();
    }

    /**
     * Get currently active page
     *
     * @return index of currently active page
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Set current page (indicator will be updated accordingly)
     *
     * @param index    Index of current page
     * @param animated index change will be animated if set to true
     */
    public void setCurrentPage(int index, boolean animated) {
        if (index >= pageCount || index < 0) {
            Log.w(TAG, "Invalid index! PageCount: " + pageCount + ", requested index: " + index);
            return;
        }

        if (animated) {
            animateToInactiveIndicator(currentPage);
            animateToActiveIndicator(index);
        } else {
            CircleIndicatorView currentIndicatorView = indicatorViews.get(currentPage);
            currentIndicatorView.setDiameter(inactiveIndicatorSize);
            currentIndicatorView.setFillColor(inactiveIndicatorFillColor);
            currentIndicatorView.setStrokeColor(inactiveIndicatorStrokeColor);
            currentIndicatorView.setStrokeWidth(inactiveIndicatorStrokeWidth);

            CircleIndicatorView newIndicatorView = indicatorViews.get(index);
            newIndicatorView.setDiameter(activeIndicatorSize);
            newIndicatorView.setFillColor(activeIndicatorFillColor);
            newIndicatorView.setStrokeColor(activeIndicatorStrokeColor);
            newIndicatorView.setStrokeWidth(activeIndicatorStrokeWidth);
        }

        currentPage = index;

        invalidate();
        requestLayout();
    }

    /**
     * Set the new stroke width for an active indicator
     *
     * @param width width in pixel
     */
    public void setActiveIndicatorStrokeWidth(float width) {
        this.activeIndicatorStrokeWidth = width;

        for (int i = 0; i < pageCount; i++) {
            if (i == currentPage) {
                CircleIndicatorView circle = indicatorViews.get(i);
                circle.setStrokeWidth(activeIndicatorStrokeWidth);
                break;
            }
        }

        init();

        invalidate();
        requestLayout();
    }

    /**
     * Set the new stroke width for an inactive indicator
     *
     * @param width width in pixel
     */
    public void setInactiveIndicatorStrokeWidth(float width) {
        this.inactiveIndicatorStrokeWidth = width;

        for (int i = 0; i < pageCount; i++) {
            if (i != currentPage) {
                CircleIndicatorView circle = indicatorViews.get(i);
                circle.setStrokeWidth(inactiveIndicatorStrokeWidth);
            }
        }

        init();

        invalidate();
        requestLayout();
    }

    /**
     * Set a listener to OnClick events from indicators.
     *
     * @param listener OnIndicatorClickedListener
     */
    public void setOnIndicatorClickedListener(OnIndicatorClickedListener listener) {
        onIndicatorClickedListener = listener;
    }
}