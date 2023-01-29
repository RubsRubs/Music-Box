package com.example.streamingaudioplayer;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.streamingaudioplayer.databinding.FragmentTabLayOutHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class TabLayOutHistoryFragment extends Fragment {

    FragmentTabLayOutHistoryBinding binding;

    RecyclerView recyclerView;
    HistoryRecyclerAdapter historyRecyclerAdapter;
    ArrayList<String> songIdsList;
    FirebaseAuth firebaseAuth;
    String userId;
    DatabaseReference databaseReference;

    public TabLayOutHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabLayOutHistoryBinding.inflate(getLayoutInflater());

        recyclerView = binding.historyFragmentRecyclerViewID;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        songIdsList = new ArrayList<>();

        getSongIds();

        return binding.getRoot();
    }

    public void getSongIds() {

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").child(userId).child("history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                songIdsList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    SongIDModel songIDModel = data.getValue(SongIDModel.class);
                    songIdsList.add(songIDModel.getSongId());
                }
                refreshAndCreateNewRecyclerViewAdapater();
                setRecyclerAdapter(songIdsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void refreshAndCreateNewRecyclerViewAdapater() {
        historyRecyclerAdapter = new HistoryRecyclerAdapter(getContext());
        recyclerView.setAdapter(historyRecyclerAdapter);

    }

    public void setRecyclerAdapter(ArrayList<String> songIdsList) {
        historyRecyclerAdapter.setItems(songIdsList);
        historyRecyclerAdapter.notifyDataSetChanged();

    }
}