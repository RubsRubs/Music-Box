package com.example.streamingaudioplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlayListDetailsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<String> songIdsList = new ArrayList<>();
    Playlist playlist;

    public PlayListDetailsRecyclerAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setItems(ArrayList<String> songIdsList, Playlist playlist) {
        this.songIdsList.addAll(songIdsList);
        this.playlist = playlist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.miscellaneous_song_item, parent, false);
        return new MiscellaneousAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        MiscellaneousAdapterHolder miscellaneousAdapterHolder = (MiscellaneousAdapterHolder) holder;
        String songId = songIdsList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Artists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Artist artist = dataSnapshot.getValue(Artist.class);

                    ArrayList<Album> albums = artist.getAlbums();

                    for (Album album : albums) {

                        ArrayList<Song> songs = album.getSongs();

                        for (Song song : songs) {

                            for (int i = 0; i < songIdsList.size(); i++) {

                                if (Double.toString(song.getSongId()).equals(songId)) {
                                    Glide.with(miscellaneousAdapterHolder.itemView).load(album.getImgURL()).fitCenter().into(miscellaneousAdapterHolder.imageViewPicture);
                                    miscellaneousAdapterHolder.txtArtist.setText(artist.getName());
                                    miscellaneousAdapterHolder.txtSong.setText(song.getSongTitle());
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

        ((MiscellaneousAdapterHolder) holder).imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.playlist_details_item_pop_up_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener((menuItem) -> {

                    switch (menuItem.getItemId()) {

                        case R.id.agregar_a_favoritos_desde_playlist_detail_ID: {
                            addToFavourites(songId);
                            break;
                        }

                        case R.id.eliminar_de_playlist_ID: {
                            delete(songId);
                        }
                    }
                    return true;
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("songIdPosition", position);
                bundle.putStringArrayList("songIdsList", songIdsList);
                Intent intent = new Intent(context, PlayerActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// addFlags para que no me de error al pasar a la nueva activity
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songIdsList.size();
    }

    public void delete(String songId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Playlists").orderByChild("title").equalTo(playlist.getTitle()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                songIdsList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {

                    DataSnapshot dataSnapshot = data.child("songs");

                    for (DataSnapshot data2 : dataSnapshot.getChildren()) {
                        SongIDModel songIDModel = data2.getValue(SongIDModel.class);

                        if (songIDModel.getSongId().equals(songId)) {
                            data2.getRef().removeValue();
                            Toast.makeText(context.getApplicationContext(), "Canción eliminada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addToFavourites(String songId) {

        SongIDModel songIdModel = new SongIDModel(songId);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(); //este objeto hace referencia al nodo principal de la bd en real-time
        String userId = auth.getCurrentUser().getUid(); //obtenemos el id del usuario recién registrado para después utilizarlo para generar su mapa de valores correspondiente.

        //String favouritesKey = databaseReference.child("Users").child("favourites").push().getKey();

        databaseReference.child("Users").child(userId).child("favourites").push().setValue(songIdModel).addOnCompleteListener(new OnCompleteListener<Void>() { //.child crea un nuevo nodo
            @Override
            public void onComplete(@NonNull Task<Void> task1) {
                Toast.makeText(context.getApplicationContext(), "Canción agregada a favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
