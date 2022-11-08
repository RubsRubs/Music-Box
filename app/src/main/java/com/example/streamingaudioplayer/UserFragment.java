package com.example.streamingaudioplayer;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.example.streamingaudioplayer.databinding.FragmentUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserFragment extends Fragment {

    FragmentUserBinding binding;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String id;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(getLayoutInflater());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();

        retreiveUserData();

        return binding.getRoot();
    }

    public void retreiveUserData() {

        databaseReference.child("Users").orderByChild("email").equalTo(firebaseAuth.getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    loadLayOut(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadLayOut(User user) {
        binding.textVUserNameID.setText(user.getUser());
        binding.textVEmailID.setText(user.getEmail());
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.publicProfileSwitchID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (binding.publicProfileSwitchID.isChecked()) {
                    isChecked = true;
                } else {
                    isChecked = false;
                }
                databaseReference.child("Users").child(id).child("publico").setValue(isChecked).addOnCompleteListener(new OnCompleteListener<Void>() { //.child crea un nuevo nodo
                    @Override
                    public void onComplete(@NonNull Task<Void> task2) {
                    }
                });
            }
        });

        binding.userNamePencilID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    public void openDialog() {
        EditProfileNameDialogue editProfileNameDialogue = new EditProfileNameDialogue();
        editProfileNameDialogue.show(getParentFragmentManager(), "UserName");
    }
}