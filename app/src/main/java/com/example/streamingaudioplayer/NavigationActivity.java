package com.example.streamingaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.streamingaudioplayer.databinding.ActivityNavigationBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationActivity extends AppCompatActivity {

    ActivityNavigationBinding binding;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar;

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
}

