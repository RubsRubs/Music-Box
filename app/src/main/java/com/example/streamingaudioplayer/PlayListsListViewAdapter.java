package com.example.streamingaudioplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class PlayListsListViewAdapter extends ArrayAdapter {

    public PlayListsListViewAdapter(Context context, int resource, ArrayList<String> playLists) {
        super(context, resource, playLists);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        String playListName = (String) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.playlists_listview_item, parent, false);
        }

        TextView playListTextView = convertView.findViewById(R.id.txt_view_playlist_name_ID);
        playListTextView.setText(playListName);

        return convertView;
    }
}
