package com.example.streamingaudioplayer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private String[] titles = new String[]{"Álbumes", "Biografía"};
    Artist artist;

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity, Artist artist) {
        super(fragmentActivity);
        this.artist = artist;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new TabLayOutAlbumsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("artist", artist);
                fragment.setArguments(bundle);//pasamos el bundle con el objeto artist al fragment
                break;
            case 1:
                fragment = new TabLayOutBiographyFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}