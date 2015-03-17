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
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oprisnik.navdrawer.entry.NavDrawerEntry;

public class MainActivity extends DemoDrawerActivity {

    private NavDrawerEntry mSelected = ENTRY1;

    private TextView mSelectedText;

    public static final String KEY_ENTRY = "entry_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(0, 0);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_actionbar);
        if (tb != null) {
            setSupportActionBar(tb);
        }

        mSelectedText = (TextView) findViewById(R.id.selected);

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(KEY_ENTRY, 0);
            updateEntry(index);
        } else if (getIntent() != null && getIntent().hasExtra(KEY_ENTRY)) {
            int index = getIntent().getIntExtra(KEY_ENTRY, 0);
            updateEntry(index);
        }

        // we do not want the up arrow (<-) here
        showUpNavigation(false);
    }

    public void setCustomHeader(View view) {
        getDrawerLayout().setHeader(R.layout.custom_header);
        getDrawerLayout().getHeader().findViewById(R.id.custom_header_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Custom header button clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setDefaultHeader(View view) {
        getDrawerLayout().setDefaultHeader();
        updateHeaderInfo();
    }

    public void removeHeader(View view) {
        getDrawerLayout().removeHeader();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateText();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ENTRY, ENTRY1.equals(mSelected) ? 0 : 1);
    }

    protected void updateEntry(int index) {
        if (index == 0) {
            mSelected = ENTRY1;
        } else {
            mSelected = ENTRY2;
        }
    }

    @Override
    public NavDrawerEntry getSelectedNavDrawerItem() {
        return mSelected;
    }

    @Override
    public void onEntrySelected(NavDrawerEntry entry) {
        // special behavior for entry1 and entry2 -> we don't switch the activity, we only change the text
        if (ENTRY1.equals(entry) || ENTRY2.equals(entry)) {
            mSelected = entry;
            updateText();
        } else {
            super.onEntrySelected(entry);
        }
    }

    protected void updateText() {
        // we just update our TextView
        if (mSelectedText != null && mSelected != null) {
            mSelectedText.setText(getString(R.string.selected, getString(mSelected.getTitleResId())));
        }
    }
}
