package com.example.streamingaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.streamingaudioplayer.databinding.ActivityNavigationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NavigationActivity extends AppCompatActivity implements PlayListCreationDialogue.PlayListAddDialogueListener  {

    ActivityNavigationBinding binding;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar;

        //cambiamos el color de la status bar
        Window window = NavigationActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(NavigationActivity.this, R.color.black));

        bottomNavigationView = binding.bottomNavigationID;

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_id, new HomeFragment()).commit(); //para determinar que fragment se ve por defecto al iniciar la activity

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragmentSeleccionado = null;

                switch (item.getItemId()) {

                    case R.id.inicio_ID:
                        fragmentSeleccionado = new HomeFragment();
                        break;
                    case R.id.musica_ID:
                        fragmentSeleccionado = new LibraryFragment();
                        break;
                    case R.id.perfil_ID:
                        fragmentSeleccionado = new UserFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_id, fragmentSeleccionado).commit();
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //NAVEGACIÓN ENTRE FRAGMENTS DENTRO DE LAS MISMA ACTIVITY...
       /* FragmentTransaction fragmentTransaction;
        Fragment artistFragment = new ArtistsFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_id, artistFragment);
        fragmentTransaction.addToBackStack(null); // para evitar que al dar al botón "atrás" se salga de la aplicación en vez de volver al fragment anterior
        fragmentTransaction.commit();*/

        binding.searchIconID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void applyTexts(String title, String description, boolean publica) {
        addPlaylist(title, description, publica);
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

