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

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.EditTextPreference
import android.preference.PreferenceFragment
import android.support.annotation.ColorInt
import android.support.v4.content.LocalBroadcastManager

import com.rarepebble.colorpicker.ColorPreference

import de.markusressel.android.pageindicatorview.preferences.PreferencesHelper

/**
 * Simple preferences page
 *
 *
 * Created by Markus on 10.11.2016.
 */
class SettingsPage : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var activeIndicatorSize: EditTextPreference
    private lateinit var inactiveIndicatorSize: EditTextPreference
    private lateinit var indicatorGap: EditTextPreference
    private lateinit var activeIndicatorFillColor: ColorPreference
    private lateinit var activeIndicatorStrokeColor: ColorPreference
    private lateinit var inactiveIndicatorFillColor: ColorPreference
    private lateinit var inactiveIndicatorStrokeColor: ColorPreference
    private lateinit var initialPageIndex: EditTextPreference
    private lateinit var pageCount: EditTextPreference
    private lateinit var activeIndicatorStrokeWidth: EditTextPreference
    private lateinit var inactiveIndicatorStrokeWidth: EditTextPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set preferences file name
        preferenceManager.sharedPreferencesName = PreferencesHelper.SHARED_PREFS_NAME

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings)

        initPreferences()
    }

    private fun initPreferences() {
        activeIndicatorFillColor = findPreference(getString(R.string.key_activeIndicatorFillColor)) as ColorPreference
        activeIndicatorFillColor.summary = colorToHex(activeIndicatorFillColor.color)

        activeIndicatorStrokeColor = findPreference(getString(R.string.key_activeIndicatorStrokeColor)) as ColorPreference
        activeIndicatorStrokeColor.summary = colorToHex(activeIndicatorStrokeColor.color)

        activeIndicatorSize = findPreference(getString(R.string.key_activeIndicatorFillSize)) as EditTextPreference
        activeIndicatorSize.summary = getString(R.string.summary_activeIndicatorSize, activeIndicatorSize.text)

        activeIndicatorStrokeWidth = findPreference(getString(R.string.key_activeIndicatorStrokeWidth)) as EditTextPreference
        activeIndicatorStrokeWidth.summary = getString(R.string.summary_activeIndicatorStrokeWidth, activeIndicatorStrokeWidth.text)


        inactiveIndicatorFillColor = findPreference(getString(R.string.key_inactiveIndicatorFillColor)) as ColorPreference
        inactiveIndicatorFillColor.summary = colorToHex(inactiveIndicatorFillColor.color)

        inactiveIndicatorStrokeColor = findPreference(getString(R.string.key_inactiveIndicatorStrokeColor)) as ColorPreference
        inactiveIndicatorStrokeColor.summary = colorToHex(inactiveIndicatorStrokeColor.color)

        inactiveIndicatorSize = findPreference(getString(R.string.key_inactiveIndicatorFillSize)) as EditTextPreference
        inactiveIndicatorSize.summary = getString(R.string.summary_inactiveIndicatorSize, inactiveIndicatorSize.text)

        inactiveIndicatorStrokeWidth = findPreference(getString(R.string.key_inactiveIndicatorStrokeWidth)) as EditTextPreference
        inactiveIndicatorStrokeWidth.summary = getString(R.string.summary_inactiveIndicatorStrokeWidth, inactiveIndicatorStrokeWidth.text)


        indicatorGap = findPreference(getString(R.string.key_indicatorGap)) as EditTextPreference
        indicatorGap.summary = getString(R.string.summary_indicatorGap, indicatorGap.text)

        initialPageIndex = findPreference(getString(R.string.key_initialPageIndex)) as EditTextPreference
        initialPageIndex.summary = initialPageIndex.text

        pageCount = findPreference(getString(R.string.key_pageCount)) as EditTextPreference
        pageCount.summary = pageCount.text
    }

    /**
     * Converts a color int to an #AARRGGBB hex string representation
     */
    private fun colorToHex(@ColorInt color: Int): String {
        return String.format("#%08X", color)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (getString(R.string.key_activeIndicatorFillColor) == key) {
            activeIndicatorFillColor.summary = colorToHex(activeIndicatorFillColor.color)
        } else if (getString(R.string.key_activeIndicatorStrokeColor) == key) {
            activeIndicatorStrokeColor.summary = colorToHex(activeIndicatorStrokeColor.color)
        } else if (getString(R.string.key_activeIndicatorFillSize) == key) {
            activeIndicatorSize.summary = getString(R.string.summary_activeIndicatorSize, activeIndicatorSize.text)
        } else if (getString(R.string.key_activeIndicatorStrokeWidth) == key) {
            activeIndicatorStrokeWidth.summary = getString(R.string.summary_activeIndicatorStrokeWidth, activeIndicatorStrokeWidth.text)
        } else if (getString(R.string.key_inactiveIndicatorFillColor) == key) {
            inactiveIndicatorFillColor.summary = colorToHex(inactiveIndicatorFillColor.color)
        } else if (getString(R.string.key_inactiveIndicatorStrokeColor) == key) {
            inactiveIndicatorStrokeColor.summary = colorToHex(inactiveIndicatorStrokeColor.color)
        } else if (getString(R.string.key_inactiveIndicatorFillSize) == key) {
            inactiveIndicatorSize.summary = getString(R.string.summary_inactiveIndicatorSize, inactiveIndicatorSize.text)
        } else if (getString(R.string.key_inactiveIndicatorStrokeWidth) == key) {
            inactiveIndicatorStrokeWidth.summary = getString(R.string.summary_inactiveIndicatorStrokeWidth, inactiveIndicatorStrokeWidth.text)
        } else if (getString(R.string.key_indicatorGap) == key) {
            indicatorGap.summary = getString(R.string.summary_indicatorGap, indicatorGap.text)
        } else if (getString(R.string.key_initialPageIndex) == key) {
            initialPageIndex.summary = initialPageIndex.text
        } else if (getString(R.string.key_pageCount) == key) {
            pageCount.summary = pageCount.text
        }

        notifyPreferenceChanged(activity, key)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    companion object {

        const val INTENT_ACTION_PREFERENCE_CHANGED = "preference_changed"
        const val KEY_PREFERENCE_KEY = "preferenceKey"

        fun newInstance(): SettingsPage {
            return SettingsPage()
        }

        /**
         * Notifies the MainActivity that settings have changed
         *
         * @param context application context
         * @param key     prefrence key that has changed
         */
        private fun notifyPreferenceChanged(context: Context, key: String) {
            val intent = Intent(INTENT_ACTION_PREFERENCE_CHANGED)
            intent.putExtra(KEY_PREFERENCE_KEY, key)

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }
}