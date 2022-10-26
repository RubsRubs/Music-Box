package com.example.streamingaudioplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<String> songKeysList = new ArrayList<>();

    public HistoryRecyclerAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setItems(ArrayList<String> songKeysList) {
        this.songKeysList.addAll(songKeysList);
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
                popupMenu.getMenuInflater().inflate(R.menu.history_item_pop_up_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener((menuItem) -> {

                    switch (menuItem.getItemId()) {

                        case R.id.agregar_a_favoritos_desde_historyID: {

                            break;
                        }

                        case R.id.agregar_a_playlist_desde_history_ID: {

                            //break;
                        }

                        case R.id.eliminar_de_historial_ID: {
                            delete(songKey);
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

    public void delete(String songKey) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child("Users").child(userId).child("history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    SongIDModel songIDModel = data.getValue(SongIDModel.class);

                    if (songIDModel.getSongId().equals(songKey)) {
                        data.getRef().removeValue();
                        songKeysList.clear(); //limpiamos el arraylist para que se vuelva refrescar el recyclerview
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
