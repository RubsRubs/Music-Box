package com.example.streamingaudioplayer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.streamingaudioplayer.databinding.FragmentTabLayOutBiographyBinding;
import com.example.streamingaudioplayer.databinding.FragmentTabLayOutFavouritesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TabLayOutFavouritesFragment extends Fragment {

    FragmentTabLayOutFavouritesBinding binding;

    RecyclerView recyclerView;
    FavouritesRecyclerAdapter favouritesRecyclerAdapter;
    ArrayList<String> songIdsList;
    FirebaseAuth firebaseAuth;
    String userId;
    DatabaseReference databaseReference;


    public TabLayOutFavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabLayOutFavouritesBinding.inflate(getLayoutInflater());

        recyclerView = binding.favouritesFragmentRecyclerViewID;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        songIdsList = new ArrayList<>();

        getSongIds();

        return binding.getRoot();
    }


    public void getSongIds() {

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").child(userId).child("favourites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                songIdsList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    SongIDModel songIDModel = data.getValue(SongIDModel.class);
                    songIdsList.add(songIDModel.getSongId());
                }
                refreshAndCreateNewRecyclerViewAdapater();
                setRecyclerAdapter(songIdsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void refreshAndCreateNewRecyclerViewAdapater() {
        favouritesRecyclerAdapter = new FavouritesRecyclerAdapter(getContext());
        recyclerView.setAdapter(favouritesRecyclerAdapter);

    }

    public void setRecyclerAdapter(ArrayList<String> songIdsList) {
        favouritesRecyclerAdapter.setItems(songIdsList);
        favouritesRecyclerAdapter.notifyDataSetChanged();

    }
}