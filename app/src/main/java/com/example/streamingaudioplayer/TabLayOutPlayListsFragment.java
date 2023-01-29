package com.example.streamingaudioplayer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.streamingaudioplayer.databinding.FragmentTabLayOutPlayListsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TabLayOutPlayListsFragment extends Fragment {

    FragmentTabLayOutPlayListsBinding binding;
    RecyclerView recyclerView;
    PlayListsRecyclerViewAdapter playListsRecyclerViewAdapter;
    ArrayList<Playlist> playlists;

    public TabLayOutPlayListsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabLayOutPlayListsBinding.inflate(getLayoutInflater());

        recyclerView = binding.playlistsFragmentRecyclerViewID;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        playlists = new ArrayList<>();
        loadPlaylistsData();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.imgViewAddCircleID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(); //abrimos ventanita para crear la playlist
            }
        });
    }

    public void loadPlaylistsData() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Playlists").orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                playlists.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Playlist playlist = data.getValue(Playlist.class);
                    playlists.add(playlist);
                }
                //importante crear un nuevo adaptador cada vez para que no se duplicquen los items en el RecyclerView
                playListsRecyclerViewAdapter = new PlayListsRecyclerViewAdapter(getContext());
                recyclerView.setAdapter(playListsRecyclerViewAdapter);
                playListsRecyclerViewAdapter.setItems(playlists);
                playListsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void openDialog() {
        PlayListCreationDialogue playListCreationDialogue = new PlayListCreationDialogue();
        playListCreationDialogue.show(getParentFragmentManager(), "Playlist");
    }
}