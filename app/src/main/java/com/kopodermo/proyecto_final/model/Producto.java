package com.kopodermo.proyecto_final.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Producto implements Parcelable {

    private String idProducto;
    private String nombre;
    private float precio;
    private int existencias;
    private String imagen;
    private int Tipo;  // 1= Comida ||  2=Bebida || 3=Extra
    private int Cantidad; //Auxiliar nunca se almacena en la columna producto de la db

    public Producto(String idProducto, String nombre, float precio, int existencias, int Tipo,  String imagen) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.existencias = existencias;
        this.Tipo = Tipo;
        this.imagen = imagen;
        this.Cantidad = 0 ;
    }

    public Producto(String idProducto, String nombre, float precio, int existencias, int Tipo,  String imagen, int Cantidad) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.existencias = existencias;
        this.Tipo = Tipo;
        this.imagen = imagen;
        this.Cantidad = Cantidad;
    }

    protected Producto(Parcel in) {
        idProducto = in.readString();
        nombre = in.readString();
        precio = in.readFloat();
        existencias = in.readInt();
        imagen = in.readString();
        Tipo = in.readInt();
        Cantidad = in.readInt();
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int tipo) {
        Tipo = tipo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idProducto);
        dest.writeString(nombre);
        dest.writeFloat(precio);
        dest.writeInt(existencias);
        dest.writeString(imagen);
        dest.writeInt(Tipo);
        dest.writeInt(Cantidad);
    }
}
