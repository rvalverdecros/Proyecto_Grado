package com.example.proyectogrado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Puntajes extends AppCompatActivity {

    LinearLayoutManager mLayoutManager;
    RecyclerView recyclerViewUsuarios;
    Adaptador adaptador;
    List<Usuario> usuarioList;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntajes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Puntuaciones");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mLayoutManager = new LinearLayoutManager(this);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios);

        mLayoutManager.setReverseLayout(true); /*Se va a ordenar de Z-A */
        mLayoutManager.setStackFromEnd(true); /*EMPIEZA DESDE ARRIBA SIN TENER DESLIZ*/
        recyclerViewUsuarios.setHasFixedSize(true);
        recyclerViewUsuarios.setLayoutManager(mLayoutManager);
        usuarioList = new ArrayList<>();

        ObtenerTodosLosUsuarios();
    }

    private void ObtenerTodosLosUsuarios() {

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser(); /*OBTENER EL USUARIO ACTUAL*/
        DatabaseReference ref = FirebaseDatabase.getInstance("https://zombie-buster-dc5f7-default-rtdb.europe-west1.firebasedatabase.app").getReference("MI BASE DE DATOS JUGADORES");

        ref.orderByChild("Zombies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usuarioList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){

                    // DEL MODELO USUARIO
                    Usuario usuario = ds.getValue(Usuario.class);

                    /*if(!usuario.getUid().equals(fUser.getUid())){
                        usuarioList.add(usuario);
                    }*/

                    usuarioList.add(usuario);

                    adaptador = new Adaptador(Puntajes.this, usuarioList);
                    recyclerViewUsuarios.setAdapter(adaptador);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}