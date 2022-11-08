package com.example.streamingaudioplayer;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlayListsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<Playlist> list = new ArrayList<>();

    public PlayListsRecyclerViewAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setItems(ArrayList<Playlist> playlists) {
        this.list.addAll(playlists);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlists_listview_item, parent, false);
        return new PlayListsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        PlayListsAdapterViewHolder playListsAdapterViewHolder = (PlayListsAdapterViewHolder) holder;
        Playlist playlist = list.get(position);

        playListsAdapterViewHolder.titleTextView.setText(playlist.getTitle());
        playListsAdapterViewHolder.descriptionTextView.setText(playlist.getDescription());

        ((PlayListsAdapterViewHolder) holder).imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.playlists_item_pop_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener((menuItem) -> {

                    switch (menuItem.getItemId()) {

                        case R.id.eliminar_playlist_ID: {
                            delete(playlist.getTitle());
                        }
                    }
                    return true;
                });
            }
        });

        ((PlayListsAdapterViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("playList", playlist);
                Intent intent = new Intent(context, PlayListDetailsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void delete(String playListTitle) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        //eliminamos por título ya que no puede haber dos iguales
        Query delete = databaseReference.child("playlists").orderByChild("title").equalTo(playListTitle);

        delete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear(); // importante limpiar la lista cada vez que se elimina un item para que no se dupliquen en la parte de abajo...
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    data.getRef().removeValue();
                    Toast.makeText(context.getApplicationContext(), "Playlist eliminada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
