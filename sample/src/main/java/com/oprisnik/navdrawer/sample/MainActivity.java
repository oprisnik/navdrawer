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

package com.oprisnik.navdrawer.sample;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oprisnik.navdrawer.NavDrawerActivity;
import com.oprisnik.navdrawer.NavDrawerDivider;
import com.oprisnik.navdrawer.NavDrawerDividerBeforeSubheader;
import com.oprisnik.navdrawer.NavDrawerEntry;
import com.oprisnik.navdrawer.NavDrawerSettingsEntry;
import com.oprisnik.navdrawer.NavDrawerSubheader;
import com.oprisnik.navdrawer.NavDrawerSupportEntry;
import com.oprisnik.navdrawer.widget.NavDrawerLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends NavDrawerActivity {

    private static final List<NavDrawerEntry> NAV_ITEMS = new ArrayList<NavDrawerEntry>();

    static {
        // First section
        NAV_ITEMS.add(new NavDrawerSubheader(R.string.title_section1));

        // Simple entry
        NAV_ITEMS.add(new NavDrawerEntry(R.string.title_entry_1, R.drawable.abc_ic_menu_selectall_mtrl_alpha));

        // Using Builder
        NAV_ITEMS.add(new NavDrawerEntry.Builder()
                .setTitleResId(R.string.title_entry_2)
                .setIconResId(R.drawable.abc_ic_menu_copy_mtrl_am_alpha)
                .build());

        // Second section
        // We need to use NavDrawerDividerBeforeSubheader because of different spacing
        NAV_ITEMS.add(new NavDrawerDividerBeforeSubheader());
        NAV_ITEMS.add(new NavDrawerSubheader(R.string.title_section2));

        // Custom highlight color
        NAV_ITEMS.add(new NavDrawerEntry.Builder()
                .setTitleResId(R.string.title_entry_3)
                .setIconResId(R.drawable.abc_ic_menu_cut_mtrl_alpha)
                .setSelectedColorRes(R.color.accent)
                .build());


        NAV_ITEMS.add(new NavDrawerDivider());

        // Settings entry
        NAV_ITEMS.add(new NavDrawerSettingsEntry(R.string.title_settings));
        // Support entry
        NAV_ITEMS.add(new NavDrawerSupportEntry(R.string.title_support));
    }

    private NavDrawerEntry mSelected = NAV_ITEMS.get(1);

    private TextView mSelectedText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_actionbar);
        if (tb != null) {
            setSupportActionBar(tb);
        }

        mSelectedText = (TextView) findViewById(R.id.selected);

        Button custom = (Button) findViewById(R.id.button);
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawerLayout().setHeader(R.layout.custom_header);
                ((Button) getDrawerLayout().getHeader().findViewById(R.id.custom_header_btn)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Custom header button clicked!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button defaultBtn = (Button) findViewById(R.id.button2);
        defaultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawerLayout().setDefaultHeader();
                updateHeaderInfo();
            }
        });

        Button removeBtn = (Button) findViewById(R.id.button3);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawerLayout().removeHeader();
            }
        });

        // we do not want the up arrow (<-) here
        showUpNavigation(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHeaderInfo();
        update(mSelected);
    }

    @Override
    public List<NavDrawerEntry> getNavDrawerItems() {
        return NAV_ITEMS;
    }

    @Override
    public NavDrawerEntry getSelectedNavDrawerItem() {
        return mSelected;
    }

    @Override
    public void onEntrySelected(NavDrawerEntry entry) {
        mSelected = entry;
        update(mSelected);
        Toast.makeText(this, entry.getTitleResId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderClicked() {
        Toast.makeText(this, "Header clicked!", Toast.LENGTH_SHORT).show();
    }

    protected void updateHeaderInfo() {
        getDrawerLayout().setHeaderTitle("Jonathan Lee");
        getDrawerLayout().setHeaderSubtitle("heyfromjonathan@gmail.com");
//        getDrawerLayout().setHeaderIcon(getResources().getDrawable(R.drawable.ic_launcher));
        getDrawerLayout().setHeaderBackgroundColor(getResources().getColor(R.color.primary));
    }

    protected void update(NavDrawerEntry active) {
        // we just update our TextView
        if (mSelectedText != null && active != null) {
            mSelectedText.setText(getString(R.string.selected, getString(active.getTitleResId())));
        }
    }
}
