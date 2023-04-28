package com.example.proyectogrado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    EditText correoEt,passEt,nombreEt, edadEt, paisEt;
    TextView fechatxt;
    Button registrar;
    FirebaseAuth auth; //La Autenticacion de Firebase



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_registro);

        correoEt = findViewById(R.id.correoEt);
        passEt = findViewById(R.id.passEt);
        nombreEt = findViewById(R.id.nombreEt);
        edadEt = findViewById(R.id.edadEt);
        paisEt = findViewById(R.id.paisEt);
        fechatxt = findViewById(R.id.fechatxt);
        registrar = findViewById(R.id.registrar);

        //UBICACION
        String ubicacion ="fuentes/zombie.TTF";
        Typeface tf = Typeface.createFromAsset(Registro.this.getAssets(),ubicacion);

        registrar.setTypeface(tf);

        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String stringFecha = fecha.format(date);
        fechatxt.setText(stringFecha);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = correoEt.getText().toString();
                String password = passEt.getText().toString();

                //Validacion de datos
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correoEt.setError("Correo no Valido");
                    correoEt.setFocusable(true);
                } else if (password.length()<6) {
                    passEt.setError("Contraseña debe de ser mayor a 6");
                    passEt.setFocusable(true);
                }else{
                    RegistrarJugador(email,password);
                }
            }
        });
    }

    //Metodo para regsitrar un jugador
    private void RegistrarJugador(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Si el jugador fue registrado corrcetamente
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();

                    int contador = 0;

                    assert user != null; //El usuario no es nulo
                    String uidString = user.getUid();
                    String correoString = correoEt.getText().toString();
                    String passString = passEt.getText().toString();
                    String nombreString = nombreEt.getText().toString();
                    String edadString = edadEt.getText().toString();
                    String paisString = paisEt.getText().toString();
                    String fechaString = fechatxt.toString();

                    HashMap<Object,Object> datosJugador = new HashMap<>();

                    datosJugador.put("Uid", uidString);
                    datosJugador.put("Correo", correoString);
                    datosJugador.put("Contraseña", passString);
                    datosJugador.put("Nombre", nombreString);
                    datosJugador.put("Edad", edadString);
                    datosJugador.put("Pais", paisString);
                    datosJugador.put("Imagen", "");
                    datosJugador.put("Fecha", fechaString);
                    datosJugador.put("Zombies", contador);

                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://zombie-buster-dc5f7-default-rtdb.europe-west1.firebasedatabase.app");
                    DatabaseReference reference = database.getReference("MI BASE DE DATOS JUGADORES");
                    reference.child(uidString).setValue(datosJugador);
                    startActivity(new Intent(Registro.this, Menu.class));
                    Toast.makeText(Registro.this, "USUARIO REGISTRADO!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(Registro.this, "Ha Ocurrido un Error!", Toast.LENGTH_SHORT).show();
                }
            }
        })
         //Si Falla el Registro
         .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}