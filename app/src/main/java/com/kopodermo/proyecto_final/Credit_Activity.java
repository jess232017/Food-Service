package com.kopodermo.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class Credit_Activity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.tb_credit));

        /*toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.tb_credit));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
