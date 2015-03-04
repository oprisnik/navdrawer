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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.oprisnik.navdrawer.R;

public class NavDrawerHeader extends FrameLayout {

    private String mTitle;
    private String mSubtitle;
    private Drawable mIcon;

    private TextView mTitleText;
    private TextView mSubtitleText;
    private ImageView mIconView;

    public NavDrawerHeader(Context context) {
        super(context);
    }

    public NavDrawerHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavDrawerHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavDrawerHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleText = (TextView) findViewById(android.R.id.text1);
        mSubtitleText = (TextView) findViewById(android.R.id.text2);
        mIconView = (ImageView) findViewById(android.R.id.icon);
        updateData();
    }

    protected void updateData() {
        updateText(mTitleText, mTitle);
        updateText(mSubtitleText, mSubtitle);
        updateIcon();
    }

    protected void updateText(TextView view, String text) {
        if (view != null) {
            view.setText(text == null ? "" : text);
        }
    }

    protected void updateIcon() {
        if (mIconView != null) {
            if (mIcon != null) {
                mIconView.setImageDrawable(mIcon);
            } else {
                mIconView.setImageResource(R.drawable.person_image_empty);
            }
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
        updateText(mTitleText, mTitle);
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String subtitle) {
        mSubtitle = subtitle;
        updateText(mSubtitleText, mSubtitle);
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
        updateIcon();
    }
}
