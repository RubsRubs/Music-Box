package com.example.streamingaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.streamingaudioplayer.databinding.ActivityPlayerBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    ActivityPlayerBinding binding;
    ArrayList<String> songIdsList;
    int songIdPosition;
    String songId;
    static MediaPlayer mediaPlayer; //importante ponerlo como estático para que al retroceder a la activity anterior mientras está sonando una canción y elegir una nueva el objeto siga siendo !=null
    private Handler handler = new Handler();
    Artist actualArtist;
    Album actualAlbum;
    Song actualSong;
    boolean favourited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar

        //cambiamos el color de la status bar
        Window window = PlayerActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(PlayerActivity.this, R.color.black));

        Bundle bundle = getIntent().getExtras();
        songIdsList = bundle.getStringArrayList("songIdsList");
        songIdPosition = bundle.getInt("songIdPosition");
        songId = songIdsList.get(songIdPosition);

        favourited = false;
        retreiveDataFromDataBase();
    }

    private void retreiveDataFromDataBase() {

        binding.playerProgressCircularID.setVisibility(View.VISIBLE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Artists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Artist artist = dataSnapshot.getValue(Artist.class);

                    ArrayList<Album> albums = artist.getAlbums();

                    for (Album album : albums) {

                        ArrayList<Song> songs = album.getSongs();

                        for (Song song : songs) {

                            for (int i = 0; i < songIdsList.size(); i++) {

                                if (Double.toString(song.getSongId()).equals(songId)) {
                                    actualArtist = artist;
                                    actualAlbum = album;
                                    actualSong = song;
                                }
                            }
                        }
                    }
                }
                loadLayOutData();
                binding.playerProgressCircularID.setVisibility(View.VISIBLE);
                View view = binding.getRoot();
                play(view);
                addToHistory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadLayOutData() {
        Glide.with(getApplicationContext()).load(actualAlbum.getImgURL()).fitCenter().into(binding.imgViewCoverPicID);
        binding.txtVArtistaID.setText(actualArtist.getName());
        binding.txtVSongID.setText(actualSong.getSongTitle());
        binding.txtVTotalTimeID.setText(actualSong.getDuration());
        checkIfFavourited();
    }

    private void play(View view) {

        if (mediaPlayer == null) { //si no hay nada reproduciéndose...
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(actualSong.getFileURL());
                mediaPlayer.prepare();
                mediaPlayer.start();
                binding.playPauseButtonID.setImageResource(R.drawable.ic_baseline_pause);
                progresoSeekBar();

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        next(view);
                    }
                });
            } catch (IOException e) {
                Toast.makeText(PlayerActivity.this, "Error de conexión al servidor", Toast.LENGTH_SHORT).show();
            }
        } else { //si hay una canción reproduciéndose...
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(actualSong.getFileURL());
                mediaPlayer.prepare();
                mediaPlayer.start();
                binding.playPauseButtonID.setImageResource(R.drawable.ic_baseline_pause);
                progresoSeekBar();

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        next(view);
                    }
                });
            } catch (IOException e) {
                Toast.makeText(PlayerActivity.this, "Error de conexión al servidor", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.playPauseButtonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause(view);
            }
        });

        binding.nextButtonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next(view);
            }
        });

        binding.previousButtonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous(view);
            }
        });

        binding.txtVArtistaID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("artist", actualArtist);
                Intent intent = new Intent(getApplicationContext(), ArtistActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// addFlags para que no me de error al pasar a la nueva activity
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.txtVArtistaID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("artist", actualArtist);
                Intent intent = new Intent(getApplicationContext(), ArtistActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.imgVFavouritedID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!favourited) {
                    addToFavourites();
                    binding.imgVFavouritedID.setImageResource(R.drawable.ic_baseline_favorite_on);
                } else {
                    deleteFromFavourites(Double.toString(actualSong.getSongId()));
                    binding.imgVFavouritedID.setImageResource(R.drawable.ic_baseline_favorite_off);
                }
            }
        });
    }

    public void pause(View view) {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            binding.playPauseButtonID.setImageResource(R.drawable.ic_baseline_play_arrow);

        } else {
            mediaPlayer.start();
            binding.playPauseButtonID.setImageResource(R.drawable.ic_baseline_pause);
        }
    }

    public void next(View view) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null; //importante poner el objeto a null para resetear el espacio en memoria y que vuelva a reproducir desde cero

        if (songIdPosition == songIdsList.size() - 1) { //si estamos en la última canción de la lista, al darle a next se reproducirá la primera canción de la lista...
            songIdPosition = 0;
            Toast.makeText(PlayerActivity.this, "Fin de la lista de reproducción", Toast.LENGTH_LONG).show();
        } else {
            songIdPosition = songIdPosition + 1;
        }
        songId = songIdsList.get(songIdPosition);
        retreiveDataFromDataBase();
    }

    public void previous(View view) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null; //importante poner el objeto a null para resetear el espacio en memoria y que vuelva a reproducir desde cero

        if (songIdPosition == 0) {//si estamos en la primera canción de la lista al darle a previous se reproducirá la última canción de la lista...
            songIdPosition = songIdsList.size() - 1;
        } else {
            songIdPosition = songIdPosition - 1;
        }
        songId = songIdsList.get(songIdPosition);
        retreiveDataFromDataBase();
    }

    public void progresoSeekBar() {

        binding.seekbarID.setMax(mediaPlayer.getDuration() / 1000);
        binding.seekbarID.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int posicionActual = mediaPlayer.getCurrentPosition() / 1000;
                    binding.seekbarID.setProgress(posicionActual);
                    binding.txtTiempoTranscurridoID.setText(formatearTiempo(posicionActual));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private String formatearTiempo(int posicionActual) {

        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(posicionActual % 60);
        String minutes = String.valueOf(posicionActual / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;

        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    private void addToHistory() {

        String idNumber = Double.toString(actualSong.getSongId());
        SongIDModel songIDModel = new SongIDModel(idNumber);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(); //este objeto hace referencia al nodo principal de la bd en real-time
        String userId = auth.getCurrentUser().getUid(); //obtenemos el id del usuario recién registrado para después utilizarlo para generar su mapa de valores correspondiente.

        //String favouritesKey = databaseReference.child("Users").child("favourites").push().getKey();

        databaseReference.child("Users").child(userId).child("history").push().setValue(songIDModel).addOnCompleteListener(new OnCompleteListener<Void>() { //.child crea un nuevo nodo
            @Override
            public void onComplete(@NonNull Task<Void> task1) {

            }
        });
    }

    public void checkIfFavourited() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(); //este objeto hace referencia al nodo principal de la bd en real-time
        String userId = auth.getCurrentUser().getUid();

        databaseReference.child("Users").child(userId).child("favourites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    SongIDModel songIDModel = data.getValue(SongIDModel.class);

                    if (Double.toString(actualSong.getSongId()).equals(songIDModel.getSongId())) {
                        binding.imgVFavouritedID.setImageResource(R.drawable.ic_baseline_favorite_on);
                        favourited = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addToFavourites() {

        String songId = Double.toString(actualSong.getSongId());
        SongIDModel songIdModel = new SongIDModel(songId);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(); //este objeto hace referencia al nodo principal de la bd en real-time
        String userId = auth.getCurrentUser().getUid(); //obtenemos el id del usuario recién registrado para después utilizarlo para generar su mapa de valores correspondiente.

        //String favouritesKey = databaseReference.child("Users").child("favourites").push().getKey();

        databaseReference.child("Users").child(userId).child("favourites").push().setValue(songIdModel).addOnCompleteListener(new OnCompleteListener<Void>() { //.child crea un nuevo nodo
            @Override
            public void onComplete(@NonNull Task<Void> task1) {
                Toast.makeText(getApplicationContext(), "Canción agregada a favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteFromFavourites(String songId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();

        Query delete = databaseReference.child("Users").child(userId).child("favourites").orderByChild("songId").equalTo(songId);

        delete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                songIdsList.clear(); // importante limpiar la lista cada vez que se elimina un item para que no se dupliquen en la parte de abajo...
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    data.getRef().removeValue();
                    Toast.makeText(getApplicationContext(), "Canción eliminada de favoritos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
