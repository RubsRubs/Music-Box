package com.example.streamingaudioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.streamingaudioplayer.databinding.ActivityPlayListDetailsBinding;
import com.example.streamingaudioplayer.databinding.ActivityPublicPlaylistDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PublicPlaylistDetailsActivity extends AppCompatActivity {

    ActivityPublicPlaylistDetailsBinding binding;

    RecyclerView recyclerView;
    PublicPlaylistDetailsRecyclerAdapter publicPlaylistDetailsRecyclerAdapter;
    ArrayList<String> songIdsList;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Bundle bundle;
    Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPublicPlaylistDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar

        bundle = getIntent().getExtras();
        playlist = (Playlist) bundle.getSerializable("playList");

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //cambiamos el color de la status bar
        Window window = PublicPlaylistDetailsActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(PublicPlaylistDetailsActivity.this, R.color.black));

        recyclerView = binding.publicPlayListsDetailsRecyclerViewID;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        songIdsList = new ArrayList<>();

        getSongIds();
        loadLayOutData();
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.imagvBackID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getSongIds() {

        databaseReference.child("Playlists").orderByChild("title").equalTo(playlist.getTitle()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                songIdsList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {

                    DataSnapshot dataSnapshot = data.child("songs");

                    for (DataSnapshot data2 : dataSnapshot.getChildren()) {

                        SongIDModel songIDModel = data2.getValue(SongIDModel.class);
                        songIdsList.add(songIDModel.getSongId());
                    }

                    refreshAndCreateNewRecyclerViewAdapater();
                    setRecyclerAdapter(songIdsList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void refreshAndCreateNewRecyclerViewAdapater() {
        publicPlaylistDetailsRecyclerAdapter = new PublicPlaylistDetailsRecyclerAdapter(getApplicationContext());
        recyclerView.setAdapter(publicPlaylistDetailsRecyclerAdapter);
    }

    public void setRecyclerAdapter(ArrayList<String> songIdsList) {
        publicPlaylistDetailsRecyclerAdapter.setItems(songIdsList, playlist);
        publicPlaylistDetailsRecyclerAdapter.notifyDataSetChanged();
    }

    public void loadLayOutData() {
        binding.publicPlaylistDetailsPlaylistTitleTxtViewID.setText(playlist.getTitle());
        binding.publicPlaylistDetailsPlaylistDescriptionTxtViewID.setText(playlist.getDescription());

        databaseReference.child("Users").orderByKey().equalTo(playlist.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    binding.publicPlaylistDetailsPlaylistUploadedByTxtViewID.setText(user.getUser());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
