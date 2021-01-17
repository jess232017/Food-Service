package com.kopodermo.proyecto_final.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.kopodermo.proyecto_final.model.Cliente;
import com.kopodermo.proyecto_final.model.Producto;
import com.kopodermo.proyecto_final.sqlite.Constants.CabecerasPedido;
import com.kopodermo.proyecto_final.sqlite.Constants.Clientes;
import com.kopodermo.proyecto_final.sqlite.Constants.DetallesPedido;
import com.kopodermo.proyecto_final.sqlite.Constants.FormasPago;
import com.kopodermo.proyecto_final.sqlite.Constants.Productos;
import com.kopodermo.proyecto_final.sqlite.Constants.Profile;

import java.util.Arrays;
import java.util.List;

public class BDHelper extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DATOS = "pedidos.db";

    private static final int VERSION_ACTUAL = 1;

    private Context contexto;

    interface Tablas{
        String CABECERA_PEDIDO = "cabecera_pedido";
        String DETALLE_PEDIDO = "detalle_pedido";
        String PRODUCTO = "producto";
        String CLIENTE = "cliente";
        String FORMA_PAGO = "forma_pago";
        String PROFILE = "profile";
    }

    interface Referencias {
        String ID_CABECERA_PEDIDO = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                Tablas.CABECERA_PEDIDO, CabecerasPedido.ID);

        String ID_PRODUCTO = String.format("REFERENCES %s(%s)",
                Tablas.PRODUCTO, Productos.ID);

        String ID_CLIENTE = String.format("REFERENCES %s(%s)",
                Tablas.CLIENTE, Clientes.ID);

        String ID_FORMA_PAGO = String.format("REFERENCES %s(%s)",
                Tablas.FORMA_PAGO, FormasPago.ID);
    }

    public BDHelper(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL)",
                Tablas.PROFILE, BaseColumns._ID,
                Profile.ID, Profile.CORREO, Profile.USUARIO, Profile.CONTRA, Profile.NOMBRE, Profile.EDAD, Profile.GENERO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s DATETIME NOT NULL," +
                        "%s DATETIME NOT NULL,%s DATETIME NOT NULL," +
                        "%s TEXT NOT NULL %s,%s TEXT NOT NULL %s)",
                Tablas.CABECERA_PEDIDO, BaseColumns._ID,
                CabecerasPedido.ID, CabecerasPedido.FECHA,
                CabecerasPedido.FECHAENTREGA, CabecerasPedido.FECHAPAGO,
                CabecerasPedido.ID_CLIENTE, Referencias.ID_CLIENTE, CabecerasPedido.ID_FORMA_PAGO, Referencias.ID_FORMA_PAGO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL %s," +
                        "%s INTEGER NOT NULL CHECK (%s>0)," +
                        "%s INTEGER NOT NULL %s," +
                        "%s INTEGER NOT NULL,%s REAL NOT NULL," +
                        "UNIQUE (%s,%s) )",
                Tablas.DETALLE_PEDIDO, BaseColumns._ID,
                DetallesPedido.ID, Referencias.ID_CABECERA_PEDIDO,
                DetallesPedido.SECUENCIA, DetallesPedido.SECUENCIA,
                DetallesPedido.ID_PRODUCTO, Referencias.ID_PRODUCTO,
                DetallesPedido.CANTIDAD, DetallesPedido.PRECIO,
                DetallesPedido.ID, DetallesPedido.SECUENCIA));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s REAL NOT NULL," +
                        "%s INTEGER NOT NULL CHECK(%s>=0)," +
                        "%s INTEGER NOT NULL,%s TEXT NOT NULL)",
                Tablas.PRODUCTO, BaseColumns._ID,
                Productos.ID, Productos.NOMBRE, Productos.PRECIO,
                Productos.EXISTENCIAS, Productos.EXISTENCIAS,
                Productos.TIPO,Productos.IMAGEN));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL,%s TEXT NOT NULL)",
                Tablas.CLIENTE, BaseColumns._ID,
                Clientes.ID, Clientes.NOMBRES, Clientes.APELLIDOS,
                Clientes.TELEFONO, Clientes.IMAGEN));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL )",
                Tablas.FORMA_PAGO, BaseColumns._ID,
                FormasPago.ID, FormasPago.NOMBRE));

        // Inserción del perfil unico por defecto
        ContentValues valores = new ContentValues();
        valores.put(Profile.ID,  Profile.getIdPerfil());
        valores.put(Profile.CORREO, "jess232016@gmail.com");
        valores.put(Profile.USUARIO, "Jess23");
        valores.put(Profile.CONTRA, "8168");
        valores.put(Profile.NOMBRE, "Jesus Enmanuel Hernandez Gonzalez");
        valores.put(Profile.EDAD, "19");
        valores.put(Profile.GENERO, "Masculino");
        db.insertOrThrow(Tablas.PROFILE, null, valores);

        // Inserción Formas de pago
        valores.clear();
        // Generar Pk
        String idFormaPago = FormasPago.generarIdFormaPago();

        valores.put(FormasPago.ID, idFormaPago);
        valores.put(FormasPago.NOMBRE, "Efectivo");
        db.insertOrThrow(Tablas.FORMA_PAGO, null, valores);

        idFormaPago = FormasPago.generarIdFormaPago();
        valores.put(FormasPago.ID, idFormaPago);
        valores.put(FormasPago.NOMBRE, "Crédito");
        db.insertOrThrow(Tablas.FORMA_PAGO, null, valores);

        idFormaPago = FormasPago.generarIdFormaPago();
        valores.put(FormasPago.ID, idFormaPago);
        valores.put(FormasPago.NOMBRE, "Pagado");
        db.insertOrThrow(Tablas.FORMA_PAGO, null, valores);

        // Inserción Productos predeterminados
        valores.clear();
        // Generar Pk
        for(int i=0; i< productos.size();i++){
            Producto producto = productos.get(i);
            // Generar Pk
            String idProducto = Productos.generarIdProducto();
            valores.put(Productos.ID, idProducto);
            valores.put(Productos.NOMBRE, producto.getNombre());
            valores.put(Productos.PRECIO, producto.getPrecio());
            valores.put(Productos.EXISTENCIAS, producto.getExistencias());
            valores.put(Productos.TIPO, producto.getTipo());
            valores.put(Productos.IMAGEN, producto.getImagen());
            db.insertOrThrow(Tablas.PRODUCTO, null, valores);
        }

        // Inserción Clientes predeterminados
        valores.clear();
        // Generar Pk
        for(int i=0; i< clientes.size();i++){
            Cliente cliente = clientes.get(i);
            // Generar Pk
            String idCliente = Clientes.generarIdCliente();
            valores.put(Clientes.ID, idCliente);
            valores.put(Clientes.NOMBRES, cliente.getNombres());
            valores.put(Clientes.APELLIDOS, cliente.getApellidos());
            valores.put(Clientes.TELEFONO, cliente.getTelefono());
            valores.put(Clientes.IMAGEN, cliente.getImagen());
            db.insertOrThrow(Tablas.CLIENTE, null, valores);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CABECERA_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.DETALLE_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.PRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CLIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.FORMA_PAGO);

        onCreate(db);
    }

    private final static List<Producto> productos = Arrays.asList(
            new Producto(null,"Carne Asada"  , 120.0f,25,1,"https://s3-media2.fl.yelpcdn.com/bphoto/tFBqYAvMYuIMnGFq4TyPHw/o.jpg"),
            new Producto(null,"Pollo Asado"  , 120.0f,25,1,"https://www.el19digital.com/files/articulos/144285.jpg"),
            new Producto(null,"Cerdo Asado"  , 120.0f,25,1,"https://i.ytimg.com/vi/qO6qpbTgjls/maxresdefault.jpg"),
            new Producto(null,"Enchilada"    , 35.0f ,35,1,"https://i.pinimg.com/originals/fb/4b/36/fb4b36556eb4084c908609022e247349.jpg"),
            new Producto(null,"Taco"         , 25.0f ,25,1,"https://i.ytimg.com/vi/tf0BYpgwqRk/maxresdefault.jpg"),
            new Producto(null,"Torta de Papa", 25.0f ,25,1,"https://d3icrsxj31wib.cloudfront.net/recipes_ok/5811/L/MAIN_RECIPE_30477_20162218515.jpg"),
            new Producto(null,"Cola Shaker"  , 20.0f ,90,2,"http://apen.org.ni/wp-content/uploads/2018/01/Logo-kola-shaler-1.png"),
            new Producto(null,"Agua"         , 15.0f ,90,2,"https://png.pngtree.com/element_origin_min_pic/16/08/01/11579ec2f3cff68.jpg"),
            new Producto(null,"Café"         , 10.0f ,90,2,"https://www.ikea.com/PIAimages/0711058_PE727933_S5.JPG"),
            new Producto(null,"Queso"        , 7.0f  ,50,3,"https://cocina-casera.com/wp-content/uploads/2017/11/queso-frito.jpg"),
            new Producto(null,"Arroz"        , 25.0f  ,50,3,"http://www.guerrillero.cu/images/2018/enero/arroz_cocina_fpt2.jpg")
    );

    private final static  List<Cliente> clientes = Arrays.asList(
            new Cliente("Carlos Jose", "Diaz Diaz", "81923333", "https://cdn.pixabay.com/photo/2019/04/12/14/46/ocean-4122312_960_720.jpg"),
            new Cliente("Jhon Carlos", "Mendoza Arauz", "81092345", "https://cdn.pixabay.com/photo/2015/03/17/14/05/person-677770_960_720.jpg"),
            new Cliente("Edward Rafael", "Ruiz", "80126754", "https://cdn.pixabay.com/photo/2017/02/09/20/44/love-2053479_960_720.jpg"),
            new Cliente("Jenny", "Gomez Flores", "80123459", "https://cdn.pixabay.com/photo/2019/06/24/07/22/family-4295385_960_720.jpg"),
            new Cliente("Keneth Martinez", "Martinez Diaz", "80124534", "https://cdn.pixabay.com/photo/2012/03/04/01/01/baby-22194_960_720.jpg")
    );
}
