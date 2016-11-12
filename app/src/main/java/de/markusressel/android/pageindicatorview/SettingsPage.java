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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.ColorInt;
import android.support.v4.content.LocalBroadcastManager;

import com.rarepebble.colorpicker.ColorPreference;

import de.markusressel.android.pageindicatorview.preferences.PreferencesHelper;

/**
 * Simple preferences page
 * <p>
 * Created by Markus on 10.11.2016.
 */
public class SettingsPage extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String INTENT_ACTION_PREFRENCE_CHANGED = "preference_changed";
    public static final String KEY_PREFERENCE_KEY = "preferenceKey";
    private EditTextPreference activeIndicatorSize;
    private EditTextPreference inactiveIndicatorSize;
    private EditTextPreference indicatorGap;
    private ColorPreference activeIndicatorFillColor;
    private ColorPreference activeIndicatorStrokeColor;
    private ColorPreference inactiveIndicatorFillColor;
    private ColorPreference inactiveIndicatorStrokeColor;
    private EditTextPreference initialPageIndex;
    private EditTextPreference pageCount;

    public static SettingsPage newInstance() {
        Bundle args = new Bundle();
        SettingsPage fragment = new SettingsPage();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Notifies the MainActivity that settings have changed
     *
     * @param context application context
     * @param key     prefrence key that has changed
     */
    private static void notifyPreferenceChanged(Context context, String key) {
        Intent intent = new Intent(INTENT_ACTION_PREFRENCE_CHANGED);
        intent.putExtra(KEY_PREFERENCE_KEY, key);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set preferences file name
        getPreferenceManager().setSharedPreferencesName(PreferencesHelper.SHARED_PREFS_NAME);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);

        initPreferences();
    }

    private void initPreferences() {
        activeIndicatorFillColor = (ColorPreference) findPreference(getString(R.string.key_activeIndicatorColorFill));
        activeIndicatorFillColor.setSummary(colorToHex(activeIndicatorFillColor.getColor()));

        activeIndicatorStrokeColor = (ColorPreference) findPreference(getString(R.string.key_activeIndicatorColorStroke));
        activeIndicatorStrokeColor.setSummary(colorToHex(activeIndicatorStrokeColor.getColor()));

        activeIndicatorSize = (EditTextPreference) findPreference(getString(R.string.key_activeIndicatorSize));
        activeIndicatorSize.setSummary(getString(R.string.summary_activeIndicatorSize, activeIndicatorSize.getText()));


        inactiveIndicatorFillColor = (ColorPreference) findPreference(getString(R.string.key_inactiveIndicatorColorFill));
        inactiveIndicatorFillColor.setSummary(colorToHex(inactiveIndicatorFillColor.getColor()));

        inactiveIndicatorStrokeColor = (ColorPreference) findPreference(getString(R.string.key_inactiveIndicatorColorStroke));
        inactiveIndicatorStrokeColor.setSummary(colorToHex(inactiveIndicatorStrokeColor.getColor()));

        inactiveIndicatorSize = (EditTextPreference) findPreference(getString(R.string.key_inactiveIndicatorSize));
        inactiveIndicatorSize.setSummary(getString(R.string.summary_inactiveIndicatorSize, inactiveIndicatorSize.getText()));


        indicatorGap = (EditTextPreference) findPreference(getString(R.string.key_indicatorGap));
        indicatorGap.setSummary(getString(R.string.summary_indicatorGap, indicatorGap.getText()));

        initialPageIndex = (EditTextPreference) findPreference(getString(R.string.key_initialPageIndex));
        initialPageIndex.setSummary(initialPageIndex.getText());

        pageCount = (EditTextPreference) findPreference(getString(R.string.key_pageCount));
        pageCount.setSummary(pageCount.getText());
    }

    /**
     * Converts a color int to an #AARRGGBB hex string representation
     */
    private String colorToHex(@ColorInt int color) {
        return String.format("#%08X", (color));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getString(R.string.key_activeIndicatorColorFill).equals(key)) {
            activeIndicatorFillColor.setSummary(colorToHex(activeIndicatorFillColor.getColor()));
        } else if (getString(R.string.key_activeIndicatorColorStroke).equals(key)) {
            activeIndicatorStrokeColor.setSummary(colorToHex(activeIndicatorStrokeColor.getColor()));
        } else if (getString(R.string.key_activeIndicatorSize).equals(key)) {
            activeIndicatorSize.setSummary(getString(R.string.summary_activeIndicatorSize, activeIndicatorSize.getText()));
        } else if (getString(R.string.key_inactiveIndicatorColorFill).equals(key)) {
            inactiveIndicatorFillColor.setSummary(colorToHex(inactiveIndicatorFillColor.getColor()));
        } else if (getString(R.string.key_inactiveIndicatorColorStroke).equals(key)) {
            inactiveIndicatorStrokeColor.setSummary(colorToHex(inactiveIndicatorStrokeColor.getColor()));
        } else if (getString(R.string.key_inactiveIndicatorSize).equals(key)) {
            inactiveIndicatorSize.setSummary(getString(R.string.summary_inactiveIndicatorSize, inactiveIndicatorSize.getText()));
        } else if (getString(R.string.key_indicatorGap).equals(key)) {
            indicatorGap.setSummary(getString(R.string.summary_indicatorGap, indicatorGap.getText()));
        } else if (getString(R.string.key_initialPageIndex).equals(key)) {
            initialPageIndex.setSummary(initialPageIndex.getText());
        } else if (getString(R.string.key_pageCount).equals(key)) {
            pageCount.setSummary(pageCount.getText());
        }

        notifyPreferenceChanged(getActivity(), key);
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
    }
}