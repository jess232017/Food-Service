package com.kopodermo.proyecto_final.view.activity.Pedido;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kopodermo.proyecto_final.Main_Activity;
import com.kopodermo.proyecto_final.R;

public class Finalizado_Activity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizado);

        String Id_Pedido = getIntent().getStringExtra("Id_Pedido");
        String fechaActual = getIntent().getStringExtra("fechaActual");
        TextView idOrden = findViewById(R.id.idOrden);
        TextView date = findViewById(R.id.idDate);

        idOrden.setText("ID de la Orden : " + Id_Pedido);
        date.setText("Fecha del Encargo : " + fechaActual);
    }

    public void goMenu(View view) {
        Intent intent = new Intent(this, Main_Activity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main_Activity.class);
        startActivity(intent);
    }
}