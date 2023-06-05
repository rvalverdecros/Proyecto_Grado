package com.example.proyectogrado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CambioDePass extends AppCompatActivity {

    //Llamamos a las variables
    EditText ActualPass, NuevoPass;
    Button CambiarPass;
    DatabaseReference BASEDEDATOS;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_de_pass);

        ActualPass = findViewById(R.id.ActualPass);
        NuevoPass = findViewById(R.id.NuevoPass);
        CambiarPass = findViewById(R.id.CambiarPass);

        //Conectamos con la base de datos

        BASEDEDATOS = FirebaseDatabase.getInstance("https://zombie-buster-dc5f7-default-rtdb.europe-west1.firebasedatabase.app").getReference("MI BASE DE DATOS JUGADORES");
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        CambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ACTUAL = ActualPass.getText().toString().trim();
                String NUEVA = NuevoPass.getText().toString().trim();

                //En caso de no rellenar bien los datos
                if(TextUtils.isEmpty(ACTUAL)){
                    Toast.makeText(CambioDePass.this, "Llenar campo actual contraseña", Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(NUEVA)){
                    Toast.makeText(CambioDePass.this, "Llenar campo Nueva contraseña", Toast.LENGTH_SHORT).show();
                }

                if(!TextUtils.isEmpty(ACTUAL) && !TextUtils.isEmpty(NUEVA) && ACTUAL.length()>=6 && NUEVA.length() >= 6){
                    CambioDePassJugador(ACTUAL,NUEVA);
                }
            }
        });
    }

    //Funcion que sirve para cambiar la contraseña del jugador
    private void CambioDePassJugador(String actual, String nueva) {

        AuthCredential authCredential = EmailAuthProvider.getCredential((user.getEmail()),actual);

        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.updatePassword(nueva)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        String value = NuevoPass.getText().toString().trim();
                                        HashMap<String,Object> result = new HashMap<>();
                                        result.put("Contraseña", value);
                                        BASEDEDATOS.child(user.getUid()).updateChildren(result)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CambioDePass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(CambioDePass.this, Login.class));
                                        Toast.makeText(CambioDePass.this, "Se ha cerrado sesion", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CambioDePass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CambioDePass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}