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

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
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
    private static final int DEFAULT_INDICATOR_GAP = 5;
    private static final int DEFAULT_ACTIVE_INDICATOR_SIZE = 7;
    private static final int DEFAULT_INACTIVE_INDICATOR_SIZE = 5;
    private static final int DEFAULT_PAGE_COUNT = 1;
    private static final int DEFAULT_CURRENT_PAGE_INDEX = 0;
    private static final int DEFAULT_ACTIVE_INDICATOR_FILL_COLOR = Color.WHITE;
    private static final int DEFAULT_INACTIVE_INDICATOR_FILL_COLOR = Color.WHITE;
    private static final int DEFAULT_ACTIVE_INDICATOR_STROKE_COLOR = Color.WHITE;
    private static final int DEFAULT_INACTIVE_INDICATOR_STROKE_COLOR = Color.WHITE;

    private int currentPage;
    private int pageCount;
    private int activeIndicatorSize;
    private int inactiveIndicatorSize;
    private int indicatorGap;
    private int activeIndicatorFillColor;
    private int activeIndicatorStrokeColor;
    private int inactiveIndicatorFillColor;
    private int inactiveIndicatorStrokeColor;

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
            activeIndicatorSize = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_activeIndicatorSize, DEFAULT_ACTIVE_INDICATOR_SIZE);
            inactiveIndicatorSize = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_inactiveIndicatorSize, DEFAULT_INACTIVE_INDICATOR_SIZE);
            indicatorGap = a.getDimensionPixelSize(R.styleable.PageIndicatorView_piv_indicatorGap, DEFAULT_INDICATOR_GAP);
            activeIndicatorFillColor = a.getColor(R.styleable.PageIndicatorView_piv_activeIndicatorColorFill, DEFAULT_ACTIVE_INDICATOR_FILL_COLOR);
            activeIndicatorStrokeColor = a.getColor(R.styleable.PageIndicatorView_piv_activeIndicatorColorStroke, DEFAULT_ACTIVE_INDICATOR_STROKE_COLOR);
            inactiveIndicatorFillColor = a.getColor(R.styleable.PageIndicatorView_piv_inactiveIndicatorColorFill, DEFAULT_INACTIVE_INDICATOR_FILL_COLOR);
            inactiveIndicatorStrokeColor = a.getColor(R.styleable.PageIndicatorView_piv_inactiveIndicatorColorStroke, DEFAULT_INACTIVE_INDICATOR_STROKE_COLOR);
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

        for (int i = 0; i < pageCount; i++) {
            if (i == currentPage) {
                addActiveIndicator();
            } else {
                addInactiveIndicator();
            }
        }
    }

    private void addActiveIndicator() {
        CircleIndicatorView activeCircleIndicator = new CircleIndicatorView(getContext());
        activeCircleIndicator.setDiameter(activeIndicatorSize);
        activeCircleIndicator.setFillColor(activeIndicatorFillColor);
        activeCircleIndicator.setStrokeColor(activeIndicatorStrokeColor);

        addView(activeCircleIndicator, activeIndicatorSize + 1, activeIndicatorSize + 1);

        LayoutParams lp = (LayoutParams) activeCircleIndicator.getLayoutParams();
        lp.leftMargin = indicatorGap / 2;
        lp.rightMargin = indicatorGap / 2;
        activeCircleIndicator.setLayoutParams(lp);

        indicatorViews.add(activeCircleIndicator);
    }

    private void addInactiveIndicator() {
        CircleIndicatorView inactiveCircleIndicator = new CircleIndicatorView(getContext());
        inactiveCircleIndicator.setDiameter(inactiveIndicatorSize);
        inactiveCircleIndicator.setFillColor(inactiveIndicatorFillColor);
        inactiveCircleIndicator.setStrokeColor(inactiveIndicatorStrokeColor);

        addView(inactiveCircleIndicator, activeIndicatorSize + 1, activeIndicatorSize + 1);

        LayoutParams lp = (LayoutParams) inactiveCircleIndicator.getLayoutParams();
        lp.leftMargin = indicatorGap / 2;
        lp.rightMargin = indicatorGap / 2;
        inactiveCircleIndicator.setLayoutParams(lp);

        indicatorViews.add(inactiveCircleIndicator);
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

        sizeAnimator.start();
        fillColorAnimator.start();
        strokeColorAnimator.start();
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

        sizeAnimator.start();
        fillColorAnimator.start();
        strokeColorAnimator.start();
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
        }

        currentPage = index;

        invalidate();
        requestLayout();
    }

}
