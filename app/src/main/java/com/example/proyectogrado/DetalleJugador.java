package com.example.proyectogrado;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetalleJugador extends AppCompatActivity {

    //Coge los datos del usuario
    ImageView ImagenDetalle;
    TextView INFORMACIONDETALLE, NombreDetalle, CorreoDetalle, PuntajeDetalle,EdadDetalle,PaisDetalle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_jugador);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detalle");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        ImagenDetalle = findViewById(R.id.ImagenDetalle);
        INFORMACIONDETALLE = findViewById(R.id.INFORMACIONDETALLE);
        NombreDetalle = findViewById(R.id.NombreDetalle);
        CorreoDetalle = findViewById(R.id.CorreoDetalle);
        PuntajeDetalle = findViewById(R.id.PuntajeDetalle);
        EdadDetalle = findViewById(R.id.EdadDetalle);
        PaisDetalle = findViewById(R.id.PaisDetalle);



        String imagen = getIntent().getStringExtra("Imagen");
        String nombre = getIntent().getStringExtra("Nombre");
        String correo = getIntent().getStringExtra("Correo");
        String puntaje = getIntent().getStringExtra("Puntaje");
        String edad = getIntent().getStringExtra("Edad");
        String pais = getIntent().getStringExtra("Pais");

        NombreDetalle.setText("Nombre: "+nombre);
        CorreoDetalle.setText("Correo: "+correo);
        PuntajeDetalle.setText("Puntuacion: "+puntaje);
        EdadDetalle.setText("Edad: "+edad);
        PaisDetalle.setText("Pais: "+pais);

        //GESTIONAR IMAGEN

        try {
            Picasso.get().load(imagen).into(ImagenDetalle);
        }catch (Exception e){
            Picasso.get().load(R.drawable.jugador).into(ImagenDetalle);
        }

        CambioDeFuente();

    }
    private void CambioDeFuente(){
        //CAMBIO DE LETRA

        String ubicacion = "fuentes/zombie.TTF";
        Typeface tf = Typeface.createFromAsset(DetalleJugador.this.getAssets(),ubicacion);

        //CAMBIO DE LETRA
        INFORMACIONDETALLE.setTypeface(tf);
        NombreDetalle.setTypeface(tf);
        CorreoDetalle.setTypeface(tf);
        PuntajeDetalle.setTypeface(tf);
        EdadDetalle.setTypeface(tf);
        PaisDetalle.setTypeface(tf);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}