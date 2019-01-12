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

/**
 * Listener for indicator click events
 *
 *
 * Created by Markus on 14.11.2016.
 */
interface OnIndicatorClickedListener {

    /**
     * Called when an indicator is clicked
     *
     * @param pageIndicatorView the PageIndicatorView that holds this indicator
     * @param index             position of this indicator
     */
    fun onIndicatorClicked(pageIndicatorView: PageIndicatorView, index: Int)

}
