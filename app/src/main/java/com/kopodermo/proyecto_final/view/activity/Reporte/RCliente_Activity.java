package com.kopodermo.proyecto_final.view.activity.Reporte;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.adapter.Reportes_Adapter;
import com.kopodermo.proyecto_final.model.Reporte;
import com.kopodermo.proyecto_final.sqlite.BDAdapter;
import com.kopodermo.proyecto_final.view.activity.Pedido.Pedido_Activity;

import java.util.ArrayList;
import java.util.Objects;

public class RCliente_Activity extends AppCompatActivity {
    private String ID_Cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcliente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Reporte");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ID_Cliente = getIntent().getStringExtra("ID_Cliente");
        String nombres = getIntent().getStringExtra("Nombres");
        String phone1 = getIntent().getStringExtra("Phone");
        String image1 = getIntent().getStringExtra("Image");

        TextView name = findViewById(R.id.name);
        TextView phone = findViewById(R.id.phone);
        ImageView image = findViewById(R.id.image);
        ImageButton NuevaOrden = findViewById(R.id.NuevaOrden);

        name.setText(nombres);
        phone.setText(phone1);

        Glide.with(this)
                .load(image1)
                .into(image);

        NuevaOrden.setOnClickListener(v -> {
            Intent intent = new Intent(RCliente_Activity.this, Pedido_Activity.class);
            intent.putExtra("ID_Cliente", ID_Cliente);
            RCliente_Activity.this.startActivity(intent);
        });

        RecyclerView ReportesRv = findViewById(R.id.order_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        ReportesRv.setLayoutManager(linearLayoutManager);
        ReportesRv.setHasFixedSize(true);
        ReportesRv.addItemDecoration(new DividerItemDecoration(this, 1));

        Reportes_Adapter reportes_adapter = new Reportes_Adapter(this,CargarReportes(), R.layout.raw_reporte2);
        ReportesRv.setAdapter(reportes_adapter);
    }

    private ArrayList<Reporte> CargarReportes(){
        ArrayList<Reporte> reportes = new ArrayList<>();

        BDAdapter db = new BDAdapter(this);
        db.openDB();
        DatabaseUtils.dumpCursor(db.obtenerCabecerasPorUserId(ID_Cliente));
        try
        {
            Reporte p;
            Cursor c = db.obtenerCabecerasPorUserId(ID_Cliente);
            while (c.moveToNext()) {
                p= new Reporte(c.getString(0),c.getString(1) + " " + c.getString(2),
                        c.getString(4), c.getString(5), null, c.getString(6));
                p.setTotal(db.sumarDetallesPedidoPorId(c.getString(0)));
                reportes.add(p);
            }
            db.closeDB();
            return reportes;

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            return reportes;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
