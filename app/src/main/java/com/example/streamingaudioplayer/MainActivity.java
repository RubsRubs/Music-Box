package com.example.streamingaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.streamingaudioplayer.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.buttonAccederID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.editEmailID.getText().toString();
                String passw = binding.editPasswID.getText().toString();

                if (!email.isEmpty() && !passw.isEmpty()) {
                    login(email, passw);
                } else {
                    Toast.makeText(MainActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.editRegistrarseID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login(String email, String passw) {

        binding.loginProgressCircularID.setVisibility(View.VISIBLE);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    binding.loginProgressCircularID.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Fallo al iniciar sesión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}