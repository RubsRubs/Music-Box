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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NavigationActivity extends AppCompatActivity implements PlayListCreationDialogue.PlayListAddDialogueListener, EditProfileNameDialogue.ChangeProfileNameDialogueListener {

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

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        Playlist playlist = new Playlist(title, description, publica, userId);
        addPlaylist(playlist);
    }

    public void addPlaylist(Playlist playlist) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        //String favouritesKey = databaseReference.child("Users").child("favourites").push().getKey();

        databaseReference.child("Playlists").push().setValue(playlist).addOnCompleteListener(new OnCompleteListener<Void>() { //.child crea un nuevo nodo
            @Override
            public void onComplete(@NonNull Task<Void> task1) {
                Toast.makeText(getApplicationContext(), "Playlist Creada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void changeNameApplyText(String name) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String id = firebaseAuth.getCurrentUser().getUid();

        //String favouritesKey = databaseReference.child("Users").child("favourites").push().getKey();

        databaseReference.child("Users").child(id).child("user").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(NavigationActivity.this, "Nombre de usuario actualizado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

