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

package de.markusressel.android.pageindicatorview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import de.markusressel.android.library.pageindicatorview.PageIndicatorView;
import de.markusressel.android.pageindicatorview.preferences.PreferencesHelper;

public class MainActivity extends AppCompatActivity {

    private ViewPager tabViewPager;
    private CustomTabAdapter customTabAdapter;
    private PageIndicatorView pageIndicatorView;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicator);

        // Create the adapter that will return a fragment
        // for each of the two primary sections of the app.
        customTabAdapter = new CustomTabAdapter(getFragmentManager(), this);

        // Set up the tabViewPager, attaching the adapter and setting up a listener
        // for when the user swipes between sections.
        tabViewPager = (ViewPager) findViewById(R.id.viewPager);
        tabViewPager.setAdapter(customTabAdapter);

        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setCurrentPage(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        @ColorInt int activeIndicatorColorFill = PreferencesHelper.getColor(getApplicationContext(), R.string.key_activeIndicatorColorFill, getResources().getColor(R.color.default_value_activeIndicatorColorFill));
        @ColorInt int activeIndicatorColorStroke = PreferencesHelper.getColor(getApplicationContext(), R.string.key_activeIndicatorColorStroke, getResources().getColor(R.color.default_value_activeIndicatorColorStroke));
        int activeIndicatorSize = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_activeIndicatorSize, R.integer.default_value_activeIndicatorSize);

        @ColorInt int inactiveIndicatorColorFill = PreferencesHelper.getColor(getApplicationContext(), R.string.key_inactiveIndicatorColorFill, getResources().getColor(R.color.default_value_inactiveIndicatorColorFill));
        @ColorInt int inactiveIndicatorColorStroke = PreferencesHelper.getColor(getApplicationContext(), R.string.key_inactiveIndicatorColorFill, getResources().getColor(R.color.default_value_inactiveIndicatorColorStroke));
        int inactiveIndicatorSize = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_inactiveIndicatorSize, R.integer.default_value_inactiveIndicatorSize);

        int indicatorGap = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_indicatorGap, R.integer.default_value_indicatorGap);
        int initialPageIndex = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_initialPageIndex, R.integer.default_value_initialPageIndex);
        int pageCount = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_pageCount, R.integer.default_value_pageCount);

        pageIndicatorView.setActiveIndicatorFillColor(activeIndicatorColorFill);
        pageIndicatorView.setActiveIndicatorStrokeColor(activeIndicatorColorStroke);
        pageIndicatorView.setActiveIndicatorSize(activeIndicatorSize);

        pageIndicatorView.setInactiveIndicatorFillColor(inactiveIndicatorColorFill);
        pageIndicatorView.setInactiveIndicatorStrokeColor(inactiveIndicatorColorStroke);
        pageIndicatorView.setInactiveIndicatorSize(inactiveIndicatorSize);

        pageIndicatorView.setIndicatorGap(indicatorGap);

        pageIndicatorView.setPageCount(pageCount);
        pageIndicatorView.setCurrentPage(initialPageIndex, false);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (SettingsPage.INTENT_ACTION_PREFRENCE_CHANGED.equals(intent.getAction())) {
                    String key = intent.getStringExtra(SettingsPage.KEY_PREFERENCE_KEY);

                    if (getString(R.string.key_activeIndicatorColorFill).equals(key)) {
                        @ColorInt int activeIndicatorColorFill = PreferencesHelper.getColor(getApplicationContext(), R.string.key_activeIndicatorColorFill, getResources().getColor(R.color.default_value_activeIndicatorColorFill));
                        pageIndicatorView.setActiveIndicatorFillColor(activeIndicatorColorFill);
                    } else if (getString(R.string.key_activeIndicatorColorStroke).equals(key)) {
                        @ColorInt int activeIndicatorColorStroke = PreferencesHelper.getColor(getApplicationContext(), R.string.key_activeIndicatorColorStroke, getResources().getColor(R.color.default_value_activeIndicatorColorStroke));
                        pageIndicatorView.setActiveIndicatorStrokeColor(activeIndicatorColorStroke);
                    } else if (getString(R.string.key_activeIndicatorSize).equals(key)) {
                        int activeIndicatorSize = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_activeIndicatorSize, R.integer.default_value_activeIndicatorSize);
                        pageIndicatorView.setActiveIndicatorSize(activeIndicatorSize);
                    } else if (getString(R.string.key_inactiveIndicatorColorFill).equals(key)) {
                        @ColorInt int inactiveIndicatorColorFill = PreferencesHelper.getColor(getApplicationContext(), R.string.key_inactiveIndicatorColorFill, getResources().getColor(R.color.default_value_inactiveIndicatorColorFill));
                        pageIndicatorView.setInactiveIndicatorFillColor(inactiveIndicatorColorFill);
                    } else if (getString(R.string.key_inactiveIndicatorColorStroke).equals(key)) {
                        @ColorInt int inactiveIndicatorColorStroke = PreferencesHelper.getColor(getApplicationContext(), R.string.key_inactiveIndicatorColorFill, getResources().getColor(R.color.default_value_inactiveIndicatorColorStroke));
                        pageIndicatorView.setInactiveIndicatorStrokeColor(inactiveIndicatorColorStroke);
                    } else if (getString(R.string.key_inactiveIndicatorSize).equals(key)) {
                        int inactiveIndicatorSize = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_inactiveIndicatorSize, R.integer.default_value_inactiveIndicatorSize);
                        pageIndicatorView.setInactiveIndicatorSize(inactiveIndicatorSize);
                    } else if (getString(R.string.key_indicatorGap).equals(key)) {
                        int indicatorGap = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_indicatorGap, R.integer.default_value_indicatorGap);
                        pageIndicatorView.setIndicatorGap(indicatorGap);
                    } else if (getString(R.string.key_initialPageIndex).equals(key)) {
                        int initialPageIndex = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_initialPageIndex, R.integer.default_value_initialPageIndex);
                        // TODO
                    } else if (getString(R.string.key_pageCount).equals(key)) {
                        int pageCount = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_pageCount, R.integer.default_value_pageCount);
                        customTabAdapter.setCount(pageCount);
                        pageIndicatorView.setPageCount(pageCount);
                    }
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SettingsPage.INTENT_ACTION_PREFRENCE_CHANGED);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    private static class CustomTabAdapter extends FragmentPagerAdapter {
        private Context context;
        private int count = 5;

        public CustomTabAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return SettingsPage.newInstance();
            } else {
                return SamplePage.newInstance("Page: " + (position + 1));
            }
        }

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return count;
        }

        /**
         * Set the amount of pages
         */
        public void setCount(int count) {
            this.count = count;

            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position + 1);
        }
    }
}
