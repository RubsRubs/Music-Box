package com.example.streamingaudioplayer;

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

import com.example.streamingaudioplayer.databinding.ActivityPlayListDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlayListDetailsActivity extends AppCompatActivity {

    ActivityPlayListDetailsBinding binding;

    RecyclerView recyclerView;
    PlayListDetailsRecyclerAdapter playListDetailsRecyclerAdapter;
    ArrayList<String> songIdsList;
    FirebaseAuth firebaseAuth;
    String userId;
    DatabaseReference databaseReference;
    Bundle bundle;
    String playListTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlayListDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar

        bundle = getIntent().getExtras();
        playListTitle = bundle.getString("playListTitle");

        //cambiamos el color de la status bar
        Window window = PlayListDetailsActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(PlayListDetailsActivity.this, R.color.black));

        recyclerView = binding.playListsDetailsRecyclerViewID;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        songIdsList = new ArrayList<>();

        getSongIds();
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

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(userId).child("playlists").orderByChild("title").equalTo(playListTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                songIdsList.clear();
                for (DataSnapshot data : snapshot.child("songs").getChildren()) {
                    SongIDModel songIDModel = data.getValue(SongIDModel.class);
                    songIdsList.add(songIDModel.getSongId());
                }
                refreshAndCreateNewRecyclerViewAdapater();
                setRecyclerAdapter(songIdsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void refreshAndCreateNewRecyclerViewAdapater() {
        playListDetailsRecyclerAdapter = new PlayListDetailsRecyclerAdapter(getApplicationContext());
        recyclerView.setAdapter(playListDetailsRecyclerAdapter);
    }

    public void setRecyclerAdapter(ArrayList<String> songIdsList) {
        playListDetailsRecyclerAdapter.setItems(songIdsList);
        playListDetailsRecyclerAdapter.notifyDataSetChanged();
    }
}
