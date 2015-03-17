package com.oprisnik.navdrawer.sample;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.oprisnik.navdrawer.NavDrawerActivity;
import com.oprisnik.navdrawer.entry.NavDrawerDivider;
import com.oprisnik.navdrawer.entry.NavDrawerDividerBeforeSubheader;
import com.oprisnik.navdrawer.entry.NavDrawerEntry;
import com.oprisnik.navdrawer.entry.NavDrawerSettingsEntry;
import com.oprisnik.navdrawer.entry.NavDrawerSubheader;
import com.oprisnik.navdrawer.entry.NavDrawerSupportEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Base drawer activity that defines the drawer structure
 */
public abstract class DemoDrawerActivity extends NavDrawerActivity {

    // Simple entry
    public static final NavDrawerEntry ENTRY1 = new NavDrawerEntry(R.string.title_entry_1,
            R.drawable.abc_ic_menu_selectall_mtrl_alpha);

    // Using Builder
    public static final NavDrawerEntry ENTRY2 = new NavDrawerEntry.Builder()
            .setTitleResId(R.string.title_entry_2)
            .setIconResId(R.drawable.abc_ic_menu_copy_mtrl_am_alpha)
            .build();

    // Custom highlight color
    public static final NavDrawerEntry ENTRY3 = new NavDrawerEntry.Builder()
            .setTitleResId(R.string.title_entry_3)
            .setIconResId(R.drawable.abc_ic_menu_cut_mtrl_alpha)
            .setSelectedColorRes(R.color.accent)
            .build();

    // External entry:
    // Launches external activity -> do not highlight navigation drawer item on click
    // so that the selected item is still correct if the user navigates back to our activity
    public static final NavDrawerEntry ENTRY_MORE_APPS = new NavDrawerEntry.Builder()
            .setTitleResId(R.string.more_apps)
            .setIconResId(R.drawable.abc_ic_search_api_mtrl_alpha)
            .setSelectOnClick(false)
            .build();

    // Settings entry:
    // The second parameter is set to false because the entry should not be selected on click (since it displays a toast)
    public static final NavDrawerEntry ENTRY_SETTINGS = new NavDrawerSettingsEntry(R.string.title_settings, false);

    // Support entry - the second parameter is set to true because the entry is external (opens GitHub)
    public static final NavDrawerEntry ENTRY_SUPPORT = new NavDrawerSupportEntry(R.string.title_support, false);


    public static final List<NavDrawerEntry> NAV_ITEMS = new ArrayList<NavDrawerEntry>();

    static {

        // Our navigation drawer does not change -> we can use a static list

        // First section
        NAV_ITEMS.add(new NavDrawerSubheader(R.string.title_section1));
        NAV_ITEMS.add(ENTRY1);
        NAV_ITEMS.add(ENTRY2);

        // Second section
        // We need to use NavDrawerDividerBeforeSubheader because of different spacing
        NAV_ITEMS.add(new NavDrawerDividerBeforeSubheader());
        NAV_ITEMS.add(new NavDrawerSubheader(R.string.title_section2));


        NAV_ITEMS.add(ENTRY3);

        NAV_ITEMS.add(ENTRY_MORE_APPS);


        NAV_ITEMS.add(new NavDrawerDivider());

        NAV_ITEMS.add(ENTRY_SETTINGS);
        NAV_ITEMS.add(ENTRY_SUPPORT);
    }

    @Override
    public List<NavDrawerEntry> getNavDrawerItems() {
        return NAV_ITEMS;
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

    @Override
    public void onEntrySelected(NavDrawerEntry entry) {
        if (ENTRY1.equals(entry)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (ENTRY2.equals(entry)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (ENTRY3.equals(entry)) {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
            finish();
        } else if (ENTRY_MORE_APPS.equals(entry)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://search?q=pub:Alexander Oprisnik"));
            startActivity(intent);
        } else if (ENTRY_SETTINGS.equals(entry)) {
            Toast.makeText(this, "Settings clicked!", Toast.LENGTH_SHORT).show();
        } else if (ENTRY_SUPPORT.equals(entry)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://github.com/oprisnik/navdrawer"));
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHeaderInfo();
    }
}
