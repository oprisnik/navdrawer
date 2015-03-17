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

package com.oprisnik.navdrawer.entry;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oprisnik.navdrawer.R;
import com.oprisnik.navdrawer.widget.NavDrawerLayout;

/**
 * Navigation drawer entry.
 */
public class NavDrawerEntry {

    public static final int DEFAULT_LAYOUT_RES_ID = R.layout.navdrawer_item;

    protected static final boolean DEFAULT_CLICKABLE = true;
    protected static final boolean DEFAULT_LAUNCH_DELAYED = true;
    protected static final boolean DEFAULT_FADE_OUT_CONTENT = false;
    protected static final boolean DEFAULT_SELECT_ON_CLICK = true;

    @StringRes
    private int mTitleResId;

    @DrawableRes
    private int mIconResId;

    @LayoutRes
    private int mLayoutResId;


    @ColorRes
    private int mSelectedColorRes;
    private boolean mHasCustomColor = false;

    private boolean mFadeOutContent = DEFAULT_FADE_OUT_CONTENT;

    private boolean mLaunchDelayed = DEFAULT_LAUNCH_DELAYED;

    private boolean mIsClickable = DEFAULT_CLICKABLE;
    
    private boolean mSelectOnClick = DEFAULT_SELECT_ON_CLICK;


    public NavDrawerEntry(@StringRes int titleResId, @DrawableRes int iconResId) {
        this(titleResId, iconResId, DEFAULT_LAYOUT_RES_ID);
    }

    public NavDrawerEntry(@StringRes int titleResId, @DrawableRes int iconResId, @LayoutRes int layoutResId) {
        this();
        mLayoutResId = layoutResId;
        mIconResId = iconResId;
        mTitleResId = titleResId;
        mHasCustomColor = false;
    }

    public NavDrawerEntry(@StringRes int titleResId, @DrawableRes int iconResId, @LayoutRes int layoutResId, @ColorRes int selectedColorRes) {
        this(titleResId, iconResId, layoutResId);
        mSelectedColorRes = selectedColorRes;
        mHasCustomColor = true;
    }

    public NavDrawerEntry() {
    }

