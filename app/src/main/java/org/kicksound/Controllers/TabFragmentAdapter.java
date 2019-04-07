package org.kicksound.Controllers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabFragmentAdapter extends FragmentPagerAdapter {
    Context context;

    public TabFragmentAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SearchFragment.newInstance();
            case 1:
                return EventsFragment.newInstance();
            case 2:
                return LivesFragment.newInstance();
            case 3:
                return LibraryFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
