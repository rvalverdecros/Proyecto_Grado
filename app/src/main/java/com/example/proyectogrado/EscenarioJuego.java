package com.example.proyectogrado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class EscenarioJuego extends AppCompatActivity {

    String nombreS, uidS, zombieS;

    TextView tvContador,tvNombre,tvTiempo;

    ImageView ivZombie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);

        ivZombie = findViewById(R.id.ivZombie);

        tvContador= findViewById(R.id.tvContador);
        tvNombre= findViewById(R.id.tvNombre);
        tvTiempo= findViewById(R.id.tvTiempo);

        Bundle intent = getIntent().getExtras();

        uidS = intent.getString("UID");
        nombreS = intent.getString("NOMBRE");
        zombieS = intent.getString("ZOMBIES");

        tvNombre.setText(nombreS);
        tvContador.setText(zombieS);
    }
}