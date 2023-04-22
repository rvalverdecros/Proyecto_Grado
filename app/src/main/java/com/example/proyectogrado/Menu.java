package com.example.proyectogrado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    TextView mipuntuaciontxt,uid,correo,nombre,menutxt;
    Button jugarbtn,puntuacionesbtn,acercadebtn,cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_menu);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        mipuntuaciontxt = findViewById(R.id.mipuntuaciontxt);
        uid = findViewById(R.id.uid);
        correo = findViewById(R.id.correo);
        nombre = findViewById(R.id.nombre);
        menutxt = findViewById(R.id.menutxt);

        jugarbtn = findViewById(R.id.jugarbtn);
        puntuacionesbtn = findViewById(R.id.puntuacionesbtn);
        acercadebtn = findViewById(R.id.acercadebtn);
        cerrarSesion = findViewById(R.id.cerrarSesionbtn);

        jugarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "JUGAR", Toast.LENGTH_SHORT).show();
            }
        });

        puntuacionesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "PUNTUACIONES", Toast.LENGTH_SHORT).show();
            }
        });

        acercadebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "ACERCA DE", Toast.LENGTH_SHORT).show();
            }
        });
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