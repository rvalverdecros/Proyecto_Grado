package com.example.proyectogrado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText correoLogin, passLogin;
    Button botonLogin;

    FirebaseAuth auth; //La Autenticacion de Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        correoLogin = findViewById(R.id.correoLogin);
        passLogin = findViewById(R.id.passLogin);
        botonLogin = findViewById(R.id.botonLogin);
        auth = FirebaseAuth.getInstance();

        //Al hacer Click en el boton de Login
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = correoLogin.getText().toString();
                String pass = passLogin.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correoLogin.setError("Correo no Valido");
                    correoLogin.setFocusable(true);
                } else if (pass.length()<6) {
                    passLogin.setError("Contraseña debe de ser mayor a 6");
                    passLogin.setFocusable(true);
                }else{
                    LoginJugador(email,pass);
                }
            }
        });
    }

    //Metodo para iniciar sesion del jugador
    private void LoginJugador(String email, String pass) {
        auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(Login.this, Menu.class));
                            assert user != null; //El usuario no es nulo
                            Toast.makeText(Login.this, "BIENVENIDO/A"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    //Si falla el Login
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}