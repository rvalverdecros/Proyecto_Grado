package com.example.proyectogrado;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference jugadores;

    TextView mipuntuaciontxt;
    TextView zombies,uid,correo,nombre,edad,pais,menutxt;
    Button jugarbtn,editarbtn,cambiarPassbtn,puntuacionesbtn,acercadebtn,cerrarSesion;
    CircleImageView imagenPerfil;

    private StorageReference referenciaDeAlmacenamiento;
    private String rutaAlmacenamiento = "FotosDePerfil/*";

    //PERMISOS

    private static final int codigo_De_Solitud_De_Almacenamiento = 200;
    private static final int codigo_Para_La_Seleccion_De_La_Imagen = 300;

    //MATRICES

    private String [] permisosDeAlmacenamiento;
    private Uri imagen_Uri;
    private String perfil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_menu);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance("https://zombie-buster-dc5f7-default-rtdb.europe-west1.firebasedatabase.app");
        jugadores = firebaseDatabase.getReference("MI BASE DE DATOS JUGADORES");

        referenciaDeAlmacenamiento = FirebaseStorage.getInstance().getReference();
        permisosDeAlmacenamiento = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //UBICACION
        String ubicacion ="fuentes/zombie.TTF";
        Typeface tf = Typeface.createFromAsset(Menu.this.getAssets(),ubicacion);

        mipuntuaciontxt = findViewById(R.id.mipuntuaciontxt);

        //PERFIL
        imagenPerfil = findViewById(R.id.imagenPerfil);
        uid = findViewById(R.id.uid);
        correo = findViewById(R.id.correo);
        nombre = findViewById(R.id.nombre);
        edad = findViewById(R.id.edad);
        pais = findViewById(R.id.pais);
        menutxt = findViewById(R.id.menutxt);
        zombies = findViewById(R.id.zombies);

        //OPCIONES DEL USUARIO
        jugarbtn = findViewById(R.id.jugarbtn);
        editarbtn = findViewById(R.id.editarbtn);
        cambiarPassbtn = findViewById(R.id.cambiarPassbtn);
        puntuacionesbtn = findViewById(R.id.puntuacionesbtn);
        acercadebtn = findViewById(R.id.acercadebtn);
        cerrarSesion = findViewById(R.id.cerrarSesionbtn);

        mipuntuaciontxt.setTypeface(tf);
        uid.setTypeface(tf);
        correo.setTypeface(tf);
        nombre.setTypeface(tf);
        edad.setTypeface(tf);
        pais.setTypeface(tf);
        zombies.setTypeface(tf);
        menutxt.setTypeface(tf);


        jugarbtn.setTypeface(tf);
        puntuacionesbtn.setTypeface(tf);
        acercadebtn.setTypeface(tf);
        cerrarSesion.setTypeface(tf);
        editarbtn.setTypeface(tf);
        cambiarPassbtn.setTypeface(tf);

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

        editarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "EDITAR", Toast.LENGTH_SHORT).show();
                EditarDatos();
            }
        });

        cambiarPassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "CAMBIAR CONTRASEÑA", Toast.LENGTH_SHORT).show();
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

    private void EditarDatos() {

        //Definido el array con las opciones disponibles que podemos elegir
        String [] opciones = {"Foto de Perfil","Cambiar Edad", "Cambiar Pais"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    perfil = "Imagen";
                    ActualizarFotoPerfil();
                }

                if (i == 1){
                    ActualizarEdad("Edad");
                }

                if (i == 2){
                    ActualizarPais("Pais");
                }
            }
        });
        builder.create().show();
    }

    private void ActualizarEdad(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: " + key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        EditText editText = new EditText(this);
        editText.setHint("Ingrese " + key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        //Si el usuario hace click a Actualizar
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String,Object> result = new HashMap<>();
                result.put(key,value);
                jugadores.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Menu.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Menu.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Menu.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    private void ActualizarPais(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: " + key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        EditText editText = new EditText(this);
        editText.setHint("Ingrese " + key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        //Si el usuario hace click a Actualizar
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String,Object> result = new HashMap<>();
                result.put(key,value);
                jugadores.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Menu.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Menu.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Menu.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }


    //CAMBIO DE FOTO
    private void ActualizarFotoPerfil() {
        String [] opciones = {"Galeria"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    //Seleccion de GaleriA
                    if (!ComprobarPermisosAlmacenamiento()){
                        //SI NO SE HABILITA EL PERMISO
                        SolicitarPermisoAlAlmacenamiento();
                    }else{
                        ElegirImagenDeGaleria();
                    }
                }
            }
        });
        builder.create().show();
    }



    //Metodo de almacenamiento en tiempo de ejecucion
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void SolicitarPermisoAlAlmacenamiento() {
        requestPermissions(permisosDeAlmacenamiento,codigo_De_Solitud_De_Almacenamiento);
    }

    //COMPRUEBA SI LOS PERMISOS DE ALMACENAMIENTO ESTAN HABILITADOS O NO
    private boolean ComprobarPermisosAlmacenamiento() {
        boolean resultado = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return resultado;
    }

    //SE LLAMA CUANDO EL USUARIO PRESIONA PERMINITIR O DENEGAR EL CUADRO DEL DIALOGO
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case codigo_De_Solitud_De_Almacenamiento:{
                //SELECCION DE LA GALERIA
                if(grantResults.length > 0){
                    boolean escrituraDeAlmacenamientoAceptado = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (escrituraDeAlmacenamientoAceptado){
                        //PERMISO HABILIDADO
                        ElegirImagenDeGaleria();
                    }else{
                        Toast.makeText(this, "HABILITE EL PERMISO A LA GALERIA", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Se llama cuando el jugador ya ha elegido imagen de la galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            //LA imagen vamos a obtener la uir
            if (requestCode == codigo_Para_La_Seleccion_De_La_Imagen){
                imagen_Uri = data.getData();
                SubirFoto(imagen_Uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Cambia la foto de perfil del jugador y lo actualiza en la base de datos
    private void SubirFoto(Uri imagen_uri) {
        String rutaDeArchivoYNombre = rutaAlmacenamiento + "" + perfil + "_"+ user.getUid();
        StorageReference storageReference = referenciaDeAlmacenamiento.child(rutaDeArchivoYNombre);
        storageReference.putFile(imagen_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloaduri= uriTask.getResult();


                            if (uriTask.isSuccessful()){
                                HashMap<String,Object> resultado = new HashMap<>();
                                resultado.put(perfil,downloaduri.toString());
                                jugadores.child(user.getUid()).updateChildren(resultado)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Menu.this, "LA IMAGEN HA SIDO CAMBIADA CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Menu.this, "HA OCURRIDO UN ERROR", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                }else {
                                Toast.makeText(Menu.this, "ALGO HA SALIDO MAL!", Toast.LENGTH_SHORT).show();
                            }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Menu.this, "ALGO HA SALIDO MAL!", Toast.LENGTH_SHORT).show();
                    }
                });

    };

    private void ElegirImagenDeGaleria() {
        Intent intentGaleria = new Intent(Intent.ACTION_PICK);
        intentGaleria.setType("image/*");
        startActivityForResult(intentGaleria, codigo_Para_La_Seleccion_De_La_Imagen);
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
                    String edadString = ""+ ds.child("Edad").getValue();
                    String paisString = ""+ ds.child("Pais").getValue();
                    String imagen = ""+ ds.child("Imagen").getValue();


                    //Set de los datos
                    zombies.setText(zombiesString);
                    uid.setText(uidString);
                    correo.setText("Correo: "+emailString);
                    nombre.setText("Nombres: "+nombreString);
                    edad.setText("Edad: "+edadString);
                    pais.setText("Pais: "+paisString);

                    try {
                        Picasso.get().load(imagen).into(imagenPerfil);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.soldado).into(imagenPerfil);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}