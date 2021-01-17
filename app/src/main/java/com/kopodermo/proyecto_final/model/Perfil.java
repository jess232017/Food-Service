package com.kopodermo.proyecto_final.model;

public class Perfil {
    private String Correo, Usuario, Contra, Nombre, Edad, Genero;

    public Perfil(String correo, String usuario, String contra, String nombre, String edad, String genero) {
        Correo = correo;
        Usuario = usuario;
        Contra = contra;
        Nombre = nombre;
        Edad = edad;
        Genero = genero;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getContra() {
        return Contra;
    }

    public void setContra(String contra) {
        Contra = contra;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getEdad() {
        return Edad;
    }

    public void setEdad(String edad) {
        Edad = edad;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String genero) {
        Genero = genero;
    }
}
