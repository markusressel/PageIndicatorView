# PageIndicatorView
A small, simple, animated page indicator without the need for a viewpager.

![Example](http://i.giphy.com/l3vRccyQXcHc1kDQs.gif)

# Why
I built this library because I was tired of all the existing page indicators out there
to require a viewpager as this prevented me from using in on an Android Wear project that time.

# Usage
To use this view just include it in your depencencies using

```groovy
repositories {
    ...
    maven { url "https://jitpack.io" }
}
```
    
in your project build.gradle file and

```groovy
dependencies {
    compile 'com.github.markusressel:PageIndicatorView:v1.0.0'
}
```
    
in your desired module build.gradle file.

After successfull gradle build you will be able to use this in your xml layout files

```xml
<de.markusressel.android.library.pageindicatorview.PageIndicatorView
        android:id="@+id/pageIndicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
```

If you want you can change the style of the indicators by using attributes

```xml
<de.markusressel.android.library.pageindicatorview.PageIndicatorView
        android:id="@+id/pageIndicator"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="0"
        android:paddingTop="@dimen/activity_vertical_margin"
        app:piv_activeIndicatorFillColor="?attr/colorAccent"
        app:piv_activeIndicatorFillSize="20dp"
        app:piv_activeIndicatorStrokeColor="@android:color/white"
        app:piv_activeIndicatorStrokeWidth="5dp"
        app:piv_inactiveIndicatorFillColor="?attr/colorAccent"
        app:piv_inactiveIndicatorFillSize="15dp"
        app:piv_inactiveIndicatorStrokeColor="@android:color/darker_gray"
        app:piv_inactiveIndicatorStrokeWidth="10dp"
        app:piv_pageCount="5" />
```

You can also set these values in code using the following methods:

```kotlin

pageIndicator.activeIndicatorSize = pxFromDp(this, activeIndicatorSize)
pageIndicator.activeIndicatorFillColor = activeIndicatorColorFill
pageIndicator.activeIndicatorStrokeColor = activeIndicatorColorStroke
pageIndicator.activeIndicatorStrokeWidth = pxFromDp(this, activeIndicatorStrokeWidth)

pageIndicator.inactiveIndicatorSize = pxFromDp(this, inactiveIndicatorSize)
pageIndicator.inactiveIndicatorFillColor = inactiveIndicatorColorFill
pageIndicator.inactiveIndicatorStrokeColor = inactiveIndicatorColorStroke
pageIndicator.inactiveIndicatorStrokeWidth = pxFromDp(this, inactiveIndicatorStrokeWidth)

pageIndicator.indicatorGap = pxFromDp(this, indicatorGap)

pageIndicator.pageCount = pageCount

pageIndicator.setCurrentPage(initialPageIndex)          // animated
pageIndicator.setCurrentPage(initialPageIndex, false)   // not animated
```

To be able to react to "OnClick" events on indicators, you can set a OnIndicatorClickedListener like this:

```kotlin
pageIndicator.setOnIndicatorClickedListener(object : OnIndicatorClickedListener {
    override fun onIndicatorClicked(pageIndicatorView: PageIndicatorView, index: Int) {
        // set the indicator view to the clicked position
        pageIndicatorView.setCurrentPage(index, true)

        // set your view pager to the clicked position
        viewPager.currentItem = index
    }
})
```

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
