package com.example.streamingaudioplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.streamingaudioplayer.databinding.ActivityArtistBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ArtistActivity extends AppCompatActivity {

    ActivityArtistBinding binding;
    Artist artist;
    private String[] titles = new String[]{"Álbumes", "Biografía"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtistBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar

        //cambiamos el color de la status bar
        Window window = ArtistActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(ArtistActivity.this, R.color.black));

        Bundle bundle = getIntent().getExtras();
        artist = (Artist) bundle.getSerializable("artist");

        binding.txtVArtistActivityNameID.setText(artist.getName());
        Glide.with(getApplicationContext()).load(artist.getArtistImageURL()).fitCenter().into(binding.artistImgViewID);

        viewPager();
    }

    public void viewPager() {
        ViewPager2 viewPager2 = binding.artistViewPagerID;
        TabLayout tabLayout = binding.artistActivityTabLayOutID;
        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(this, artist); //pasamos el objeto artist como parámetro en el constructor
        viewPager2.setAdapter(viewPagerFragmentAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> tab.setText(titles[position]))).attach();
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.artistActivityLayOutsearchIconID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
        startActivity(intent);
    }
}