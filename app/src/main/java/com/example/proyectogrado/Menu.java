package com.example.proyectogrado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference jugadores;

    TextView mipuntuaciontxt;
    TextView zombies,uid,correo,nombre,menutxt;
    Button jugarbtn,puntuacionesbtn,acercadebtn,cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_menu);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance("https://zombie-buster-dc5f7-default-rtdb.europe-west1.firebasedatabase.app");
        jugadores = firebaseDatabase.getReference("MI BASE DE DATOS JUGADORES");

        //UBICACION
        String ubicacion ="fuentes/zombie.TTF";
        Typeface tf = Typeface.createFromAsset(Menu.this.getAssets(),ubicacion);

        mipuntuaciontxt = findViewById(R.id.mipuntuaciontxt);
        uid = findViewById(R.id.uid);
        correo = findViewById(R.id.correo);
        nombre = findViewById(R.id.nombre);
        menutxt = findViewById(R.id.menutxt);
        zombies = findViewById(R.id.zombies);

        jugarbtn = findViewById(R.id.jugarbtn);
        puntuacionesbtn = findViewById(R.id.puntuacionesbtn);
        acercadebtn = findViewById(R.id.acercadebtn);
        cerrarSesion = findViewById(R.id.cerrarSesionbtn);

        mipuntuaciontxt.setTypeface(tf);
        uid.setTypeface(tf);
        correo.setTypeface(tf);
        nombre.setTypeface(tf);
        menutxt.setTypeface(tf);


        jugarbtn.setTypeface(tf);
        puntuacionesbtn.setTypeface(tf);
        acercadebtn.setTypeface(tf);
        cerrarSesion.setTypeface(tf);

        jugarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Menu.this, "JUGAR", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(Menu.this,EscenarioJuego.class);

                String  uidS = uid.getText().toString();
                String nombreS = nombre.getText().toString();
                String zombieS = zombies.getText().toString();

                intent.putExtra("UID",uidS);
                intent.putExtra("NOMBRE",nombreS);
                intent.putExtra("ZOMBIES",zombieS);

                startActivity(intent);
                Toast.makeText(Menu.this, "ENVIANDO PARAMETROS", Toast.LENGTH_SHORT).show();
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
            Consulta();
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

    //Metodo para realizar la consulta
    private void Consulta(){
        //Consulta
        Query query = jugadores.orderByChild("Correo").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    //Obtener los datos

                    String zombiesString = ""+ ds.child("Zombies").getValue();
                    String uidString = ""+ ds.child("Uid").getValue();
                    String emailString = ""+ ds.child("Correo").getValue();
                    String nombreString = ""+ ds.child("Nombre").getValue();

                    //Set de los datos
                    zombies.setText(zombiesString);
                    uid.setText(uidString);
                    correo.setText(emailString);
                    nombre.setText(nombreString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}