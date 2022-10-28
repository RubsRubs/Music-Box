package com.example.streamingaudioplayer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MiscellaneousAdapterHolder extends RecyclerView.ViewHolder {

    public TextView txtArtist, txtSong;
    public ImageView imageViewPicture, imageViewMenu;

    public MiscellaneousAdapterHolder(@NonNull View itemView) {
        super(itemView);
        txtArtist = itemView.findViewById(R.id.txtV_artistID);
        txtSong = itemView.findViewById(R.id.txtV_songID);
        imageViewPicture = itemView.findViewById(R.id.song_miscellanous_item_imageID);
        imageViewMenu = itemView.findViewById(R.id.item_menu_ID);
    }
}
