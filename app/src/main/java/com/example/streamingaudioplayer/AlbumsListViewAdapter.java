package com.example.streamingaudioplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumsListViewAdapter extends ArrayAdapter {

    public AlbumsListViewAdapter(Context context, int resource, ArrayList<Album> albums) {
        super(context, resource, albums);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Album album = (Album) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.album_item, parent, false);
        }
        ImageView albumImgView = convertView.findViewById(R.id.album_img_view_ID);
        TextView albumTxtView = convertView.findViewById(R.id.album_text_view_ID);

        Glide.with(getContext()).load(album.getImgURL()).fitCenter().into(albumImgView);
        albumTxtView.setText(album.getAlbumTitle());

        return convertView;


    }
}

