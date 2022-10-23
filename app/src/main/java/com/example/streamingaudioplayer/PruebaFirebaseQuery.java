package com.example.streamingaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.streamingaudioplayer.databinding.ActivityPruebaFirebaseQueryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PruebaFirebaseQuery extends AppCompatActivity {

    ActivityPruebaFirebaseQueryBinding binding;
    RecyclerView recyclerView;
    PruebaFirebaseQueryListViewAdaper pruebaFirebaseQeryListViewAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPruebaFirebaseQueryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar

        recyclerView = binding.recyclerViewPruebaQueryID;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        pruebaFirebaseQeryListViewAdaper = new PruebaFirebaseQueryListViewAdaper(getApplicationContext());
        recyclerView.setAdapter(pruebaFirebaseQeryListViewAdaper);
        loadData();
    }

    private void loadData() {

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {

                    User user = data.getValue(User.class);
                    users.add(user);
                }
                pruebaFirebaseQeryListViewAdaper.setItems(users);
                pruebaFirebaseQeryListViewAdaper.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dbr.addListenerForSingleValueEvent(valueEventListener);


    }
}