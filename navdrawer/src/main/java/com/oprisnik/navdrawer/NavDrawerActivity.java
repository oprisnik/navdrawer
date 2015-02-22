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

package com.oprisnik.navdrawer;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.oprisnik.navdrawer.widget.NavDrawerLayout;

public abstract class NavDrawerActivity extends ActionBarActivity implements NavDrawerLayout.NavigationListener, NavDrawerDataProvider {

    protected static final TypeEvaluator ARGB_EVALUATOR = new ArgbEvaluator();

    private NavDrawerLayout mDrawerLayout = null;

    private ValueAnimator mStatusBarColorAnimator = null;

    protected boolean mUpNavigation = true;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        if (rootView != null) {
            View drawer = rootView.getChildAt(0);
            // setup the drawer layout if we found it
            if (drawer != null && drawer instanceof NavDrawerLayout) {
                setupDrawerLayout((NavDrawerLayout) drawer);
            }
        }
    }

    public void setupDrawerLayout(NavDrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setNavigationListener(this);
        mDrawerLayout.setDataProvider(this);
    }

    public void showUpNavigation(boolean show) {
        mUpNavigation = show;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Drawable drawable = getResources().getDrawable(show ? R.drawable.ic_back : R.drawable.ic_menu);
        int colorControlNormal = Utils.getAttrColor(R.attr.colorControlNormal, getSupportActionBar().getThemedContext());
        drawable.setColorFilter(colorControlNormal, PorterDuff.Mode.SRC_IN);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
    }

    public boolean hasUpNavigation() {
        return mUpNavigation;
    }

    public boolean hasDrawer() {
        return mDrawerLayout != null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (hasDrawer() && !hasUpNavigation() && item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer();
            return true;
        }
        return false;
    }

    public NavDrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public void setStatusBarBackgroundColor(int color) {
        if (mDrawerLayout != null) {
            mDrawerLayout.setStatusBarBackgroundColor(color);
            mDrawerLayout.invalidate();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(color);
            }
        }
    }

    public void animateStatusBarBackgroundColor(int fromColor, int toColor) {
        if (mStatusBarColorAnimator != null) {
            mStatusBarColorAnimator.cancel();
            mStatusBarColorAnimator = null;
        }

        if (mDrawerLayout != null) {
            mStatusBarColorAnimator = ObjectAnimator.ofInt(mDrawerLayout, "statusBarBackgroundColor",
                    fromColor, toColor);
            mStatusBarColorAnimator.setDuration(200);
            mStatusBarColorAnimator.setEvaluator(ARGB_EVALUATOR);
            mStatusBarColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ViewCompat.postInvalidateOnAnimation(mDrawerLayout);
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mStatusBarColorAnimator = ValueAnimator.ofArgb(fromColor, toColor);
                mStatusBarColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        final int val = (Integer) animation.getAnimatedValue();
                        getWindow().setStatusBarColor(val);
                    }
                });
            }
        }
        if (mStatusBarColorAnimator != null) {
            mStatusBarColorAnimator.start();
        }
    }
}
