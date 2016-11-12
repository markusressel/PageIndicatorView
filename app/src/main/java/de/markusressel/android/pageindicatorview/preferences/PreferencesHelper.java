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

package de.markusressel.android.pageindicatorview.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by Markus on 11.11.2016.
 */

public class PreferencesHelper {

    public static final String SHARED_PREFS_NAME = "settings";

    /**
     * Get a dimension from preferences
     *
     * @param context      application context
     * @param key          preference key
     * @param defaultValue default value
     * @return dimension float
     */
    public static float getDimen(@NonNull Context context, @StringRes int key, @DimenRes int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String dimenAsString = sharedPreferences.getString(context.getString(key), null);

        if (dimenAsString == null) {
            return context.getResources().getDimension(defaultValue);
        } else {
            return Float.valueOf(dimenAsString);
        }
    }

    /**
     * Get an integer from preferences
     *
     * @param context      application context
     * @param key          preference key
     * @param defaultValue default value
     * @return integer
     */
    public static int getInteger(@NonNull Context context, @StringRes int key, @IntegerRes int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String integerAsString = sharedPreferences.getString(context.getString(key), null);

        if (integerAsString == null) {
            return context.getResources().getInteger(defaultValue);
        } else {
            return Integer.valueOf(integerAsString);
        }
    }

    /**
     * Get a color from preferences
     *
     * @param context      application context
     * @param key          preference key
     * @param defaultValue default value
     * @return color as integer
     */
    public static int getColor(@NonNull Context context, @StringRes int key, @ColorInt int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(key), defaultValue);
    }

}
