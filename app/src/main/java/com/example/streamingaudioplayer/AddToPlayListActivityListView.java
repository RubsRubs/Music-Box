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

import com.example.streamingaudioplayer.databinding.ActivityAddToPlayListListViewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddToPlayListActivityListView extends AppCompatActivity {

    ActivityAddToPlayListListViewBinding binding;
    RecyclerView recyclerView;
    ArrayList<Playlist> playlists;
    AddToPlayListRecyclerAdapter addToPlayListRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddToPlayListListViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar

        recyclerView = binding.playListsRecyclerViewID;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        //cambiamos el color de la status bar
        Window window = AddToPlayListActivityListView.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(AddToPlayListActivityListView.this, R.color.black));

        playlists = new ArrayList<>();
        loadPlaylistsData();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.imagvBackID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void loadPlaylistsData() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").child(userId).child("playlists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                playlists.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Playlist playlist = data.getValue(Playlist.class);
                    playlists.add(playlist);
                }

                //importante crear un nuevo adaptador cada vez para que no se duplicquen los items en el RecyclerView
                Bundle bundle = getIntent().getExtras();
                addToPlayListRecyclerAdapter = new AddToPlayListRecyclerAdapter(getApplicationContext());
                recyclerView.setAdapter(addToPlayListRecyclerAdapter);
                addToPlayListRecyclerAdapter.setItems(playlists, bundle);
                addToPlayListRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
