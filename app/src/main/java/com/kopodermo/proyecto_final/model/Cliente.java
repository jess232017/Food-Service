package com.kopodermo.proyecto_final.model;

public class Cliente {

    private String idCliente;

    private String nombres;

    private String apellidos;

    private String telefono;

    private String imagen;

    private int Pedidos; //Auxiliar NO se almacena directamente en la BD

    public Cliente(String nombres, String apellidos, String telefono, String imagen) {
        this.idCliente = null;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.imagen = imagen;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getPedidos() {
        return Pedidos;
    }

    public void setPedidos(int pedidos) {
        Pedidos = pedidos;
    }
}
