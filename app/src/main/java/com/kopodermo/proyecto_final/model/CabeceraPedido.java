package com.kopodermo.proyecto_final.model;

public class CabeceraPedido {

    public String idCabeceraPedido;

    public String fecha;

    public String fecha_pago;

    public String fecha_entrega;

    public String idCliente;

    public String idFormaPago;

    public CabeceraPedido(String idCabeceraPedido, String fecha,
                          String fecha_pago, String fecha_entrega,
                          String idCliente, String idFormaPago) {
        this.idCabeceraPedido = idCabeceraPedido;
        this.fecha = fecha;
        this.fecha_pago = fecha_pago;
        this.fecha_entrega = fecha_entrega;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idFormaPago = idFormaPago;
    }
}
