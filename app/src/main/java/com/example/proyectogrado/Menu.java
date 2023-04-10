package com.example.proyectogrado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    Button cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_menu);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        cerrarSesion = findViewById(R.id.cerrarSesion);

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesion();
            }
        });
    }

    //Este metodo se ejecuta cuado se abre el juego
    @Override
    protected void onStart() {
        UsuarioLogueado();
        super.onStart();
    }

    //Metodo para comprobar si el usuario ha iniciado sesion
    private void UsuarioLogueado(){
        if (user != null){
            Toast.makeText(this, "Jugador en Linea", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(Menu.this, MainActivity.class));
            finish();
        }
    }

    //Metodo para Cerra Sesion
    private void CerrarSesion(){
        auth.signOut();
        startActivity(new Intent(Menu.this, MainActivity.class));
        Toast.makeText(this, "Has Cerrado Sesion Correctamente", Toast.LENGTH_SHORT).show();
    }
}