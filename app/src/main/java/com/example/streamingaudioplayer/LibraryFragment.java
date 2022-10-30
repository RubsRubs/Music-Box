package com.example.streamingaudioplayer;

import android.content.Intent;
import android.graphics.Paint;
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
    HistoryRecyclerAdapter historyRecyclerAdapter;
    ArrayList<String> songIdsList;
    FirebaseAuth firebaseAuth;
    String userId;
    DatabaseReference databaseReference;
    boolean favourites;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(getLayoutInflater());

        //le metemos el underline al TextView txtVPlaylistsID
        binding.txtVPlaylistsID.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        recyclerView = binding.libraryRecyclerViewId;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        songIdsList = new ArrayList<>();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.layoutPlayListsID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlayListsActivity.class);
                startActivity(intent);
            }
        });

        binding.layoutFavoritosID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favourites = true;
                setIconsColors();
                //en este caso tenemos el mismo RecyclerView para dos litas diferentes, cada vez que hacemos click creamos un nuevo FavouritesRecyclerAdapter para refrescar la vista.
                getSongIds();
            }
        });

        binding.layoutHistoryID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favourites = false;
                setIconsColors();
                //en este caso tenemos el mismo RecyclerView para dos litas diferentes, cada vez que hacemos click creamos un nuevo FavouritesRecyclerAdapter para refrescar la vista.
                getSongIds();
            }
        });
    }

    public void getSongIds() {

        binding.libraryProgressCircularID.setVisibility(View.VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (favourites == true) {

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

        } else {

            binding.historyIconID.setImageResource(R.drawable.ic_baseline_history_on);

            databaseReference.child("Users").child(userId).child("history").addValueEventListener(new ValueEventListener() {
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
    }

    public void refreshAndCreateNewRecyclerViewAdapater() {

        if (favourites == true) {
            favouritesRecyclerAdapter = new FavouritesRecyclerAdapter(getContext());
            recyclerView.setAdapter(favouritesRecyclerAdapter);
        } else {
            historyRecyclerAdapter = new HistoryRecyclerAdapter(getContext());
            recyclerView.setAdapter(historyRecyclerAdapter);
        }
    }

    public void setRecyclerAdapter(ArrayList<String> songKeys) {

        if (favourites == true) {
            favouritesRecyclerAdapter.setItems(songKeys);
            favouritesRecyclerAdapter.notifyDataSetChanged();
        } else {
            historyRecyclerAdapter.setItems(songKeys);
            historyRecyclerAdapter.notifyDataSetChanged();
        }
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