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

import android.os.Bundle;
import android.preference.PreferenceFragment;

import de.markusressel.android.pageindicatorview.preferences.PreferencesHelper;

/**
 * Created by Markus on 10.11.2016.
 */

public class SettingsPage extends PreferenceFragment {

    public static SettingsPage newInstance() {
        Bundle args = new Bundle();
        SettingsPage fragment = new SettingsPage();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set preferences file name
        getPreferenceManager().setSharedPreferencesName(PreferencesHelper.SHARED_PREFS_NAME);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

}