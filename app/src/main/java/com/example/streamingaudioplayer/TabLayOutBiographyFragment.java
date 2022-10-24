package com.example.streamingaudioplayer;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.streamingaudioplayer.databinding.FragmentTabLayOutBiographyBinding;

public class TabLayOutBiographyFragment extends Fragment {

    FragmentTabLayOutBiographyBinding binding;
    Artist artist;

    public TabLayOutBiographyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabLayOutBiographyBinding.inflate(getLayoutInflater());

        artist = (Artist) getArguments().getSerializable("artist");
        binding.txtvArtistInfoID.setText(artist.getArtistInfo());

        return binding.getRoot();
    }
}