package com.example.streamingaudioplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        return new PlayListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        PlayListAdapterViewHolder playListAdapterViewHolder = (PlayListAdapterViewHolder) holder;
        Playlist playlist = list.get(position);

        playListAdapterViewHolder.titleTextView.setText(playlist.getTitle());
        playListAdapterViewHolder.descriptionTextView.setText(playlist.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
