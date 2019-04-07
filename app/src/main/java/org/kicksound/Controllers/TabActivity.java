package org.kicksound.Controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import org.kicksound.R;

public class TabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        this.configureViewPager();
    }

    private void configureViewPager() {
        ViewPager pager = findViewById(R.id.tab_view_pager);
        pager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), getApplicationContext()));

        TabLayout tabs = findViewById(R.id.tab_bar);
        tabs.setupWithViewPager(pager);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.getTabAt(0).setIcon(R.drawable.ic_action_search);
        tabs.getTabAt(1).setIcon(R.drawable.ic_action_event);
        tabs.getTabAt(2).setIcon(R.drawable.ic_action_live);
        tabs.getTabAt(3).setIcon(R.drawable.ic_action_library);
    }
}
