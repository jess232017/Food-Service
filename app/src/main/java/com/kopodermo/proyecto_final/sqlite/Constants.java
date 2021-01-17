package com.kopodermo.proyecto_final.sqlite;

import java.util.UUID;

public class Constants {

    interface ColumnasPerfil {
        String ID = "id";
        String CORREO   = "correo";
        String USUARIO  = "usuario";
        String CONTRA   = "contra";
        String NOMBRE   = "nombre";
        String EDAD     = "edad";
        String GENERO   = "genero";
    }

    interface ColumnasCabeceraPedido {
        String ID = "id";
        String FECHA = "fecha";
        String FECHAPAGO = "fecha_pago";
        String FECHAENTREGA = "fecha_entrega";
        String ID_CLIENTE = "id_cliente";
        String ID_FORMA_PAGO = "id_forma_pago";
    }

    interface ColumnasDetallePedido {
        String ID = "id";
        String SECUENCIA = "secuencia";
        String ID_PRODUCTO = "id_producto";
        String CANTIDAD = "cantidad";
        String PRECIO = "precio";
    }

    interface ColumnasProducto {
        String ID = "id";
        String NOMBRE = "nombre";
        String PRECIO = "precio";
        String EXISTENCIAS = "existencias";
        String IMAGEN = "imagen";
        String TIPO = "tipo";
    }

    interface ColumnasCliente {
        String ID = "id";
        String NOMBRES = "nombres";
        String APELLIDOS = "apellidos";
        String TELEFONO = "telefono";
        String IMAGEN = "imagen";
    }

    interface ColumnasFormaPago {
        String ID = "id";
        String NOMBRE = "nombre";
    }

    public static class CabecerasPedido implements ColumnasCabeceraPedido {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }

    public static class DetallesPedido implements ColumnasDetallePedido {
        // Métodos auxiliares
    }

    public static class Productos implements ColumnasProducto{
        public static String generarIdProducto() {
            return "PRO-" + UUID.randomUUID().toString();
        }
    }

    public static class Clientes implements ColumnasCliente{
        public static String generarIdCliente() {
            return "CLI-" + UUID.randomUUID().toString();
        }
    }

    public static class FormasPago implements ColumnasFormaPago{
        public static String generarIdFormaPago() {
            return "FP-" + UUID.randomUUID().toString();
        }
    }

    public static class Profile implements ColumnasPerfil{
        public static String getIdPerfil() {
            return "PERFILUNICO-0001";
        }
    }


    private Constants() {
    }
}