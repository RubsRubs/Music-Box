package com.example.streamingaudioplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.streamingaudioplayer.databinding.ActivityAlbumBinding;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    ActivityAlbumBinding binding;
    Artist artist;
    int albumPosition;
    ListView listView;
    AlbumSongsListViewAdapter albumSongsListViewAdapter;
    ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar

        //cambiamos el color de la status bar
        Window window = AlbumActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(AlbumActivity.this, R.color.black));

        Bundle bundle = getIntent().getExtras();
        artist = (Artist) bundle.getSerializable("artist");
        albumPosition = bundle.getInt("albumPosition");

        binding.albumActivityAlbumNameTextViewID.setText(artist.getAlbums().get(albumPosition).getAlbumTitle());
        binding.albumActivityArtistNameTextViewID.setText(artist.getName());
        Glide.with(getApplicationContext()).load(artist.getAlbums().get(albumPosition).getImgURL()).into(binding.albumActivityImgViewID);

        setUpListView();
    }

    private void setUpListView() {
        songs = artist.getAlbums().get(albumPosition).getSongs();
        albumSongsListViewAdapter = new AlbumSongsListViewAdapter(getApplicationContext(), 0, songs);
        listView = binding.albumActivityListViewID;
        listView.setAdapter(albumSongsListViewAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.albumActivityLayOutsearchIconID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

//ITEMCLICKLISTENER PARA EL ADAPTADOR AlbumSongsListViewAdapter------------------------------------------------------------------------
        binding.albumActivityListViewID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int songPosition, long l) {

                ArrayList<String> songIdsList = generateAlbumSongKeys();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("songIdsList", songIdsList);
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            private ArrayList<String> generateAlbumSongKeys() {

                ArrayList<String> songIdsList = new ArrayList<>();
                for (Song song : songs) {
                    songIdsList.add(Double.toString(song.getSongId()));
                }
                return songIdsList;
            }
        });
    }
}
