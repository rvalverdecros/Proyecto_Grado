package com.example.proyectogrado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int duracion_Splash = 3000; // Son 3 segundos pero en milisegundos


        //HANDLER, ejecuta lineas de codigo en un tiempo que ponemos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Codigo que se ejecuta
                Intent intent = new Intent(Splash.this, Menu.class);
                startActivity(intent);
            };
        }, duracion_Splash);
    }
}