package org.kicksound.Controllers.Tabs;

import android.content.Context;

import org.kicksound.Controllers.Tabs.Fragments.EventsFragment;
import org.kicksound.Controllers.Tabs.Fragments.LibraryFragment;
import org.kicksound.Controllers.Tabs.Fragments.NewsFragment;
import org.kicksound.Controllers.Tabs.Fragments.SearchFragment;
import org.kicksound.Controllers.Tabs.Fragments.UserFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabFragmentAdapter extends FragmentPagerAdapter {
    private Context context;

    public TabFragmentAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NewsFragment.newInstance();
            case 1:
                return EventsFragment.newInstance();
            case 2:
                return SearchFragment.newInstance();
            case 3:
                return LibraryFragment.newInstance();
            case 4:
                return UserFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
