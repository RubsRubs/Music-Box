package com.example.streamingaudioplayer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchViewAdapterViewHolder extends RecyclerView.ViewHolder {

    public TextView queryText;
    public ImageView imageViewPicture;

    public SearchViewAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        queryText = itemView.findViewById(R.id.txt_view_query_ID);
        imageViewPicture = itemView.findViewById(R.id.item_imageID);
    }
}
