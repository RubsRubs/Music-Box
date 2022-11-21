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
import android.view.inputmethod.EditorInfo;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.streamingaudioplayer.databinding.ActivitySearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements Filterable {

    ActivitySearchBinding binding;
    SearchView searchView;
    SearchViewAdapter searchViewAdapter;
    RecyclerView recyclerView;
    ArrayList<Artist> fileredList;
    int queryCombination;
    int albumPosition;
    int songPosition;
    ArrayList<String> easterEggStrings;
    int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide(); //escondemos la action bar

        //cambiamos el color de la status bar
        Window window = SearchActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(SearchActivity.this, R.color.black));

        searchView = binding.searhViewID;
        searchView.setIconified(false); //desiconificamos la lupa para poder hacer focus en el searchview y poder abrir el teclado automáticamente al iniciar la activity
        searchView.requestFocus();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE); //para cambiar la lupa del teclado por un tick

        recyclerView = binding.searchRecyclerViewID;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        searchViewAdapter = new SearchViewAdapter(getApplicationContext());
        recyclerView.setAdapter(searchViewAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String charSequence) {
                easterEggFilter(charSequence);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String charSequence) {
                getFilter().filter(charSequence);
                return false;
            }
        });
    }

    @Override
    public Filter getFilter() {
        return filtrado; //devuelve el objeto filter que creamos abajo
    }

    private Filter filtrado = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            if (charSequence == null || charSequence.length() == 0) {
                fileredList.clear(); //si no existe ningún patrón limpiamos y vaciamos la lista
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();

                fileredList = new ArrayList<>();

                dbr.child("artists").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot data : snapshot.getChildren()) {

                            Artist artist = data.getValue(Artist.class);

                            if (artist.getName().toLowerCase().contains(filterPattern)) {
                                queryCombination = 1;
                                fileredList.add(artist);
                            } /*else {
                                ArrayList<Album> albums = artist.getAlbums();
                                for (Album album : albums) {
                                    if (album.getAlbumTitle().toLowerCase().contains(filterPattern)) {
                                        albumPosition = albums.indexOf(album);
                                        queryCombination = 2;
                                        fileredList.add(artist);
                                    } else {
                                        ArrayList<Song> songs = album.getSongs();
                                        for (Song song : songs) {
                                            if (song.getSongTitle().toLowerCase().contains(filterPattern)) {
                                                albumPosition = albums.indexOf(album);
                                                songPosition = songs.indexOf(song);
                                                queryCombination = 3;
                                                fileredList.add(artist);
                                            }
                                        }
                                    }
                                }
                            }*/
                        }
                        searchViewAdapter.setItems(fileredList, queryCombination, albumPosition, songPosition);
                        searchViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = fileredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            searchViewAdapter.list.clear(); //limpiamos la lista actual
            searchViewAdapter.list.addAll((ArrayList) filterResults.values);
            searchViewAdapter.notifyDataSetChanged(); //notificamos los cambios realizados
        }
    };

    public void easterEggFilter(String charSequence) {

        if (contador == 0) {
            easterEggStrings = new ArrayList<>();
            easterEggStrings.add("regeton");
            easterEggStrings.add("reggetton");
            easterEggStrings.add("reaggeton");
            easterEggStrings.add("reaggetton");
            easterEggStrings.add("reageton");
            easterEggStrings.add("regueton");
            easterEggStrings.add("reguetton");
            easterEggStrings.add("trap");
            easterEggStrings.add("daddy yankee");
            easterEggStrings.add("dady yanke");
            easterEggStrings.add("dady yankee");
            easterEggStrings.add("dadyy yanke");
            easterEggStrings.add("dady yanki");
            easterEggStrings.add("bad bunny");
            easterEggStrings.add("bad buny");
            easterEggStrings.add("papi chulo");
            easterEggStrings.add("pit bull");
            easterEggStrings.add("don omar");
            easterEggStrings.add("mas gasolina");
            easterEggStrings.add("electro latino");
            easterEggStrings.add("paquirrin");
            easterEggStrings.add("Bertin Osborne");
        }

        for (String string : easterEggStrings) {

            if (charSequence.toLowerCase().trim().contains(string.toLowerCase().trim())) {
                if (contador == 0) {
                    Toast.makeText(SearchActivity.this, "Error, inténtalo de nuevo", Toast.LENGTH_SHORT).show();
                    contador++;

                } else if (contador == 1) {
                    Toast.makeText(SearchActivity.this, "Creo que te has equivocado", Toast.LENGTH_SHORT).show();
                    contador++;

                } else if (contador == 2) {
                    Toast.makeText(SearchActivity.this, "Me refiero a que te has equivocado de aplicación", Toast.LENGTH_SHORT).show();
                    contador++;

                } else if (contador == 3) {
                    Toast.makeText(SearchActivity.this, "¿En serio?, parece que te gusta jugar, ¿no?", Toast.LENGTH_SHORT).show();
                    contador++;

                } else if (contador == 4) {
                    Toast.makeText(SearchActivity.this, "No lo vuelvas a intentar", Toast.LENGTH_SHORT).show();
                    contador++;

                } else if (contador == 5) {
                    Toast.makeText(SearchActivity.this, "Te lo he advertido, adiós", Toast.LENGTH_SHORT).show();
                    contador++;
                    logOut();
                }
            }
        }
    }

    public void logOut() {

        binding.easterEggProgressCircularID.setVisibility(View.VISIBLE);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 3000);
    }
}