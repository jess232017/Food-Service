package com.kopodermo.proyecto_final.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.kopodermo.proyecto_final.model.CabeceraPedido;
import com.kopodermo.proyecto_final.model.Cliente;
import com.kopodermo.proyecto_final.model.DetallePedido;
import com.kopodermo.proyecto_final.model.FormaPago;
import com.kopodermo.proyecto_final.model.Perfil;
import com.kopodermo.proyecto_final.model.Producto;

import static android.content.ContentValues.TAG;
import static com.kopodermo.proyecto_final.sqlite.BDHelper.*;
import static com.kopodermo.proyecto_final.sqlite.Constants.*;

public class BDAdapter {
    //-----------------
    private SQLiteDatabase db;
    private BDHelper helper;

    private static final String CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO = "cabecera_pedido " +
            "INNER JOIN cliente " +
            "ON cabecera_pedido.id_cliente = cliente.id " +
            "INNER JOIN forma_pago " +
            "ON cabecera_pedido.id_forma_pago = forma_pago.id";


    private final String[] proyCabeceraPedido = new String[]{
            Tablas.CABECERA_PEDIDO + "." + CabecerasPedido.ID,
            Clientes.NOMBRES,
            Clientes.APELLIDOS,
            CabecerasPedido.FECHA,
            CabecerasPedido.FECHAENTREGA,
            CabecerasPedido.FECHAPAGO,
            FormasPago.NOMBRE};

    public BDAdapter(Context c) {
        helper=new BDHelper(c);
    }

    //Abrir Base de Datos
    public void openDB()
    {
        try
        {
            db=helper.getWritableDatabase();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    //Cerrar Base de Datos
    public void closeDB()
    {
        try
        {
            helper.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // [OPERACIONES_DETALLE_PEDIDO]
    public Cursor obtenerDetallesPedido() {
        String sql = String.format("SELECT * FROM %s", Tablas.DETALLE_PEDIDO);
        return db.rawQuery(sql, null);
    }

    public Cursor obtenerDetallesPorIdPedido(String idCabeceraPedido) {
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.DETALLE_PEDIDO, CabecerasPedido.ID);
        String[] selectionArgs = {idCabeceraPedido};
        return db.rawQuery(sql, selectionArgs);
    }

    public float sumarDetallesPedidoPorId (String idCabeceraPedido){
        float num = 0.0f;
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                 Tablas.DETALLE_PEDIDO, CabecerasPedido.ID);
        String[] selectionArgs = {idCabeceraPedido};
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()) {
            num += c.getFloat(5) * c.getInt(4);
        }
        c.close();
        return num;
    }

    @SuppressLint("DefaultLocale")
    public String insertarDetallePedido(DetallePedido detalle) {
        ContentValues valores = new ContentValues();
        valores.put(DetallesPedido.ID, detalle.idCabeceraPedido);
        valores.put(DetallesPedido.SECUENCIA, detalle.secuencia);
        valores.put(DetallesPedido.ID_PRODUCTO, detalle.idProducto);
        valores.put(DetallesPedido.CANTIDAD, detalle.cantidad);
        valores.put(DetallesPedido.PRECIO, detalle.precio);
        db.insertOrThrow(Tablas.DETALLE_PEDIDO, null, valores);

        return String.format("%s#%d", detalle.idCabeceraPedido, detalle.secuencia);
    }

    public boolean actualizarDetallePedido(DetallePedido detalle) {
        ContentValues valores = new ContentValues();
        valores.put(DetallesPedido.SECUENCIA, detalle.secuencia);
        valores.put(DetallesPedido.CANTIDAD, detalle.cantidad);
        valores.put(DetallesPedido.PRECIO, detalle.precio);

        String selection = String.format("%s=? AND %s=?",
                DetallesPedido.ID, DetallesPedido.SECUENCIA);
        final String[] whereArgs = {detalle.idCabeceraPedido, String.valueOf(detalle.secuencia)};

        int resultado = db.update(Tablas.DETALLE_PEDIDO, valores, selection, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarDetallePedido(String idCabeceraPedido, int secuencia) {
        String selection = String.format("%s=? AND %s=?",
                DetallesPedido.ID, DetallesPedido.SECUENCIA);
        String[] whereArgs = {idCabeceraPedido, String.valueOf(secuencia)};

        int resultado = db.delete(Tablas.DETALLE_PEDIDO, selection, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_DETALLE_PEDIDO]

    // [OPERACIONES_CABECERA_PEDIDO]
    public Cursor obtenerCabecerasPedidos() {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO);

        return builder.query(db, proyCabeceraPedido, null, null, null, null, null);
    }

    public int contarCabecerasPedidos (){
        int num = 0;
        String sql= "SELECT * FROM " + Tablas.CABECERA_PEDIDO + ";";
        Cursor c = db.rawQuery(sql,null);
        if(c.moveToFirst())
            num =  c.getCount();
        c.close();
        return num;
    }

    public int contarCabecerasPedidosXMetodoPago (String Metodo_Pago){
        int num = 0;

        String selection = String.format("%s=?", CabecerasPedido.ID_FORMA_PAGO);
        String[] selectionArgs = {Metodo_Pago};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO);

        Cursor c = builder.query(db, proyCabeceraPedido, selection, selectionArgs, null, null, null);
        if(c.moveToFirst())
            num =  c.getCount();

        c.close();

        return num;
    }

    public int obtenerPedidosPorUsuario(String id_Cliente) {
        int num = 0;
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.CABECERA_PEDIDO, CabecerasPedido.ID_CLIENTE);
        String[] selectionArgs = {id_Cliente};
        Cursor c = db.rawQuery(sql, selectionArgs);

        if(c.moveToFirst())
            num =  c.getCount();
        c.close();
        return num;
    }

    public Cursor obtenerCabeceraPorId(String id) {

        String selection = String.format("%s=?", CabecerasPedido.ID);
        String[] selectionArgs = {id};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO);

        String[] proyeccion = {
                Tablas.CABECERA_PEDIDO + "." + CabecerasPedido.ID,
                CabecerasPedido.FECHA,
                Clientes.NOMBRES,
                Clientes.APELLIDOS,
                FormasPago.NOMBRE};

        return builder.query(db, proyeccion, selection, selectionArgs, null, null, null);
    }

