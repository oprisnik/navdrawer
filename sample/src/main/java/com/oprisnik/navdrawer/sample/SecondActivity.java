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

import com.oprisnik.navdrawer.entry.NavDrawerEntry;

public class SecondActivity extends DemoDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        overridePendingTransition(0, 0);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_actionbar);
        if (tb != null) {
            setSupportActionBar(tb);
        }
        // we do not want the up arrow (<-) here
        showUpNavigation(false);
    }

    @Override
    public NavDrawerEntry getSelectedNavDrawerItem() {
        return ENTRY3;
    }

}
