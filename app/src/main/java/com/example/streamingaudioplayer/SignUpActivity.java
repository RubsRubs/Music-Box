package com.example.streamingaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.streamingaudioplayer.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.buttonRegistroID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.editEmailID.getText().toString();
                String user = binding.editUserID.getText().toString();
                String passw = binding.editPassw1ID.getText().toString();
                String passw2 = binding.editPassw2ID.getText().toString();

                if (!user.isEmpty() && !email.isEmpty() && !passw.isEmpty()) {
                    if (passw.length() >= 8) {
                        if (passw.equals(passw2)) {
                            registro(email, user, passw);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "La contraseña debe contener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registro(String email, String user, String passw) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task1) {
                if (task1.isSuccessful()) {
                    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference(); //este objeto hace referencia al nodo principal de la bd en real-time
                    Map<String, Object> map = new HashMap<>(); //mapa de valores
                    map.put("email", email);
                    map.put("user", user);

                    String id = auth.getCurrentUser().getUid(); //obtenemos el id del usuario recién registrado para después utilizarlo para generar su mapa de valores correspondiente.
                    dbr.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() { //.child crea un nuevo nodo
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            Toast.makeText(SignUpActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "Fallo al registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}