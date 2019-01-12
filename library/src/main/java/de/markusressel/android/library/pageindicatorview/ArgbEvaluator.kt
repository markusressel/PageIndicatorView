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

package de.markusressel.android.library.pageindicatorview

import android.animation.TypeEvaluator
import android.os.Build

/**
 * This evaluator can be used to perform type interpolation between integer
 * values that represent ARGB colors.
 *
 *
 * **Official 4.2 source code. Solution for https://code.google.com/p/android/issues/detail?id=36158.**
 */
internal class ArgbEvaluator : TypeEvaluator<Any> {

    private var mDelegate: android.animation.ArgbEvaluator? = null

    /**
     * This function returns the calculated in-between value for a color
     * given integers that represent the start and end values in the four
     * bytes of the 32-bit int. Each channel is separately linearly interpolated
     * and the resulting calculated values are recombined into the return value.
     *
     * @param fraction   The fraction from the starting to the ending values
     * @param startValue A 32-bit int value representing colors in the
     * separate bytes of the parameter
     * @param endValue   A 32-bit int value representing colors in the
     * separate bytes of the parameter
     * @return A value that is calculated to be the linearly interpolated
     * result, derived by separating the start and end values into separate
     * color channels and interpolating each one separately, recombining the
     * resulting values in the same way.
     */
    override fun evaluate(fraction: Float, startValue: Any, endValue: Any): Any {
        val result: Any

        if (mDelegate != null) {
            result = mDelegate!!.evaluate(fraction, startValue, endValue)
        } else {
            val startInt = startValue as Int
            val startA = startInt shr 24 and 0xff
            val startR = startInt shr 16 and 0xff
            val startG = startInt shr 8 and 0xff
            val startB = startInt and 0xff

            val endInt = endValue as Int
            val endA = endInt shr 24 and 0xff
            val endR = endInt shr 16 and 0xff
            val endG = endInt shr 8 and 0xff
            val endB = endInt and 0xff

            result = startA + (fraction * (endA - startA)).toInt() shl 24 or
                    (startR + (fraction * (endR - startR)).toInt() shl 16) or
                    (startG + (fraction * (endG - startG)).toInt() shl 8) or
                    startB + (fraction * (endB - startB)).toInt()
        }

        return result
    }

    private fun withDelegate(delegate: android.animation.ArgbEvaluator): ArgbEvaluator {
        this.mDelegate = delegate
        return this
    }

    companion object {
        val newInstance: ArgbEvaluator
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ArgbEvaluator().withDelegate(android.animation.ArgbEvaluator())
            } else {
                ArgbEvaluator()
            }
    }

}