package com.example.streamingaudioplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PublicPlayListsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<Playlist> list = new ArrayList<>();

    public PublicPlayListsRecyclerAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setItems(ArrayList<Playlist> playlists) {
        this.list.addAll(playlists);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.public_playlists_recycler_item, parent, false);
        return new PublicPlayListsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        PublicPlayListsAdapterViewHolder publicPlayListsAdapterViewHolder = (PublicPlayListsAdapterViewHolder) holder;
        Playlist playlist = list.get(position);

        publicPlayListsAdapterViewHolder.titleTextView.setText(playlist.getTitle());
        publicPlayListsAdapterViewHolder.descriptionTextView.setText(playlist.getDescription());

        ((PublicPlayListsAdapterViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("playList", playlist);
                Intent intent = new Intent(context, PublicPlaylistDetailsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
