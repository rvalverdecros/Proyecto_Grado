package com.example.proyectogrado;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class EscenarioJuego extends AppCompatActivity {

    String nombreS, uidS, zombieS;

    TextView tvContador,tvNombre,tvTiempo;

    ImageView ivZombie;

    TextView anchoTv, altoTV;
    int anchoPantalla;
    int altoPantalla;

    Random aleatorio;

    boolean gameOver = false;
    Dialog miDialog;
    int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);

        ivZombie = findViewById(R.id.ivZombie);

        tvContador= findViewById(R.id.tvContador);
        tvNombre= findViewById(R.id.tvNombre);
        tvTiempo= findViewById(R.id.tvTiempo);


        miDialog = new Dialog(EscenarioJuego.this);
        Bundle intent = getIntent().getExtras();

        uidS = intent.getString("UID");
        nombreS = intent.getString("NOMBRE");
        zombieS = intent.getString("ZOMBIES");

        anchoTv = findViewById(R.id.anchoTv);
        altoTV = findViewById(R.id.altoTv);

        tvNombre.setText(nombreS);
        tvContador.setText(zombieS);

        Pantalla();
        CuentaAtras();

        //Al hacer Click en la Imagen
        ivZombie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gameOver) {
                    contador++; //Aumenta de 1 en 1
                    tvContador.setText(String.valueOf(contador)); //Ponemos el valor del contador en String

                    ivZombie.setImageResource(R.drawable.zombieaplastado);

                    //Nos permite Ejecutar
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Aqui es lo que ejecuta
                            ivZombie.setImageResource(R.drawable.zombie);
                            Movimiento();
                        }
                    }, 500);
                }
            }
        });
    }

    // Para obtener tamaño de la pantalla
    private void Pantalla(){
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        anchoPantalla = point.x;
        altoPantalla = point.y;

        String anchoS = String.valueOf(anchoPantalla);
        String altoS = String.valueOf(altoPantalla);

        anchoTv.setText(anchoS);
        altoTV.setText(altoS);

        aleatorio = new Random();
    }

    private void Movimiento(){

        int min = 0;

        //Maximo de la coordenada X
        int maximoX = anchoPantalla - ivZombie.getWidth();
        //Maximo de la coordenada Y
        int maximoY = altoPantalla - ivZombie.getHeight();

        int randomX = aleatorio.nextInt(((maximoX - min)+ 1)+min);
        int randomY = aleatorio.nextInt(((maximoY - min)+ 1)+min);

        ivZombie.setX(randomX);
        ivZombie.setY(randomY);
    }

    //Metodo para retroceder el tiempo
    private void CuentaAtras(){
        new CountDownTimer(10000,1000){

            //Se ejecuta cada segundo
            @Override
            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished/1000;
                tvTiempo.setText(segundosRestantes+"S");
            }

            //Cuando se Acaba el Tiempo
            @Override
            public void onFinish() {
                tvTiempo.setText("0S");
                gameOver = true;
                MensajeGameOver();
            }
        }.start();
    }

    private void MensajeGameOver() {
        String ubicacion = "fuentes/zombie.TTF";
        Typeface typeface = Typeface.createFromAsset(EscenarioJuego.this.getAssets(),ubicacion);

        TextView seAcaboTXT,hasMatadoTXT,numeroTXT;

        Button jugarDeNuevo,irMenu,puntuacion;

        miDialog.setContentView(R.layout.gameover);

        seAcaboTXT = miDialog.findViewById(R.id.seAcaboTXT);
        hasMatadoTXT = miDialog.findViewById(R.id.hasMatadoTXT);
        numeroTXT = miDialog.findViewById(R.id.numeroTXT);

        jugarDeNuevo = miDialog.findViewById(R.id.jugarDeNuevo);
        irMenu = miDialog.findViewById(R.id.irMenu);
        puntuacion = miDialog.findViewById(R.id.puntuacion);

        String zombies = String.valueOf(contador);
        numeroTXT.setText(zombies);

        seAcaboTXT.setTypeface(typeface);
        hasMatadoTXT.setTypeface(typeface);
        numeroTXT.setTypeface(typeface);

        jugarDeNuevo.setTypeface(typeface);
        irMenu.setTypeface(typeface);
        puntuacion.setTypeface(typeface);

        jugarDeNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EscenarioJuego.this, "JUGAR DE NUEVO", Toast.LENGTH_SHORT).show();
            }
        });

        irMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EscenarioJuego.this, "MENU", Toast.LENGTH_SHORT).show();
            }
        });

        puntuacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EscenarioJuego.this, "VER PUNTUACIONES", Toast.LENGTH_SHORT).show();
            }
        });
        miDialog.show();
    }
}