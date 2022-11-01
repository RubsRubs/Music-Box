package com.example.streamingaudioplayer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddToPlayListAdapterViewHolder extends RecyclerView.ViewHolder {

    public TextView titleTextView, descriptionTextView;

    public AddToPlayListAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.txtV_add_to_play_ist_title_ID);
        descriptionTextView = itemView.findViewById(R.id.txtV_add_to_play_ist_description_ID);
    }
}