package com.kopodermo.proyecto_final.view.activity.Reporte;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.adapter.Reportes_Adapter;
import com.kopodermo.proyecto_final.model.Reporte;
import com.kopodermo.proyecto_final.sqlite.BDAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class Credito_Activity extends AppCompatActivity {
    int Metodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credito);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.tb_reporte));


        Metodo = getIntent().getIntExtra("Metodo",1);
        if( Metodo!=1 ){
            TextView subtitle = findViewById(R.id.subtitle);
            subtitle.setText(getResources().getString(R.string.tbdebi));
        }

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
        String[] Metodos = new String[3];

        BDAdapter db = new BDAdapter(this);
        db.openDB();
        Cursor c = db.obtenerFormasPago();

        int i=0;
        while (c.moveToNext()) {
            Metodos[i] = c.getString(1);
            i++;
        }

        try
        {
            Reporte p;
            c = db.obtenerCabecerasPedidos();
            while (c.moveToNext()) {
                p= new Reporte(c.getString(0),c.getString(1) + " " + c.getString(2),
                        c.getString(4), c.getString(5), null, c.getString(6));
                p.setTotal(db.sumarDetallesPedidoPorId(c.getString(0)));

                if(Metodo == 1 && c.getString(6).equals("Crédito"))
                    reportes.add(p);
                else if(Metodo == 0 && !c.getString(6).equals("Crédito"))
                    reportes.add(p);
            }
            db.closeDB();
            return reportes;

        } catch (Exception e) {
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return reportes;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
