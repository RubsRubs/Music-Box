package com.example.streamingaudioplayer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.streamingaudioplayer.databinding.FragmentLibraryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LibraryFragment extends Fragment {

    FragmentLibraryBinding binding;
    RecyclerView recyclerView;
    FavouritesRecyclerAdapter favouritesRecyclerAdapter;
    ArrayList<String> songKeys;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(getLayoutInflater());

        recyclerView = binding.libraryRecyclerViewId;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        favouritesRecyclerAdapter = new FavouritesRecyclerAdapter(getContext());
        recyclerView.setAdapter(favouritesRecyclerAdapter);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.layoutFavoritosID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserFavouritesKeys();
            }
        });
    }

    public void getUserFavouritesKeys() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child("Users").child(userId).child("favourites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                songKeys = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    SongIDModel songIDModel = data.getValue(SongIDModel.class);
                    songKeys.add(songIDModel.getSongId());
                }
                favouritesRecyclerAdapter.setItems(songKeys);
                favouritesRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}