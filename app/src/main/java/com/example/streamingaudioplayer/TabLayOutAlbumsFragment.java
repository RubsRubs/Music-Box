package com.example.streamingaudioplayer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.streamingaudioplayer.databinding.FragmentTabLayOutAlbumsBinding;

import java.util.ArrayList;

public class TabLayOutAlbumsFragment extends Fragment {

    FragmentTabLayOutAlbumsBinding binding;
    ListView listView;
    Artist artist;
    ArrayList<Album> albums;
    AlbumsListViewAdapter albumsListViewAdapter;

    public TabLayOutAlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabLayOutAlbumsBinding.inflate(getLayoutInflater());

        artist = (Artist) getArguments().getSerializable("artist");
        albums = artist.getAlbums();
        listView = binding.albumsListViewID;
        albumsListViewAdapter = new AlbumsListViewAdapter(getContext(), 0, albums);
        listView.setAdapter(albumsListViewAdapter);

        return binding.getRoot();
    }

    //ITEMCLICKLISTENER PARA EL ADAPTADOR AlbumsListViewAdapter------------------------------------------------------------------------
    @Override
    public void onStart() {
        super.onStart();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int albumPosition, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("artist", artist);
                bundle.putInt("albumPosition", albumPosition);
                Intent intent = new Intent(getContext(), AlbumActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}