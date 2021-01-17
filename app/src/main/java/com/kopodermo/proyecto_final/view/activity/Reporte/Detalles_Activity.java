package com.kopodermo.proyecto_final.view.activity.Reporte;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.kopodermo.proyecto_final.Main_Activity;
import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.adapter.Detalles_Adapter;
import com.kopodermo.proyecto_final.model.CabeceraPedido;
import com.kopodermo.proyecto_final.model.Producto;
import com.kopodermo.proyecto_final.sqlite.BDAdapter;
import com.tuann.floatingactionbuttonexpandable.FloatingActionButtonExpandable;

import java.util.ArrayList;
import java.util.Objects;

public class Detalles_Activity extends AppCompatActivity {
    private Detalles_Adapter detalles_adapter;
    private String Id_Cabecera = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        Id_Cabecera = getIntent().getStringExtra("Id_Cabecera");
        String forma_Pago = getIntent().getStringExtra("Forma_Pago");
        String FechaPedido = getIntent().getStringExtra("FechaPedido");
        String FechaEntrega = getIntent().getStringExtra("FechaEntrega");
        String FechaPago = getIntent().getStringExtra("FechaPago");
        String IdCliente = getIntent().getStringExtra("IdCliente");

        TextView order_number = findViewById(R.id.order_number);
        order_number.setText("Orden ID: #" + Id_Cabecera);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.tb_reportdetails));

        RecyclerView DetallesRecycler = findViewById(R.id.checkout_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        DetallesRecycler.setLayoutManager(linearLayoutManager);
        DetallesRecycler.setHasFixedSize(true);
        DetallesRecycler.addItemDecoration(new DividerItemDecoration(this, 1));

        detalles_adapter = new Detalles_Adapter(CargarProductos(), R.layout.raw_detail);
        DetallesRecycler.setAdapter(detalles_adapter);


        FloatingActionButtonExpandable fab = findViewById(R.id.fab);
        assert forma_Pago != null;
        if (!forma_Pago.equals("Crédito")){
            fab.setContent("¿Cancelar Pago?");
            fab.setBackgroundButtonColor(ContextCompat.getColor(this, R.color.red_400));
        }


        fab.setOnClickListener(view -> {
            String Pago;
            String[] Metodos = new String[3];

            BDAdapter db = new BDAdapter(Detalles_Activity.this);
            db.openDB();
            Cursor c = db.obtenerFormasPago();

            int i=0;
            while (c.moveToNext()) {
                Metodos[i] = c.getString(1);
                i++;
            }

            if (forma_Pago.equals("Crédito")){
                Pago = Metodos[2];
            }else{
                Pago = Metodos[1];
            }

            db.actualizarCabeceraPedido(
                    new CabeceraPedido(Id_Cabecera, FechaPedido,
                            FechaEntrega,FechaPago,IdCliente, Pago));

            db.closeDB();


            new KAlertDialog(this)
                    .setTitleText("¡Estado Actualizado!")
                    .setConfirmClickListener(sDialog -> {
                        Intent intent = new Intent(Detalles_Activity.this, Main_Activity.class);
                        Detalles_Activity.this.startActivity(intent);
                    })
                    .show();
        });
    }

    @SuppressLint("SetTextI18n")
    private ArrayList<Producto> CargarProductos(){
        ArrayList<Producto> productos = new ArrayList<>();
        float Total = .0f;

        BDAdapter db = new BDAdapter(this);
        db.openDB();
        //DatabaseUtils.dumpCursor(db.obtenerDetallesPorIdPedido(Id_Cabecera));
        try
        {
            Producto p;
            Cursor Pedido = db.obtenerDetallesPorIdPedido(Id_Cabecera);
            while (Pedido.moveToNext()) {
                p = new Producto(Pedido.getString(3),"",Pedido.getFloat(5),
                        -1,-1,"",Pedido.getInt(4));
                Total += Pedido.getFloat(5) * Pedido.getInt(4);
                Cursor Prdts = db.obtenerProducto( p.getIdProducto());
                if(Prdts.moveToNext()){
                    p.setNombre(Prdts.getString(2));
                }
                productos.add(p);
            }

            TextView order_item_count = findViewById(R.id.order_item_count);
            TextView order_total_amount = findViewById(R.id.order_total_amount);
            TextView order_full_amounts = findViewById(R.id.order_full_amounts);
            order_item_count.setText("" + productos.size());
            order_total_amount.setText("C$" + Total);
            order_full_amounts.setText("C$" + Total);
            db.closeDB();
            return productos;

        } catch (Exception e) {
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return productos;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
