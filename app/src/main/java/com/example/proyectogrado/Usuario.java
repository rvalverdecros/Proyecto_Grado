package com.example.proyectogrado;

public class Usuario {
    String Contraseña, Correo, Edad, Fecha, Imagen, Nombre, Pais, Uid;
    int Zombies;

    public Usuario() {
    }

    public Usuario(String contraseña, String correo, String edad, String fecha, String imagen, String nombre, String pais, String uid, int zombies) {
        Contraseña = contraseña;
        Correo = correo;
        Edad = edad;
        Fecha = fecha;
        Imagen = imagen;
        Nombre = nombre;
        Pais = pais;
        Uid = uid;
        Zombies = zombies;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getEdad() {
        return Edad;
    }

    public void setEdad(String edad) {
        Edad = edad;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public int getZombies() {
        return Zombies;
    }

    public void setZombies(int zombies) {
        Zombies = zombies;
    }
}
