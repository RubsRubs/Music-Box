package com.example.streamingaudioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.streamingaudioplayer.databinding.ActivityAlbumBinding;
import com.example.streamingaudioplayer.databinding.ActivityJsoncrudBinding;

import java.util.ArrayList;

public class jSONCRUD extends AppCompatActivity {

    ActivityJsoncrudBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJsoncrudBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar


        binding.buttonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Album> albums = new ArrayList<>();

                Artist artist = new Artist(binding.nameID.getText().toString(), albums, binding.artistImageURLID.getText().toString());

            }
        });
    }
}