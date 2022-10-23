package com.example.streamingaudioplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FavouritesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<String> songKeysList = new ArrayList<>();

    public FavouritesRecyclerAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setItems(ArrayList<String> songKeysList) {
        this.songKeysList.addAll(songKeysList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_favourites_item, parent, false);
        return new FavouritesAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        FavouritesAdapterHolder favouritesAdapterHolder = (FavouritesAdapterHolder) holder;
        String songKey = songKeysList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("artists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Artist artist = dataSnapshot.getValue(Artist.class);

                    ArrayList<Album> albums = artist.getAlbums();

                    for (Album album : albums) {

                        ArrayList<Song> songs = album.getSongs();

                        for (Song song : songs) {

                            for (int i = 0; i < songKeysList.size(); i++) {

                                if (Double.toString(song.getIdNumber()).equals(songKey)) {
                                    Glide.with(favouritesAdapterHolder.itemView).load(album.getImgURL()).fitCenter().into(favouritesAdapterHolder.imageViewPicture);
                                    favouritesAdapterHolder.txtArtist.setText(artist.getName());
                                    favouritesAdapterHolder.txtSong.setText(song.getSongTitle());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("songKeyPosition", position);
                bundle.putStringArrayList("songKeysList", songKeysList);
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songKeysList.size();
    }
}