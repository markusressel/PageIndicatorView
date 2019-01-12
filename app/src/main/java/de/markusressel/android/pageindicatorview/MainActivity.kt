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

package de.markusressel.android.pageindicatorview

import android.app.Fragment
import android.app.FragmentManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v13.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

import de.markusressel.android.library.pageindicatorview.OnIndicatorClickedListener
import de.markusressel.android.library.pageindicatorview.PageIndicatorView
import de.markusressel.android.pageindicatorview.preferences.PreferencesHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**
     * Create the adapter that will return a fragment
     * for each of the two primary sections of the app.
     */
    private val customTabAdapter: CustomTabAdapter by lazy { CustomTabAdapter(fragmentManager) }
    private var broadcastReceiver: BroadcastReceiver? = null

    private fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        pageIndicator.setOnIndicatorClickedListener(object : OnIndicatorClickedListener {
            override fun onIndicatorClicked(pageIndicatorView: PageIndicatorView, index: Int) {
                pageIndicatorView.setCurrentPage(index, true)
                viewPager.currentItem = index
            }
        })

        // Set up the tabViewPager, attaching the adapter and setting up a listener
        // for when the user swipes between sections.
        viewPager.adapter = customTabAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                pageIndicator.setCurrentPage(position, true)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        initFromPreferenceValues()

        // this receiver will update the view if a preference has changed
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (SettingsPage.INTENT_ACTION_PREFERENCE_CHANGED == intent.action) {
                    val key = intent.getStringExtra(SettingsPage.KEY_PREFERENCE_KEY)

                    when (key) {
                        getString(R.string.key_activeIndicatorFillColor) -> {
                            @ColorInt val activeIndicatorColorFill = PreferencesHelper.getColor(applicationContext, R.string.key_activeIndicatorFillColor, ContextCompat.getColor(applicationContext, R.color.default_value_activeIndicatorFillColor))
                            pageIndicator.activeIndicatorFillColor = activeIndicatorColorFill
                        }
                        getString(R.string.key_activeIndicatorStrokeColor) -> {
                            @ColorInt val activeIndicatorColorStroke = PreferencesHelper.getColor(applicationContext, R.string.key_activeIndicatorStrokeColor, ContextCompat.getColor(applicationContext, R.color.default_value_activeIndicatorStrokeColor))
                            pageIndicator.activeIndicatorStrokeColor = activeIndicatorColorStroke
                        }
                        getString(R.string.key_activeIndicatorStrokeWidth) -> {
                            val activeIndicatorStrokeWidth = PreferencesHelper.getDimen(applicationContext, R.string.key_activeIndicatorStrokeWidth, R.dimen.default_value_activeIndicatorStrokeWidth)
                            pageIndicator.activeIndicatorStrokeWidth = pxFromDp(applicationContext, activeIndicatorStrokeWidth)
                        }
                        getString(R.string.key_activeIndicatorFillSize) -> {
                            val activeIndicatorSize = PreferencesHelper.getDimen(applicationContext, R.string.key_activeIndicatorFillSize, R.dimen.default_value_activeIndicatorFillSize)
                            pageIndicator.setActiveIndicatorSize(Math.round(pxFromDp(applicationContext, activeIndicatorSize)))
                        }
                        getString(R.string.key_inactiveIndicatorFillColor) -> {
                            @ColorInt val inactiveIndicatorColorFill = PreferencesHelper.getColor(applicationContext, R.string.key_inactiveIndicatorFillColor, ContextCompat.getColor(applicationContext, R.color.default_value_inactiveIndicatorFillColor))
                            pageIndicator.inactiveIndicatorFillColor = inactiveIndicatorColorFill
                        }
                        getString(R.string.key_inactiveIndicatorStrokeColor) -> {
                            @ColorInt val inactiveIndicatorColorStroke = PreferencesHelper.getColor(applicationContext, R.string.key_inactiveIndicatorStrokeColor, ContextCompat.getColor(applicationContext, R.color.default_value_inactiveIndicatorStrokeColor))
                            pageIndicator.inactiveIndicatorStrokeColor = inactiveIndicatorColorStroke
                        }
                        getString(R.string.key_inactiveIndicatorStrokeWidth) -> {
                            val inactiveIndicatorStrokeWidth = PreferencesHelper.getDimen(applicationContext, R.string.key_inactiveIndicatorStrokeWidth, R.dimen.default_value_inactiveIndicatorStrokeWidth)
                            pageIndicator.inactiveIndicatorStrokeWidth = pxFromDp(applicationContext, inactiveIndicatorStrokeWidth)
                        }
                        getString(R.string.key_inactiveIndicatorFillSize) -> {
                            val inactiveIndicatorSize = PreferencesHelper.getDimen(applicationContext, R.string.key_inactiveIndicatorFillSize, R.dimen.default_value_inactiveIndicatorFillSize)
                            pageIndicator.inactiveIndicatorSize = pxFromDp(applicationContext, inactiveIndicatorSize)

                        }
                        getString(R.string.key_indicatorGap) -> {
                            val indicatorGap = PreferencesHelper.getDimen(applicationContext, R.string.key_indicatorGap, R.dimen.default_value_indicatorGap)
                            pageIndicator.indicatorGap = pxFromDp(applicationContext, indicatorGap)
                        }
                        getString(R.string.key_initialPageIndex) -> {
                            // TODO
                            val initialPageIndex = PreferencesHelper.getInteger(applicationContext, R.string.key_initialPageIndex, R.integer.default_value_initialPageIndex)
                        }
                        getString(R.string.key_pageCount) -> {
                            val pageCount = PreferencesHelper.getInteger(applicationContext, R.string.key_pageCount, R.integer.default_value_pageCount)
                            customTabAdapter.count = pageCount
                            pageIndicator.pageCount = pageCount
                        }
                    }
                }
            }
        }
    }

    private fun initFromPreferenceValues() {
        @ColorInt val activeIndicatorColorFill = PreferencesHelper.getColor(applicationContext, R.string.key_activeIndicatorFillColor, ContextCompat.getColor(applicationContext, R.color.default_value_activeIndicatorFillColor))
        @ColorInt val activeIndicatorColorStroke = PreferencesHelper.getColor(applicationContext, R.string.key_activeIndicatorStrokeColor, ContextCompat.getColor(applicationContext, R.color.default_value_activeIndicatorStrokeColor))
        val activeIndicatorSize = PreferencesHelper.getDimen(applicationContext, R.string.key_activeIndicatorFillSize, R.dimen.default_value_activeIndicatorFillSize)
        val activeIndicatorStrokeWidth = PreferencesHelper.getDimen(applicationContext, R.string.key_activeIndicatorStrokeWidth, R.dimen.default_value_activeIndicatorStrokeWidth)

        @ColorInt val inactiveIndicatorColorFill = PreferencesHelper.getColor(applicationContext, R.string.key_inactiveIndicatorFillColor, ContextCompat.getColor(applicationContext, R.color.default_value_inactiveIndicatorFillColor))
        @ColorInt val inactiveIndicatorColorStroke = PreferencesHelper.getColor(applicationContext, R.string.key_inactiveIndicatorStrokeColor, ContextCompat.getColor(applicationContext, R.color.default_value_inactiveIndicatorStrokeColor))
        val inactiveIndicatorSize = PreferencesHelper.getDimen(applicationContext, R.string.key_inactiveIndicatorFillSize, R.dimen.default_value_inactiveIndicatorFillSize)
        val inactiveIndicatorStrokeWidth = PreferencesHelper.getDimen(applicationContext, R.string.key_inactiveIndicatorStrokeWidth, R.dimen.default_value_inactiveIndicatorStrokeWidth)

        val indicatorGap = PreferencesHelper.getDimen(applicationContext, R.string.key_indicatorGap, R.dimen.default_value_indicatorGap)
        val initialPageIndex = PreferencesHelper.getInteger(applicationContext, R.string.key_initialPageIndex, R.integer.default_value_initialPageIndex)
        val pageCount = PreferencesHelper.getInteger(applicationContext, R.string.key_pageCount, R.integer.default_value_pageCount)

        pageIndicator.setActiveIndicatorSize(Math.round(pxFromDp(this, activeIndicatorSize)))
        pageIndicator.activeIndicatorFillColor = activeIndicatorColorFill
        pageIndicator.activeIndicatorStrokeColor = activeIndicatorColorStroke
        pageIndicator.activeIndicatorStrokeWidth = pxFromDp(this, activeIndicatorStrokeWidth)

        pageIndicator.inactiveIndicatorSize = pxFromDp(this, inactiveIndicatorSize)
        pageIndicator.inactiveIndicatorFillColor = inactiveIndicatorColorFill
        pageIndicator.inactiveIndicatorStrokeColor = inactiveIndicatorColorStroke
        pageIndicator.inactiveIndicatorStrokeWidth = pxFromDp(this, inactiveIndicatorStrokeWidth)

        pageIndicator.indicatorGap = pxFromDp(this, indicatorGap)

        pageIndicator.pageCount = pageCount
        pageIndicator.setCurrentPage(initialPageIndex, false)
    }

    public override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction(SettingsPage.INTENT_ACTION_PREFERENCE_CHANGED)
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(broadcastReceiver!!, intentFilter)
    }

    public override fun onStop() {
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(broadcastReceiver!!)
        super.onStop()
    }

    private class CustomTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private var count = 5

        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                SettingsPage.newInstance()
            } else if (position == 1) {
                DemonstrationPage.newInstance()
            } else {
                SamplePage.newInstance("Page: " + (position + 1))
            }
        }

        /**
         * @return the number of pages to display
         */
        override fun getCount(): Int {
            return count
        }

        /**
         * Set the amount of pages
         */
        fun setCount(count: Int) {
            this.count = count
            notifyDataSetChanged()
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return (position + 1).toString()
        }
    }
}
