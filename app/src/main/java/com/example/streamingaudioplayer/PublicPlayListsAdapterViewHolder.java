package com.example.streamingaudioplayer;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PublicPlayListsAdapterViewHolder extends RecyclerView.ViewHolder {

    public TextView titleTextView, descriptionTextView;

    public PublicPlayListsAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.txtV_public_playlist_title_ID);
        descriptionTextView = itemView.findViewById(R.id.txtV_public_playlist_description_ID);
    }
}