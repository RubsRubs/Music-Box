package com.example.streamingaudioplayer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlayListAdapterViewHolder extends RecyclerView.ViewHolder {

    public TextView titleTextView, descriptionTextView;
    public ImageView imageViewMenu;

    public PlayListAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.txtV_playlist_title_ID);
        descriptionTextView = itemView.findViewById(R.id.txtV_playlist_description_ID);
        imageViewMenu = itemView.findViewById(R.id.play_lists_item_menu_ID);
    }
}