package com.example.streamingaudioplayer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PruebaQueryAdapterViewHolder extends RecyclerView.ViewHolder {

    public TextView pruebaText;
    public ImageView imageViewPicture;

    public PruebaQueryAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        pruebaText = itemView.findViewById(R.id.txt_view_prueba_query_ID);
        imageViewPicture = itemView.findViewById(R.id.item_prueba_imageID);
    }
}
