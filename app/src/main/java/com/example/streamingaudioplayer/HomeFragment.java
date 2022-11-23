package com.example.streamingaudioplayer;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.streamingaudioplayer.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    SliderView sliderView1;
    ArrayList<String> songIdsList;
    ArrayList<String> historyIds;
    SliderAdapter sliderAdapter;
    RecyclerView recyclerView;
    PublicPlayListsRecyclerAdapter publicPlayListsRecyclerAdapter;
    ArrayList<Playlist> playlists;
    String genre;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        sliderView1 = binding.sliderViewRecomendacionesID;
        sliderView1.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView1.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView1.setIndicatorSelectedColor(Color.WHITE);
        sliderView1.setIndicatorUnselectedColor(Color.GRAY);
        sliderView1.setScrollTimeInSec(2);
        sliderView1.startAutoCycle();

        recyclerView = binding.comunityPlalistsRecyclerViewID;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        historyIds = new ArrayList<>();
        checkHistory();

        playlists = new ArrayList<>();
        loadPlaylistsData();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadRandomSlider() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("artists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                songIdsList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Artist artist = dataSnapshot.getValue(Artist.class);

                    ArrayList<Album> albums = artist.getAlbums();
                    for (Album album : albums) {

                        ArrayList<Song> songs = album.getSongs();

                        int random = (int) (Math.random() * 3 + 1);
                        songIdsList.add(Double.toString(songs.get(random).getSongId()));
                        sliderAdapter = new SliderAdapter(getContext(), songIdsList); //al utilizar el adaptador con sliderView hay que crear un nuevo objeto adaptador cada vez que se cogen los datos de firebase, ya que al ser un slider se cargan los objetos uno a uno en pantalla, al contrario que el RecyclerView que carga todos a la vez en una lista y con solo decalarar el objeto adaptador una vez y meterle el arraylist como argumento es suficiente.
                        sliderView1.setSliderAdapter(sliderAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadBasedOnHistory() {

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
                            if (Double.toString(song.getSongId()).equals(historyIds.get(historyIds.size() - 1))) {
                                genre = album.getGenre();
                            }
                        }
                    }
                }
                loadRecommendedGenreSlider();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadPlaylistsData() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("playlists").orderByChild("publica").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                playlists.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Playlist playlist = data.getValue(Playlist.class);
                    playlists.add(playlist);
                }
                //importante crear un nuevo adaptador cada vez para que no se duplicquen los items en el RecyclerView
                publicPlayListsRecyclerAdapter = new PublicPlayListsRecyclerAdapter(getContext());
                recyclerView.setAdapter(publicPlayListsRecyclerAdapter);
                publicPlayListsRecyclerAdapter.setItems(playlists);
                publicPlayListsRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void checkHistory() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").child(userId).child("history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    SongIDModel songIDModel = data.getValue(SongIDModel.class);
                    historyIds.add(songIDModel.getSongId());
                }
                //importante llamar a este método dentro del onDataChange para esperar a que acabe de rellenar la lista, de lo contrario la lista estará vacía ya que las respuestas del Firebase van más lentas que la ejecución del código  (asynchronous behavior of the onDataChange(), se ejecuta en segundo plano mientras se ejecuta el resto del código)
                if (historyIds.isEmpty()) {
                    loadRandomSlider();
                } else {
                    loadBasedOnHistory();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void loadRecommendedGenreSlider() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("artists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                songIdsList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Artist artist = dataSnapshot.getValue(Artist.class);

                    ArrayList<Album> albums = artist.getAlbums();
                    for (Album album : albums) {

                        if (album.getGenre().equals(genre)) {

                            ArrayList<Song> songs = album.getSongs();

                            int random = (int) (Math.random() * 3 + 1);
                            songIdsList.add(Double.toString(songs.get(random).getSongId()));
                            sliderAdapter = new SliderAdapter(getContext(), songIdsList); //al utilizar el adaptador con sliderView hay que crear un nuevo objeto adaptador cada vez que se cogen los datos de firebase, ya que al ser un slider se cargan los objetos uno a uno en pantalla, al contrario que el RecyclerView que carga todos a la vez en una lista y con solo decalarar el objeto adaptador una vez y meterle el arraylist como argumento es suficiente.
                            sliderView1.setSliderAdapter(sliderAdapter);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

