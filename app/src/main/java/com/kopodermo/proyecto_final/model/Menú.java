package com.kopodermo.proyecto_final.model;

import java.util.ArrayList;

public class Menú {
    private Float Precio;
    private int Incrementar;
    private ArrayList<Producto> productos;
    private String Debug;

    public Menú(Float precio, ArrayList<Producto> Productos, int incrementar, String debug) {
        Precio = precio;
        productos = Productos;
        Incrementar = incrementar;
        Debug = debug;
    }

    public Float getPrecio() {
        return Precio;
    }

    public void setPrecio(Float total) {
        Precio = total;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    public int getIncrementar() {
        return Incrementar;
    }

    public void setIncrementar(int incrementar) {
        Incrementar = incrementar;
    }

    public String getDebug() {
        return Debug;
    }

    public void setDebug(String debug) {
        Debug = debug;
    }
}
