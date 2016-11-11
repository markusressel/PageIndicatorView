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
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import de.markusressel.android.library.pageindicatorview.PageIndicatorView;
import de.markusressel.android.pageindicatorview.preferences.PreferencesHelper;

public class MainActivity extends AppCompatActivity {

    private ViewPager tabViewPager;
    private CustomTabAdapter customTabAdapter;
    private PageIndicatorView pageIndicatorView;

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

        @ColorInt int activeIndicatorColorFill = PreferencesHelper.getColor(this, R.string.key_activeIndicatorColorFill, getResources().getColor(R.color.default_value_activeIndicatorColorFill));
        @ColorInt int activeIndicatorColorStroke = PreferencesHelper.getColor(this, R.string.key_activeIndicatorColorStroke, getResources().getColor(R.color.default_value_activeIndicatorColorStroke));
        int activeIndicatorSize = PreferencesHelper.getInteger(this, R.string.key_activeIndicatorSize, R.integer.default_value_activeIndicatorSize);

        @ColorInt int inactiveIndicatorColorFill = PreferencesHelper.getColor(this, R.string.key_inactiveIndicatorColorFill, getResources().getColor(R.color.default_value_inactiveIndicatorColorFill));
        @ColorInt int inactiveIndicatorColorStroke = PreferencesHelper.getColor(this, R.string.key_inactiveIndicatorColorFill, getResources().getColor(R.color.default_value_inactiveIndicatorColorStroke));
        int inactiveIndicatorSize = PreferencesHelper.getInteger(this, R.string.key_inactiveIndicatorSize, R.integer.default_value_inactiveIndicatorSize);

        int indicatorGap = PreferencesHelper.getInteger(this, R.string.key_indicatorGap, R.integer.default_value_indicatorGap);
        int initialPageIndex = PreferencesHelper.getInteger(this, R.string.key_initialPageIndex, R.integer.default_value_initialPageIndex);
        int pageCount = PreferencesHelper.getInteger(this, R.string.key_pageCount, R.integer.default_value_pageCount);

        pageIndicatorView.setPageCount(customTabAdapter.getCount());
        pageIndicatorView.setCurrentPage(initialPageIndex, false);
        // TODO: set other values from settings (create setters first)
    }

    private static class CustomTabAdapter extends FragmentPagerAdapter {
        private Context context;

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
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position + 1);
        }
    }
}
