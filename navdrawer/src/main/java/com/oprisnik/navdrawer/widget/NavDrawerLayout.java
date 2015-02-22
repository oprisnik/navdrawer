/*
 * Copyright 2015 Alexander Oprisnik
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

package com.oprisnik.navdrawer.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oprisnik.navdrawer.NavDrawerDataProvider;
import com.oprisnik.navdrawer.entry.NavDrawerEntry;
import com.oprisnik.navdrawer.R;
import com.oprisnik.navdrawer.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavDrawerLayout extends DrawerLayout {

    public interface NavigationListener {
        public void onEntrySelected(NavDrawerEntry entry);

        public void onHeaderClicked();
    }


    // delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    // fade in and fade out durations for the main content when switching between
    // different Activities of the app through the Nav Drawer
    public static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
    public static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    private static final String PREF_SHOW_DRAWER = "openDrawer";
    private static final String PREFS_FILE_NAME = "drawer";

    @LayoutRes
    private static final int NAVDRAWER_LAYOUT = R.layout.navdrawer;

    @LayoutRes
    private static final int NAVDRAWER_DEFAULT_HEADER_LAYOUT = R.layout.navdrawer_header;

    private Handler mHandler;

    private List<NavDrawerEntry> mEntryList;
    private Map<NavDrawerEntry, View> mEntryMap;

    private ViewGroup mNavdrawer = null;
    private ViewGroup mDrawerItemsListContainer = null;

    private View mHeader = null;

    private NavDrawerDataProvider mDataProvider;

    private NavigationListener mNavigationListener = null;
    
    private boolean mHasHeader = true;
    
    @LayoutRes
    private int mHeaderLayoutRes = NAVDRAWER_DEFAULT_HEADER_LAYOUT;

    private NavigationListener mInternalListener = new NavigationListener() {
        @Override
        public void onEntrySelected(final NavDrawerEntry entry) {
            if (entry == mDataProvider.getSelectedNavDrawerItem()) {
                closeDrawer();
                return;
            }
            if (entry.launchDelayed()) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyListeners(entry);
                    }
                }, NAVDRAWER_LAUNCH_DELAY);
                // set the selected item correctly
                for (NavDrawerEntry e : mEntryList) {
                    e.formatView(getContext(), mEntryMap.get(e), e.equals(entry));
                }
            } else {
                notifyListeners(entry);
            }
            if (entry.fadeOutContent()) {
                fadeOutContent();
            }

            closeDrawer();
        }

        @Override
        public void onHeaderClicked() {
            // not used
        }
    };

    private OnClickListener mHeaderClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mNavigationListener != null) {
                mNavigationListener.onHeaderClicked();
            }
        }
    };

    public NavDrawerLayout(Context context) {
        this(context, null);
    }

    public NavDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mEntryList = new ArrayList<NavDrawerEntry>();
        mEntryMap = new HashMap<NavDrawerEntry, View>();
        mHandler = new Handler();
        setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        int color = Utils.getAttrColor(R.attr.colorPrimaryDark, context);
        setStatusBarBackgroundColor(color);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.NavDrawerLayout,
                defStyle, 0);
        try {
            mHasHeader = a.getBoolean(R.styleable.NavDrawerLayout_hasHeader, mHasHeader);
            mHeaderLayoutRes = a.getResourceId(R.styleable.NavDrawerLayout_headerLayout, mHeaderLayoutRes); 
        } finally {
            a.recycle();
        }
    }

    public void fadeOutContent() {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (isContentView(v)) {
                v.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
            }
        }
    }

    public void fadeInContent() {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (isContentView(v)) {
                v.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        // we add the nav drawer
        mNavdrawer = (ViewGroup) inflater.inflate(NAVDRAWER_LAYOUT, this, false);
        if (mNavdrawer != null) {
            addView(mNavdrawer);
            mDrawerItemsListContainer = (ViewGroup) mNavdrawer.findViewById(android.R.id.list);
            if (mHasHeader) {
                mHeader = inflater.inflate(mHeaderLayoutRes, mNavdrawer, false);
                setHeader(mHeader);
            }
            if (mHeader != null) {
                mHeader.setOnClickListener(mHeaderClickListener);
            }
        }
        super.onFinishInflate();
    }

    public void setDataProvider(NavDrawerDataProvider dataProvider) {
        mDataProvider = dataProvider;
        updateItems();
        // show the drawer on first start
        if (shouldShowDrawer()) {
            openDrawer();
            markDrawerShown();
        }
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        mNavigationListener = navigationListener;
    }

    protected boolean isContentView(View child) {
        return ((LayoutParams) child.getLayoutParams()).gravity == Gravity.NO_GRAVITY;
    }

    protected void updateItems() {
        if (!hasDrawer()) {
            return; // nothing to do
        }
        mEntryList.clear();
        mEntryList.addAll(mDataProvider.getNavDrawerItems());
        createNavDrawerItems();
    }

    protected void createNavDrawerItems() {
        mEntryMap.clear();
        if (mDrawerItemsListContainer == null) {
            return;
        }
        for (NavDrawerEntry entry : mEntryList) {
            View v = entry.createView(getContext(), mDrawerItemsListContainer,
                    entry == mDataProvider.getSelectedNavDrawerItem(), mInternalListener);
            mEntryMap.put(entry, v);
            mDrawerItemsListContainer.addView(v);
        }
    }

    public boolean hasDrawer() {
        return mNavdrawer != null;
    }

    public boolean isDrawerOpen() {
        if (mNavdrawer != null) {
            return isDrawerOpen(mNavdrawer);
        }
        return false;
    }

    public void openDrawer() {
        if (mNavdrawer != null) {
            openDrawer(mNavdrawer);
        }
    }

    public void closeDrawer() {
        if (mNavdrawer != null) {
            closeDrawer(mNavdrawer);
        }
    }

    public View getHeader() {
        return mHeader;
    }

    public void removeHeader() {
        setHeader(null);
    }

    public void setHeader(View header) {
        if (mNavdrawer != null) {
            ViewGroup contentHolder = (ViewGroup) mNavdrawer.findViewById(R.id.navdrawer_content_holder);
            if (mHeader != null) {
                // replace the header
                contentHolder.removeView(mHeader);
                mHeader = null;
            }
            mHeader = header;
            if (mHeader != null) {
                mHasHeader = true;
                contentHolder.addView(mHeader, 0);
            } else {
                mHasHeader = false;
            }
        }
    }

    public void setHeader(@LayoutRes int headerId) {
        View header = LayoutInflater.from(getContext()).inflate(headerId,
                this, false);
        setHeader(header);
    }

    public void setDefaultHeader() {
        setHeader(NAVDRAWER_DEFAULT_HEADER_LAYOUT);
        if (mHeader != null) {
            mHeader.setOnClickListener(mHeaderClickListener);
        }
    }

    public void setHeaderTitle(String title) {
        NavDrawerHeader header = getDefaultHeader();
        if (header != null) {
            header.setTitle(title);
        }
    }

    public void setHeaderSubtitle(String subtitle) {
        NavDrawerHeader header = getDefaultHeader();
        if (header != null) {
            header.setSubtitle(subtitle);
        }
    }

    public void setHeaderIcon(Drawable icon) {
        NavDrawerHeader header = getDefaultHeader();
        if (header != null) {
            header.setIcon(icon);
        }
    }

    public void setHeaderBackground(Drawable background) {
        NavDrawerHeader header = getDefaultHeader();
        if (header != null) {
            Utils.setBackground(header, background);
        }
    }

    public void setHeaderBackgroundColor(int backgroundColor) {
        NavDrawerHeader header = getDefaultHeader();
        if (header != null) {
            header.setBackgroundColor(backgroundColor);
        }
    }

    public void setHeaderBackgroundResource(int backgroundResource) {
        NavDrawerHeader header = getDefaultHeader();
        if (header != null) {
            header.setBackgroundResource(backgroundResource);
        }
    }

    public boolean hasDefaultHeader() {
        return mHeader instanceof NavDrawerHeader;
    }


    protected SharedPreferences getPrefs() {
        return getContext().getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    protected boolean shouldShowDrawer() {
        SharedPreferences sp = getPrefs();
        return sp.getBoolean(PREF_SHOW_DRAWER, true);
    }

    protected void markDrawerShown() {
        SharedPreferences sp = getPrefs();
        sp.edit().putBoolean(PREF_SHOW_DRAWER, false).apply();
    }

    protected void notifyListeners(NavDrawerEntry entry) {
        if (mNavigationListener != null) {
            mNavigationListener.onEntrySelected(entry);
        }
    }


    private NavDrawerHeader getDefaultHeader() {
        if (hasDefaultHeader()) {
            return (NavDrawerHeader) mHeader;
        }
        return null;
    }
}
