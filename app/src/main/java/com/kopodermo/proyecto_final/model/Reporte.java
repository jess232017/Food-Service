package com.kopodermo.proyecto_final.model;

public class Reporte {
    private String Nombre;
    private String EstadoPago;
    private String Fecha_Pago;
    private String Fecha_Entr;
    private Float Total;
    private String IdCabecera;

    public Reporte(String idCabecera, String nombre, String fecha_Pago, String fecha_Entr, Float total, String estadoPago) {
        IdCabecera = idCabecera;
        Nombre = nombre;
        EstadoPago = estadoPago;
        Fecha_Entr = fecha_Entr;
        Fecha_Pago = fecha_Pago;
        Total = total;
    }


    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getEstadoPago() {
        return EstadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        EstadoPago = estadoPago;
    }

    public String getFecha_Pago() {
        return Fecha_Pago;
    }

    public void setFecha_Pago(String fecha_Pago) {
        Fecha_Pago = fecha_Pago;
    }

    public String getFecha_Entr() {
        return Fecha_Entr;
    }

    public void setFecha_Entr(String fecha_Entr) {
        Fecha_Entr = fecha_Entr;
    }

    public Float getTotal() {
        return Total;
    }

    public void setTotal(Float total) {
        Total = total;
    }

    public String getIdCabecera() {
        return IdCabecera;
    }

    public void setIdCabecera(String idCabecera) {
        IdCabecera = idCabecera;
    }
}
