# PageIndicatorView
A small, simple, animated page indicator without the need for a viewpager.

# Usage
To use this view just include it in your depencencies using

    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
    
in your project build.gradle file and

    dependencies {
        compile 'com.github.markusressel:PageIndicatorView:v0.9'
    }
    
in your desired module build.gradle file.

After successfull gradle build you will be able to use this in your xml layout files

    <de.markusressel.android.library.pageindicatorview.PageIndicatorView
            android:id="@+id/pageIndicator"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>

If you want you can change the style of the indicators by using attributes

    <de.markusressel.android.library.pageindicatorview.PageIndicatorView
            android:id="@+id/pageIndicator"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal|bottom"
            android:layout_marginBottom="38dp"
            app:piv_activeIndicatorColorFill="?attr/colorAccent"
            app:piv_activeIndicatorColorStroke="?attr/colorAccent"
            app:piv_activeIndicatorSize="10dp"
            app:piv_inactiveIndicatorColorFill="#444444"
            app:piv_inactiveIndicatorColorStroke="#444444"
            app:piv_inactiveIndicatorSize="7dp"
            app:piv_indicatorGap="10dp"
            app:piv_initialPageIndex="0"
            app:piv_pageCount="3"/>
            
# License

    Copyright (c) 2016 Markus Ressel

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
