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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.markusressel.android.library.pageindicatorview.OnIndicatorClickedListener;
import de.markusressel.android.library.pageindicatorview.PageIndicatorView;

/**
 * A simple page showing different possibilities for styling
 * <p>
 * Created by Markus on 10.11.2016.
 */
public class DemonstrationPage extends Fragment {

    public static DemonstrationPage newInstance() {
        Bundle args = new Bundle();
        DemonstrationPage fragment = new DemonstrationPage();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.demonstration_page, container, false);

        OnIndicatorClickedListener onIndicatorClickedListener = new OnIndicatorClickedListener() {
            @Override
            public void onIndicatorClicked(PageIndicatorView pageIndicator, int index) {
                pageIndicator.setCurrentPage(index, true);
            }
        };

        ArrayList<PageIndicatorView> pageIndicators = new ArrayList<>();

        PageIndicatorView pageIndicator1 = (PageIndicatorView) rootView.findViewById(R.id.pageIndicator1);
        PageIndicatorView pageIndicator2 = (PageIndicatorView) rootView.findViewById(R.id.pageIndicator2);
        PageIndicatorView pageIndicator3 = (PageIndicatorView) rootView.findViewById(R.id.pageIndicator3);
        PageIndicatorView pageIndicator4 = (PageIndicatorView) rootView.findViewById(R.id.pageIndicator4);
        PageIndicatorView pageIndicator5 = (PageIndicatorView) rootView.findViewById(R.id.pageIndicator5);
        PageIndicatorView pageIndicator6 = (PageIndicatorView) rootView.findViewById(R.id.pageIndicator6);

        pageIndicators.add(pageIndicator1);
        pageIndicators.add(pageIndicator2);
        pageIndicators.add(pageIndicator3);
        pageIndicators.add(pageIndicator4);
        pageIndicators.add(pageIndicator5);
        pageIndicators.add(pageIndicator6);

        for (PageIndicatorView pageIndicator : pageIndicators) {
            pageIndicator.setOnIndicatorClickedListener(onIndicatorClickedListener);
        }

        return rootView;
    }

}