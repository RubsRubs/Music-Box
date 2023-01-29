package com.example.streamingaudioplayer;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.streamingaudioplayer.databinding.FragmentUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;

public class UserFragment extends Fragment {

    FragmentUserBinding binding;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String id;
    StorageReference storageReference;
    Uri imageUri;

    private final int GALLERY_REQ_CODE = 1000;

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

        databaseReference.child("Users").orderByKey().equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
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

        //si pongo getContext() en vez de getActivity me da un error
        if (user.profileImgURL != null && getActivity() != null) {
            Glide.with(getActivity()).load(user.getProfileImgURL()).fitCenter().into(binding.imgvAvatarID);
        }
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

        binding.txtVLogOutID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Sesión finalizada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        binding.imgvAvatarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //para abrir la cámara en vez de la galería: new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //openGallery.launch(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQ_CODE);
            }
        });
    }

    public void openDialog() {
        EditProfileNameDialogue editProfileNameDialogue = new EditProfileNameDialogue();
        editProfileNameDialogue.show(getParentFragmentManager(), "UserName");
    }

    /*ActivityResultLauncher<Intent> openGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Bundle bundle = result.getData().getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                binding.imgvCameraID.setImageBitmap(bitmap);
            }
        }
    });*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == GALLERY_REQ_CODE) {
                imageUri = data.getData();
                binding.imgvAvatarID.setImageURI(imageUri);
                uploadImagetoStorage();
            }
        }
    }

    public void uploadImagetoStorage() {

        storageReference = FirebaseStorage.getInstance().getReference("user_profiles_pictures/" + firebaseAuth.getCurrentUser().getEmail());
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadImageUrlToRealTimeDataBase();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadImageUrlToRealTimeDataBase() {

        databaseReference.child("Users").child(id).child("profileImgURL").setValue(imageUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() { //.child crea un nuevo nodo
            @Override
            public void onComplete(@NonNull Task<Void> task1) {
                Toast.makeText(getContext(), "Imagen actualizada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}