    public View createView(Context context, ViewGroup container, boolean selected, final NavDrawerLayout.NavigationListener listener) {
        final View view = LayoutInflater.from(context).inflate(mLayoutResId, container, false);

        ImageView iconView = (ImageView) view.findViewById(android.R.id.icon);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        if (iconView != null) {
            // set icon and text
            iconView.setVisibility(mIconResId > 0 ? View.VISIBLE : View.GONE);
            if (mIconResId > 0) {
                iconView.setImageResource(mIconResId);
            }
        }
        if (titleView != null) {
            titleView.setText(mTitleResId);
        }
        formatView(context, view, selected);

        if (isClickable()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEntrySelected(NavDrawerEntry.this);
                }
            });
        }

        return view;
    }

    public void formatView(Context context, View view, boolean selected) {
        if (!isClickable() || view == null) {
            return;
        }
        ImageView iconView = (ImageView) view.findViewById(android.R.id.icon);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);

        if (selected) {
            int color;
            if (mHasCustomColor) {
                color = context.getResources().getColor(mSelectedColorRes);
            } else {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
                color = typedValue.data;
            }
            if (titleView != null) {
                titleView.setTextColor(color);
            }
            if (iconView != null) {
                iconView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
            view.setActivated(true);
        } else {
            if (titleView != null) {
                titleView.setTextColor(context.getResources().getColor(R.color.navdrawer_text_color));
            }
            if (iconView != null) {
                iconView.setColorFilter(context.getResources().getColor(R.color.navdrawer_icon_tint), PorterDuff.Mode.SRC_IN);
            }
            view.setActivated(false);
        }
    }

    public boolean launchDelayed() {
        return mLaunchDelayed;
    }

    public boolean fadeOutContent() {
        return mFadeOutContent;
    }

    public void setLaunchDelayed(boolean launchDelayed) {
        mLaunchDelayed = launchDelayed;
    }

    public void setFadeOutContent(boolean fadeOutContent) {
        mFadeOutContent = fadeOutContent;
    }

    public boolean isClickable() {
        return mIsClickable;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getIconResId() {
        return mIconResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

    public int getSelectedColorRes() {
        return mSelectedColorRes;
    }

    public boolean hasCustomColor() {
        return mHasCustomColor;
    }

    public boolean selectOnClick() {
        return mSelectOnClick;
    }

    public void setSelectOnClick(boolean isExternal) {
        mSelectOnClick = isExternal;
    }

    public static class Builder {

        @StringRes
        private int mTitleResId;

        @DrawableRes
        private int mIconResId;

        @LayoutRes
        private int mLayoutResId = DEFAULT_LAYOUT_RES_ID;


        @ColorRes
        private int mSelectedColorRes;
        private boolean mHasCustomColor = false;

        private boolean mFadeOutContent = DEFAULT_FADE_OUT_CONTENT;

        private boolean mLaunchDelayed = DEFAULT_LAUNCH_DELAYED;

        private boolean mIsClickable = DEFAULT_CLICKABLE;
        
        private boolean mSelectOnClick = DEFAULT_SELECT_ON_CLICK;

        public Builder() {
        }

        /**
         * Set a custom layout resource ID for the navigation drawer entry.
         *  
         * @param layoutResId the custom layout resource ID
         * @return the builder
         */
        public Builder setLayoutResId(@LayoutRes int layoutResId) {
            mLayoutResId = layoutResId;
            return this;
        }

        /**
         * Set the icon of the navigation drawer entry.
         *  
         * @param iconResId the icon resource ID to use
         * @return the builder
         */
        public Builder setIconResId(@DrawableRes int iconResId) {
            mIconResId = iconResId;
            return this;
        }

        /**
         * Set the title of the navigation drawer entry.
         *
         * @param titleResId the string resource ID to use
         * @return the builder
         */
        public Builder setTitleResId(@StringRes int titleResId) {
            mTitleResId = titleResId;
            return this;
        }
        
        /**
         * Set a custom selected color (text color of the selected navigation drawer entry).
         *
         * @param selectedColorRes the color resource ID to use
         * @return the builder
         */
        public Builder setSelectedColorRes(@ColorRes int selectedColorRes) {
            mSelectedColorRes = selectedColorRes;
            mHasCustomColor = true;
            return this;
        }
        
        /**
         * Specify whether the main content should be faded out when the entry is selected.
         *
         * @param fadeOutContent true if the content should be faded out
         * @return the builder
         */
        public Builder setFadeOutContent(boolean fadeOutContent) {
            mFadeOutContent = fadeOutContent;
            return this;
        }
        
        /**
         * Specify whether the click action should be delayed.
         * If set to true (default), the click action will be delayed until the navigation drawer
         * closing animation is done. 
         *
         * @param launchDelayed true if the action should be delayed
         * @return the builder
         */
        public Builder setLaunchDelayed(boolean launchDelayed) {
            mLaunchDelayed = launchDelayed;
            return this;
        }
        
        /**
         * Specify whether the navigation drawer entry is clickable.
         * For example, subheaders are not clickable, normal entries are. 
         *
         * @param isClickable true if the entry is clickable
         * @return the builder
         */
        public Builder setClickable(boolean isClickable) {
            mIsClickable = isClickable;
            return this;
        }
        
        /**
         * Specify whether the navigation drawer entry should be selected on click.
         * If set to true, the entry will be selected when it is clicked.
         * For example, if you launch an external activity or if your entry displays
         * a toast / dialog, you can setSelectOnClick to false.
         * Then, once the entry is selected, the new activity can be started. When the user
         * returns to your activity, this entry will not be selected. 
         *
         * @param selectOnClick false if the entry should not be highlighted on click
         * @return the builder
         */
        public Builder setSelectOnClick(boolean selectOnClick) {
            mSelectOnClick = selectOnClick;
            return this;
        }

        /**
         * Build the navigation drawer entry.
         *
         * @return the navigation drawer entry
         */
        public NavDrawerEntry build() {
            NavDrawerEntry entry = new NavDrawerEntry();
            entry.mLayoutResId = mLayoutResId;
            entry.mIconResId = mIconResId;
            entry.mTitleResId = mTitleResId;
            entry.mFadeOutContent = mFadeOutContent;
            entry.mSelectedColorRes = mSelectedColorRes;
            entry.mHasCustomColor = mHasCustomColor;
            entry.mLaunchDelayed = mLaunchDelayed;
            entry.mIsClickable = mIsClickable;
            entry.mSelectOnClick = mSelectOnClick;
            return entry;
        }
    }

}
