package com.example.streamingaudioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.streamingaudioplayer.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide(); //escondemos la action bar

        //cambiamos el color de la status bar
        Window window = MainActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.black));

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC); //utilizamos la versión básica de AwesomeValidation
        awesomeValidation.addValidation(binding.editEmailID, Patterns.EMAIL_ADDRESS, "Formato incorrecto"); //si no se mete una dirección de correo válida salta el error
        awesomeValidation.addValidation(binding.editPasswID, ".{6,}", "Mínimo 6 caracteres"); //si la contraseña no tiene al menos 6 caracteres salta el error
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.buttonAccederID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = binding.editEmailID.getText().toString();
                password = binding.editPasswID.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    login(email, password);
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

    public void login(String email, String password) {

        if (awesomeValidation.validate()) { //si el email y la contraseña tienen los formatos correctos

            binding.loginProgressCircularID.setVisibility(View.VISIBLE);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        binding.loginProgressCircularID.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}