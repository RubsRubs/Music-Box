package com.example.streamingaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.streamingaudioplayer.databinding.ActivityPlayListsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlayListsActivity extends AppCompatActivity implements PlayListAddDialogue.PlayListAddDialogueListener {

    ActivityPlayListsBinding binding;
    RecyclerView recyclerView;
    PlayListsRecyclerViewAdapter playListsRecyclerViewAdapter;
    ArrayList<Playlist> playlists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlayListsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar

        recyclerView = binding.playlistsRecyclerViewID;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        playListsRecyclerViewAdapter = new PlayListsRecyclerViewAdapter(getApplicationContext());
        recyclerView.setAdapter(playListsRecyclerViewAdapter);
        loadPlaylistsData();

        //cambiamos el color de la status bar
        Window window = PlayListsActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(PlayListsActivity.this, R.color.black));
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.imgViewAddCircleID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(); //abrimos ventanita para crear la playlist
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

                playlists = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Playlist playlist = data.getValue(Playlist.class);
                    playlists.add(playlist);
                }
                playListsRecyclerViewAdapter.setItems(playlists);
                playListsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void openDialog() {
        PlayListAddDialogue playListAddDialogue = new PlayListAddDialogue();
        playListAddDialogue.show(getSupportFragmentManager(), "Playlist");
    }

    @Override
    public void applyTexts(String title, String description, boolean publica) {
        addPlaylist(title, description, publica);
        //Toast.makeText(PlayListsActivity.this, title + description, Toast.LENGTH_SHORT).show();
    }

    public void addPlaylist(String title, String description, boolean publica) {

        Playlist playlist = new Playlist(title, description, publica);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(); //este objeto hace referencia al nodo principal de la bd en real-time
        String userId = auth.getCurrentUser().getUid(); //obtenemos el id del usuario recién registrado para después utilizarlo para generar su mapa de valores correspondiente.

        //String favouritesKey = databaseReference.child("Users").child("favourites").push().getKey();

        databaseReference.child("Users").child(userId).child("playlists").push().setValue(playlist).addOnCompleteListener(new OnCompleteListener<Void>() { //.child crea un nuevo nodo
            @Override
            public void onComplete(@NonNull Task<Void> task1) {
                Toast.makeText(getApplicationContext(), "Playlist Creada", Toast.LENGTH_SHORT).show();

            }
        });
    }
}