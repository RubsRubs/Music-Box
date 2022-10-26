package com.example.streamingaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    ActivityPlayerBinding binding;
    ArrayList<String> songKeysList;
    int songKeyPosition;
    String songKey;
    static MediaPlayer mediaPlayer; //importante ponerlo como estático para que al retroceder a la activity anterior mientras está sonando una canción y elegir una nueva el objeto siga siendo !=null
    private Handler handler = new Handler();
    Artist actualArtist;
    Album actualAlbum;
    Song actualSong;

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
        songKeysList = bundle.getStringArrayList("songKeysList");
        songKeyPosition = bundle.getInt("songKeyPosition");
        songKey = songKeysList.get(songKeyPosition);

        retreiveDataFromDataBase();
    }

    private void retreiveDataFromDataBase() {

        binding.playerProgressCircularID.setVisibility(View.VISIBLE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("artists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Artist artist = dataSnapshot.getValue(Artist.class);

                    ArrayList<Album> albums = artist.getAlbums();

                    for (Album album : albums) {

                        ArrayList<Song> songs = album.getSongs();

                        for (Song song : songs) {

                            for (int i = 0; i < songKeysList.size(); i++) {

                                if (Double.toString(song.getIdNumber()).equals(songKey)) {
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

        if (songKeyPosition == songKeysList.size() - 1) { //si estamos en la última canción de la lista, al darle a next se reproducirá la primera canción de la lista...
            songKeyPosition = 0;
            Toast.makeText(PlayerActivity.this, "Fin de la lista de reproducción", Toast.LENGTH_LONG).show();
        } else {
            songKeyPosition = songKeyPosition + 1;
        }
        songKey = songKeysList.get(songKeyPosition);
        retreiveDataFromDataBase();
    }

    public void previous(View view) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null; //importante poner el objeto a null para resetear el espacio en memoria y que vuelva a reproducir desde cero

        if (songKeyPosition == 0) {//si estamos en la primera canción de la lista al darle a previous se reproducirá la última canción de la lista...
            songKeyPosition = songKeysList.size() - 1;
        } else {
            songKeyPosition = songKeyPosition - 1;
        }
        songKey = songKeysList.get(songKeyPosition);
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

        String idNumber = Double.toString(actualSong.getIdNumber());
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
}