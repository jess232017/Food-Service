package com.kopodermo.proyecto_final.model;

public class DetallePedido {

    public String idCabeceraPedido;

    public int secuencia;

    public String idProducto;

    public int cantidad;

    public float precio;

    public DetallePedido(String idCabeceraPedido, int secuencia,
                         String idProducto, int cantidad, float precio) {
        this.idCabeceraPedido = idCabeceraPedido;
        this.secuencia = secuencia;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
    }
}