    public Cursor obtenerCabecerasPorUserId(String Userid) {

        String selection = String.format("%s=?", CabecerasPedido.ID_CLIENTE);
        String[] selectionArgs = {Userid};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO);

        return builder.query(db, proyCabeceraPedido, selection, selectionArgs, null, null, null);
    }

    public Cursor obtenerCabecerasPorRangoFechas(String DATE1, String DATE2) {
        String sql= "SELECT * FROM " + Tablas.CABECERA_PEDIDO + " WHERE " + CabecerasPedido.FECHAPAGO + " BETWEEN " +
                "'" + DATE1 + "'" + " AND " + "'" + DATE2 + "'" +";";

        return db.rawQuery(sql,null);
    }

    public Cursor obtenerCabecerasPorMetodo_Pago(String Metodo_Pago) {

        String selection = String.format("%s=?", CabecerasPedido.ID_FORMA_PAGO);
        String[] selectionArgs = {Metodo_Pago};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO);

        return builder.query(db, proyCabeceraPedido, selection, selectionArgs, null, null, null);
    }

    public String insertarCabeceraPedido(CabeceraPedido pedido) {

        // Generar Pk
        String idCabeceraPedido = CabecerasPedido.generarIdCabeceraPedido();

        ContentValues valores = new ContentValues();
        valores.put(CabecerasPedido.ID, idCabeceraPedido);
        valores.put(CabecerasPedido.FECHA, pedido.fecha);
        valores.put(CabecerasPedido.FECHAENTREGA, pedido.fecha_entrega);
        valores.put(CabecerasPedido.FECHAPAGO, pedido.fecha_pago);
        valores.put(CabecerasPedido.ID_CLIENTE, pedido.idCliente);
        valores.put(CabecerasPedido.ID_FORMA_PAGO, pedido.idFormaPago);

        // Insertar cabecera
        db.insertOrThrow(Tablas.CABECERA_PEDIDO, null, valores);

        return idCabeceraPedido;
    }

    public boolean actualizarCabeceraPedido(CabeceraPedido pedidoNuevo) {
        ContentValues valores = new ContentValues();
        //valores.put(CabecerasPedido.FECHA, pedidoNuevo.fecha);
        //valores.put(CabecerasPedido.ID_CLIENTE, pedidoNuevo.idCliente);
        valores.put(CabecerasPedido.ID_FORMA_PAGO, pedidoNuevo.idFormaPago);

        String whereClause = String.format("%s=?", CabecerasPedido.ID);
        String[] whereArgs = {pedidoNuevo.idCabeceraPedido};

        int resultado = db.update(Tablas.CABECERA_PEDIDO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarCabeceraPedido(String idCabeceraPedido) {

        String whereClause = CabecerasPedido.ID + "=?";
        String[] whereArgs = {idCabeceraPedido};

        int resultado = db.delete(Tablas.CABECERA_PEDIDO, whereClause, whereArgs);

        return resultado > 0;
    }

    // [/OPERACIONES_CABECERA_PEDIDO]

    // [OPERACIONES_PRODUCTO]
    //Obtener todos los productos
    public Cursor obtenerProductos()
    {
        String sql="SELECT * FROM " + Tablas.PRODUCTO + ";";
        return db.rawQuery(sql,null);
    }

    //ObtenerProducto
    public Cursor obtenerProducto(String ID_Producto) {
        String sql = "SELECT * FROM " + Tablas.PRODUCTO + " WHERE " + Productos.ID + " LIKE '%" + ID_Producto + "%'";
        return db.rawQuery(sql,null);
    }

    //Agregar productos
    public String insertarProducto(Producto producto) {
        ContentValues valores = new ContentValues();
        // Generar Pk
        String idProducto = Productos.generarIdProducto();
        valores.put(Productos.ID, idProducto);
        valores.put(Productos.NOMBRE, producto.getNombre());
        valores.put(Productos.PRECIO, producto.getPrecio());
        valores.put(Productos.EXISTENCIAS, producto.getExistencias());
        valores.put(Productos.TIPO, producto.getTipo());
        valores.put(Productos.IMAGEN, producto.getImagen());

        db.insertOrThrow(Tablas.PRODUCTO, null, valores);

        return idProducto;
    }

    public boolean actualizarProducto(Producto producto) {
        ContentValues valores = new ContentValues();
        valores.put(Productos.NOMBRE, producto.getNombre());
        valores.put(Productos.PRECIO, producto.getPrecio());
        valores.put(Productos.EXISTENCIAS, producto.getExistencias());
        valores.put(Productos.TIPO, producto.getTipo());
        valores.put(Productos.IMAGEN, producto.getImagen());

        String whereClause = String.format("%s=?", Productos.ID);
        String[] whereArgs = {producto.getIdProducto()};

        int resultado = db.update(Tablas.PRODUCTO, valores, whereClause, whereArgs);
        return resultado > 0;
    }

    public boolean eliminarProducto(String idProducto) {
        String whereClause = String.format("%s=?", Productos.ID);
        String[] whereArgs = {idProducto};

        int resultado = db.delete(Tablas.PRODUCTO, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_PRODUCTO]

    // [OPERACIONES_CLIENTE]
    public Cursor obtenerClientes() {
        String sql="SELECT * FROM " + Tablas.CLIENTE + ";";
        return db.rawQuery(sql,null);
    }

    public Cursor obtenerCliente(String ID_Cliente) {
        String sql = "SELECT * FROM " + Tablas.CLIENTE + " WHERE " + Clientes.ID + " LIKE '%" + ID_Cliente + "%'";
        return db.rawQuery(sql,null);
    }

    public String insertarCliente(Cliente cliente) {
        // Generar Pk
        String idCliente = Clientes.generarIdCliente();

        ContentValues valores = new ContentValues();
        valores.put(Clientes.ID, idCliente);
        valores.put(Clientes.NOMBRES, cliente.getNombres());
        valores.put(Clientes.APELLIDOS, cliente.getApellidos());
        valores.put(Clientes.TELEFONO, cliente.getTelefono());
        valores.put(Clientes.IMAGEN, cliente.getImagen());

        return db.insertOrThrow(Tablas.CLIENTE, null, valores) > 0 ? idCliente : null;
    }

    public boolean actualizarCliente(Cliente cliente) {

        ContentValues valores = new ContentValues();
        valores.put(Clientes.NOMBRES, cliente.getNombres());
        valores.put(Clientes.APELLIDOS, cliente.getApellidos());
        valores.put(Clientes.TELEFONO, cliente.getTelefono());
        valores.put(Clientes.IMAGEN, cliente.getImagen());

        String whereClause = String.format("%s=?", Clientes.ID);
        final String[] whereArgs = {cliente.getIdCliente()};

        int resultado = db.update(Tablas.CLIENTE, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarCliente(String idCliente) {

        String whereClause = String.format("%s=?", Clientes.ID);
        final String[] whereArgs = {idCliente};

        int resultado = db.delete(Tablas.CLIENTE, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_CLIENTE]

    // [OPERACIONES_FORMA_PAGO]
    public Cursor obtenerFormasPago() {
        String sql = String.format("SELECT * FROM %s", Tablas.FORMA_PAGO);
        return db.rawQuery(sql, null);
    }

    public String insertarFormaPago(FormaPago formaPago) {
        // Generar Pk
        String idFormaPago = FormasPago.generarIdFormaPago();

        ContentValues valores = new ContentValues();
        valores.put(FormasPago.ID, idFormaPago);
        valores.put(FormasPago.NOMBRE, formaPago.nombre);

        return db.insertOrThrow(Tablas.FORMA_PAGO, null, valores) > 0 ? idFormaPago : null;
    }

    public boolean actualizarFormaPago(FormaPago formaPago) {

        ContentValues valores = new ContentValues();
        valores.put(FormasPago.NOMBRE, formaPago.nombre);

        String whereClause = String.format("%s=?", FormasPago.ID);
        String[] whereArgs = {formaPago.idFormaPago};

        int resultado = db.update(Tablas.FORMA_PAGO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarFormaPago(String idFormaPago) {

        String whereClause = String.format("%s=?", FormasPago.ID);
        String[] whereArgs = {idFormaPago};

        int resultado = db.delete(Tablas.FORMA_PAGO, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_FORMA_PAGO]

    // [OPERACIONES_PERFIL]

    public Perfil obtenerUsuario() {
        String sql1="SELECT * FROM " + Tablas.PROFILE + " WHERE " + Profile.ID + " LIKE '%" + "PERFILUNICO-0001" + "%'";
        Perfil perfil = null;
        try
        {

            Cursor c = db.rawQuery(sql1,null);
            if(c.moveToNext()){
                perfil = new Perfil(c.getString(2),c.getString(3),c.getString(4),
                        c.getString(5),c.getString(6),c.getString(7));
            }
            c.close();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        return perfil;
    }

    public boolean actualizarUsuario(Perfil perfil) {
        ContentValues valores = new ContentValues();

        valores.put(Profile.CORREO, perfil.getCorreo());
        valores.put(Profile.USUARIO, perfil.getUsuario());
        valores.put(Profile.CONTRA, perfil.getContra());
        valores.put(Profile.NOMBRE, perfil.getNombre());
        valores.put(Profile.EDAD, perfil.getEdad());
        valores.put(Profile.GENERO, perfil.getUsuario());

        String whereClause = String.format("%s=?", Profile.ID);
        String[] whereArgs = {"PERFILUNICO-0001"};

        int resultado = db.update(Tablas.PROFILE, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    // [/OPERACIONES_PERFIL]
}