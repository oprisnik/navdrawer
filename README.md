# Material Design Navigation Drawer for Android

Simple navigation drawer library for Android applications.

## Screenshots

[![Demo](art/demo-small.png)](art/demo.png)
[![Custom highlight color](art/demo-custom-color-small.png)]((art/demo-custom-color.png))
[![Custom header](art/demo-custom-header-small.png)](art/demo-custom-header.png)


## Features

* Follows the [material design guidelines](http://www.google.com/design/spec/patterns/navigation-drawer.html)
* Pre-defined navigation drawer entries
  * Simple icon & text entry
  * Subheaders
  * Divider
  * Settings entry
  * Support entry ("Help & feedback")
* Default navigation drawer header with title, subtitle, icon, and background image
* Custom navigation drawer header support

## Sample

A small sample application can be found [here](sample).


## Usage

Just add the dependency to your `build.gradle`:

```groovy
dependencies {
  compile 'com.oprisnik:navdrawer:1.0.0'
}
```

### Activity

Declare `com.oprisnik.navdrawer.widget.NavDrawerLayout` as the root element of your layout file.
Add your content as a child of the `NavDrawerLayout`.

Example `activity.xml`:

```xml
<com.oprisnik.navdrawer.widget.NavDrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">

    <TextView
        android:layout_width="match_parent"
        android:padding="@dimen/padding"
        android:layout_height="wrap_content"
        android:text="Here comes your content!"/>
</com.oprisnik.navdrawer.widget.NavDrawerLayout>
```

Then, let your Activity extend `com.oprisnik.navdrawer.NavDrawerActivity` and implement the required methods.
If you do not want to extend `NavDrawerActivity`, you can also take a look at [NavDrawerActivity.java](navdrawer/src/main/java/com/oprisnik/navdrawer/NavDrawerActivity.java) and add a similar logic to your Activity.

### Navigation drawer structure

The structure of the navigation drawer is a simple `List<NavDrawerEntry>` and there are several
pre-defined elements available:

* NavDrawerEntry - clickable entries consisting of text and an icon
* NavDrawerSubheader - section header that is not clickable
* NavDrawerDivider - simple horizontal divider
* NavDrawerDividerBeforeSubheader - horizontal divider to be used right above a subheader (in order to have correct spacing)
* NavDrawerSettingsEntry - Default settings entry
* NavDrawerSupportEntry - Default support entry ("Help & feedback")

For NavDrawerEntries, a builder interface is available. Example:

```java
NavDrawerEntry e = new NavDrawerEntry.Builder()
                          .setTitleResId(R.string.my_title)
                          .setIconResId(R.drawable.ic_my_icon)
                          .setSelectedColorRes(R.color.my_color)
                          .build();
```

### Header

The navigation drawer also features a default header with a title, subtitle, icon and background (cover image).
You can access the header in your Activity as follows:

```java
getDrawerLayout().setHeaderTitle("Jonathan Lee");
getDrawerLayout().setHeaderSubtitle("heyfromjonathan@gmail.com");
getDrawerLayout().setHeaderIcon(getResources().getDrawable(R.drawable.my_image));
getDrawerLayout().setHeaderBackgroundColor(getResources().getColor(R.color.my_color));
```

You can also set a custom header by calling


```java
getDrawerLayout().setHeader(yourHeader);
```

or revert back to the default header:

```java
getDrawerLayout().setDefaultHeader();
```

If you use the default header, `onHeaderClicked()` will be called if the header is clicked by the user.

## Acknowledgements

* This project uses several code snippets from the [Google I/O Android app](https://github.com/google/iosched), including [BezelImageView](navdrawer/src/main/java/google/samples/apps/iosched/ui/widget/BezelImageView.java) and [ScrimInsetsScrollView](navdrawer/src/main/java/google/samples/apps/iosched/ui/widget/ScrimInsetsScrollView.java).

## Copyright


    Copyright 2015 Alexander Oprisnik

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
