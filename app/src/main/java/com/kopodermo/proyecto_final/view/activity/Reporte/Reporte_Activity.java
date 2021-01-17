package com.kopodermo.proyecto_final.view.activity.Reporte;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.adapter.Reportes_Adapter;
import com.kopodermo.proyecto_final.model.Reporte;
import com.kopodermo.proyecto_final.sqlite.BDAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Reporte_Activity extends AppCompatActivity {
    private final Calendar cldr = Calendar.getInstance();
    private int day = cldr.get(Calendar.DAY_OF_MONTH);
    private int month = cldr.get(Calendar.MONTH);
    private int year = cldr.get(Calendar.YEAR);
    public String date1 = "";
    public String date2 = "";
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.tb_reporte));

        RecyclerView ReportesRv = findViewById(R.id.order_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        ReportesRv.setLayoutManager(linearLayoutManager);
        ReportesRv.setHasFixedSize(true);
        ReportesRv.addItemDecoration(new DividerItemDecoration(this, 1));

        Reportes_Adapter reportes_adapter = new Reportes_Adapter(this,CargarReportes(), R.layout.raw_reporte2);
        ReportesRv.setAdapter(reportes_adapter);

        FloatingActionButton fabCliente = findViewById(R.id.fabCliente);
        fabCliente.setOnClickListener(v -> {
            Intent intent = new Intent(Reporte_Activity.this, SCliente_Activity.class);
            Reporte_Activity.this.startActivity(intent);
        });

        FloatingActionButton fabFecha = findViewById(R.id.fabFecha);
        fabFecha.setOnClickListener(v -> CustomDialog());

        FloatingActionButton fabCredit = findViewById(R.id.fabCredit);
        fabCredit.setOnClickListener(v -> {
            Intent intent = new Intent(Reporte_Activity.this, Credito_Activity.class);
            Reporte_Activity.this.startActivity(intent);
        });

        FloatingActionButton fabDebito = findViewById(R.id.fabDebito);
        fabDebito.setOnClickListener(v -> {
            Intent intent = new Intent(Reporte_Activity.this, Credito_Activity.class);
            intent.putExtra("Metodo", 0);
            Reporte_Activity.this.startActivity(intent);
        });
    }

    private ArrayList<Reporte> CargarReportes(){
        ArrayList<Reporte> reportes = new ArrayList<>();

        BDAdapter db = new BDAdapter(this);
        db.openDB();
        DatabaseUtils.dumpCursor(db.obtenerCabecerasPedidos());
        try
        {
            Reporte p;
            Cursor c = db.obtenerCabecerasPedidos();
            while (c.moveToNext()) {
                p= new Reporte(c.getString(0),c.getString(1) + " " + c.getString(2),
                        c.getString(4), c.getString(5), null, c.getString(6));
                p.setTotal(db.sumarDetallesPedidoPorId(c.getString(0)));
                reportes.add(p);
            }
            db.closeDB();
            return reportes;

        } catch (Exception e) {
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            return reportes;
        }
    }

    @SuppressLint("SetTextI18n")
    private void CustomDialog(){
        final Dialog dialog = new Dialog(this);


        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_fecha);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -2;

        ((TextView) dialog.findViewById(R.id.Title)).setText(getResources().getString(R.string.SelectDate));
        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());

        LinearLayout lytFechaInicial = dialog.findViewById(R.id.lytFechaInicial);
        TextView TvFechaInicial = dialog.findViewById(R.id.TvFechaInicial);
        LinearLayout lytFechaFinal = dialog.findViewById(R.id.lytFechaFinal);
        TextView TvFechaFinal = dialog.findViewById(R.id.TvFechaFinal);
        Button bt_filter = dialog.findViewById(R.id.bt_filter);

        TvFechaInicial.setInputType(InputType.TYPE_NULL);
        lytFechaInicial.setOnClickListener(v -> {
            // date picker dialog
            picker = new DatePickerDialog(this,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        date1 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        TvFechaInicial.setText(date1);
                    }, year, month, day);
            picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            picker.show();
        });

        TvFechaFinal.setInputType(InputType.TYPE_NULL);
        lytFechaFinal.setOnClickListener(v -> {
            // date picker dialog
            picker = new DatePickerDialog(this,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        //'2014-02-02'
                        date2 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        TvFechaFinal.setText(date2);
                    }, year, month, day);
            picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            picker.show();
        });

        bt_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BDAdapter db = new BDAdapter(Reporte_Activity.this);
                db.openDB();
                DatabaseUtils.dumpCursor(db.obtenerCabecerasPorRangoFechas(date1,date2));
                db.closeDB();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
