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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.markusressel.android.library.pageindicatorview.OnIndicatorClickedListener
import de.markusressel.android.library.pageindicatorview.PageIndicatorView
import kotlinx.android.synthetic.main.demonstration_page.*

/**
 * A simple page showing different possibilities for styling
 *
 *
 * Created by Markus on 10.11.2016.
 */
class DemonstrationPage : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.demonstration_page, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onIndicatorClickedListener = object : OnIndicatorClickedListener {
            override fun onIndicatorClicked(pageIndicatorView: PageIndicatorView, index: Int) {
                pageIndicatorView.setCurrentPage(index, true)
            }
        }

        setOf(pageIndicator1, pageIndicator2, pageIndicator3,
                pageIndicator4, pageIndicator5, pageIndicator6).forEach {
            it.setOnIndicatorClickedListener(onIndicatorClickedListener)
        }
    }

    companion object {

        fun newInstance(): DemonstrationPage {
            return DemonstrationPage()
        }
    }

}