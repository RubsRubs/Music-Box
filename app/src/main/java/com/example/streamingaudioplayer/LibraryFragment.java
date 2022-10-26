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
    FirebaseAuth firebaseAuth;
    String userId;
    DatabaseReference databaseReference;
    boolean favourites;
    boolean history;

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

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.layoutFavoritosID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favourites = true;
                history = false;
                setIconsColors();
                //en este caso tenemos el mismo RecyclerView para dos litas diferentes, cada vez que hacemos click creamos un nuevo FavouritesRecyclerAdapter para refrescar la vista.
                refreshAndCreateNewRecyclerViewAdapater();
                getSongsKeys();
            }
        });

        binding.layoutHistoryID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                history = true;
                favourites = false;
                setIconsColors();
                //en este caso tenemos el mismo RecyclerView para dos litas diferentes, cada vez que hacemos click creamos un nuevo FavouritesRecyclerAdapter para refrescar la vista.
                refreshAndCreateNewRecyclerViewAdapater();
                getSongsKeys();
            }
        });
    }

    public void getSongsKeys() {

        binding.libraryProgressCircularID.setVisibility(View.VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (favourites == true) {

            databaseReference.child("Users").child(userId).child("favourites").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    songKeys = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        SongIDModel songIDModel = data.getValue(SongIDModel.class);
                        songKeys.add(songIDModel.getSongId());
                    }
                    setRecyclerAdapter(songKeys);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        } else {

            binding.historyIconID.setImageResource(R.drawable.ic_baseline_history_on);

            databaseReference.child("Users").child(userId).child("history").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    songKeys = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        SongIDModel songIDModel = data.getValue(SongIDModel.class);
                        songKeys.add(songIDModel.getSongId());
                    }
                    setRecyclerAdapter(songKeys);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    public void refreshAndCreateNewRecyclerViewAdapater() {
        favouritesRecyclerAdapter = new FavouritesRecyclerAdapter(getContext());
        recyclerView.setAdapter(favouritesRecyclerAdapter);
    }

    public void setRecyclerAdapter(ArrayList<String> songKeys) {
        favouritesRecyclerAdapter.setItems(songKeys);
        favouritesRecyclerAdapter.notifyDataSetChanged();
        binding.libraryProgressCircularID.setVisibility(View.INVISIBLE);
    }

    public void setIconsColors() {

        if (favourites == true) {
            binding.favouritesIconID.setImageResource(R.drawable.ic_baseline_favorite_on);
            binding.historyIconID.setImageResource(R.drawable.ic_baseline_history_off);
        } else {
            binding.favouritesIconID.setImageResource(R.drawable.ic_baseline_favorite_off);
            binding.historyIconID.setImageResource(R.drawable.ic_baseline_history_on);
        }
    }
}