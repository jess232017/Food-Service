package com.kopodermo.proyecto_final;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kopodermo.proyecto_final.sqlite.BDAdapter;
import com.kopodermo.proyecto_final.view.activity.Config.Config_Activity;
import com.kopodermo.proyecto_final.view.activity.Gestion.GCliente_Activity;
import com.kopodermo.proyecto_final.view.activity.Pedido.Cliente_Activity;
import com.kopodermo.proyecto_final.view.activity.Gestion.Producto_Activity;
import com.kopodermo.proyecto_final.view.activity.Reporte.Reporte_Activity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Main_Activity extends AppCompatActivity {
    Toolbar toolbar;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);

        toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.tb_general));

        BDAdapter BD = new BDAdapter(this);
        BD.openDB();

        int i=0;
        String[] Metodos = new String[3];
        Cursor c = BD.obtenerFormasPago();
        while (c.moveToNext()) {
            Metodos[i] = c.getString(1);
            i++;
        }

        //no funciona bien
        int Credito = BD.contarCabecerasPedidosXMetodoPago(Metodos[1]);
        int num = BD.contarCabecerasPedidos();


        BD.closeDB();

        TextView pedidos = findViewById(R.id.txtPedidos);
        pedidos.setText(Integer.toString(num));

        TextView reportes = findViewById(R.id.txtReporte);
        reportes.setText(Integer.toString(num));

        TextView txtFecha = findViewById(R.id.txtFecha);
        txtFecha.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(Calendar.getInstance().getTime()));

        TextView txtDeuda = findViewById(R.id.txtDeuda);
        txtDeuda.setText(Integer.toString(Credito));

        TextView txtSemana = findViewById(R.id.txtSemana);
        txtSemana.setText(Integer.toString(num));

        TextView txtHoy = findViewById(R.id.txtHoy);
        txtHoy.setText(Integer.toString(num));
    }

    @SuppressLint("SetTextI18n")
    private void CustomDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_gestion);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -2;
        ((TextView) dialog.findViewById(R.id.Title)).setText(getResources().getString(R.string.edition));

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.info).setOnClickListener(v -> {
            Intent intent = new Intent(this, Credit_Activity.class);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.fabCliente).setOnClickListener(v -> {
            Intent intent = new Intent(this, GCliente_Activity.class);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.fabProducto).setOnClickListener(v -> {
            Intent intent = new Intent(this, Producto_Activity.class);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.fabReporte).setOnClickListener(v -> {
            Intent intent = new Intent(this, Config_Activity.class);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }



    public void goPedidos(View view) {
        Intent intent = new Intent(this, Cliente_Activity.class);
        startActivity(intent);
    }

    public void goReporte(View view) {
        Intent intent = new Intent(this, Reporte_Activity.class);
        startActivity(intent);
    }

    public void goEdiccion(View view) {
        CustomDialog();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.config_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.EditInfo) {
            Intent intent = new Intent(this, Reporte_Activity.class);
            startActivity(intent);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);  //exit application
    }
}
