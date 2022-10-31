package com.example.streamingaudioplayer;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.streamingaudioplayer.databinding.FragmentLibraryBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LibraryFragment extends Fragment {

    FragmentLibraryBinding binding;

    private String[] titles = new String[]{"Listas", "Favoritos", "Historial"};

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(getLayoutInflater());

        viewPager();

        return binding.getRoot();
    }

    public void viewPager() {
        ViewPager2 viewPager2 = binding.libraryFragmentViewPagerID;
        TabLayout tabLayout = binding.libraryFragmentTabLayOutID;
        ViewPagerFramentAdapterLibrary viewPagerFramentAdapterLibrary = new ViewPagerFramentAdapterLibrary(this);
        viewPager2.setAdapter(viewPagerFramentAdapterLibrary);
        new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> tab.setText(titles[position]))).attach();
    }
}