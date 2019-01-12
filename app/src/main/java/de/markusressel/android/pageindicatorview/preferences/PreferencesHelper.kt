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

package de.markusressel.android.pageindicatorview.preferences

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.DimenRes
import android.support.annotation.IntegerRes
import android.support.annotation.StringRes

/**
 * Simply Preferences helper for easy access to colors, integers and dimensions stored in SharedPreferences
 *
 *
 * Created by Markus on 11.11.2016.
 */
object PreferencesHelper {

    const val SHARED_PREFS_NAME = "settings"

    private fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    /**
     * Get a dimension from preferences
     * This returns the dp value, not the actual pixel value!
     *
     * @param context      application context
     * @param key          preference key
     * @param defaultValue default value
     * @return dimension float
     */
    fun getDimen(context: Context, @StringRes key: Int, @DimenRes defaultValue: Int): Float {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val dimenAsString = sharedPreferences.getString(context.getString(key), null)

        return if (dimenAsString == null) {
            dpFromPx(context, context.resources.getDimension(defaultValue))
        } else {
            java.lang.Float.valueOf(dimenAsString)
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
    fun getInteger(context: Context, @StringRes key: Int, @IntegerRes defaultValue: Int): Int {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val integerAsString = sharedPreferences.getString(context.getString(key), null)

        return if (integerAsString == null) {
            context.resources.getInteger(defaultValue)
        } else {
            Integer.valueOf(integerAsString)
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
    fun getColor(context: Context, @StringRes key: Int, @ColorInt defaultValue: Int): Int {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(context.getString(key), defaultValue)
    }

}
