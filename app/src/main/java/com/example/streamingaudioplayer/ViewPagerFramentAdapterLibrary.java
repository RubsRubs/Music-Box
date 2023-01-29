package com.example.streamingaudioplayer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFramentAdapterLibrary extends FragmentStateAdapter {

    private String[] titles = new String[]{"1", "2", "3"};

    public ViewPagerFramentAdapterLibrary(@NonNull LibraryFragment libraryFragment) {
        super(libraryFragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new TabLayOutPlayListsFragment();
                break;
            case 1:
                fragment = new TabLayOutFavouritesFragment();
                break;
            case 2:
                fragment = new TabLayOutHistoryFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}

