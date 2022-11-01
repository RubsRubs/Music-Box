package com.example.streamingaudioplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddToPlayListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<Playlist> list = new ArrayList<>();
    Bundle bundle;

    public AddToPlayListRecyclerAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setItems(ArrayList<Playlist> playlists, Bundle bundle) {
        this.list.addAll(playlists);
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_to_play_list_recycler_item, parent, false);
        return new AddToPlayListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        AddToPlayListAdapterViewHolder addToPlayListAdapterViewHolder = (AddToPlayListAdapterViewHolder) holder;
        Playlist playlist = list.get(position);

        addToPlayListAdapterViewHolder.titleTextView.setText(playlist.getTitle());
        addToPlayListAdapterViewHolder.descriptionTextView.setText(playlist.getDescription());
        ((AddToPlayListAdapterViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSongToPlayList();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addSongToPlayList() {

        ArrayList<String> songIds = new ArrayList<>();
        songIds.add(bundle.getString("songId"));
        Playlist playlist = new Playlist(songIds);

      /*  Map<String, Object> map = new HashMap<>(); //mapa de valores
        map.put("email", email);
        map.put("user", user);*/

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(userId).child("playlists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Playlist playlistAux = data.getValue(Playlist.class);

                    if (playlistAux.getTitle().equals(playlist.getTitle())) {
                        databaseReference.push().setValue(playlist);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}