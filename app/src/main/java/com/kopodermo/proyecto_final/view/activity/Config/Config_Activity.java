package com.kopodermo.proyecto_final.view.activity.Config;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.model.Perfil;
import com.kopodermo.proyecto_final.sqlite.BDAdapter;

import java.util.Objects;

public class Config_Activity extends AppCompatActivity {
    private TextView TvCorreo, TvUsuario, TvContra, TvNombre, TvEdad, TvGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.profile));

        TvCorreo = findViewById(R.id.TvCorreo);
        TvUsuario = findViewById(R.id.TvUsuario);
        TvContra = findViewById(R.id.TvContra);
        TvNombre = findViewById(R.id.TvNombre);
        TvEdad = findViewById(R.id.TvEdad);
        TvGenero = findViewById(R.id.TvGenero);

        BDAdapter db = new BDAdapter(this);
        db.openDB();
        try
        {
            Perfil perfil = db.obtenerUsuario();
            db.closeDB();
            TvCorreo.setText(perfil.getCorreo());
            TvUsuario.setText(perfil.getUsuario());
            TvContra.setText(perfil.getContra());
            TvNombre.setText(perfil.getNombre());
            TvEdad.setText(perfil.getEdad());
            TvGenero.setText(perfil.getGenero());

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    public void Save(View view) {
        String correo = Objects.requireNonNull(TvCorreo.getText()).toString().trim();
        String usuario = Objects.requireNonNull(TvUsuario.getText()).toString().trim();
        String contra = Objects.requireNonNull(TvContra.getText()).toString().trim();
        String nombre = Objects.requireNonNull(TvNombre.getText()).toString().trim();
        String edad = Objects.requireNonNull(TvEdad.getText()).toString().trim();
        String genero = Objects.requireNonNull(TvGenero.getText()).toString().trim();

        try
        {
            if (correo.isEmpty() || usuario.isEmpty() || contra.isEmpty() || nombre.isEmpty() || edad.isEmpty() || genero.isEmpty()) {
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
            }
            else{
                BDAdapter db = new BDAdapter(this);
                db.openDB();
                if(db.actualizarUsuario(new Perfil(correo, usuario, contra, nombre, edad, genero))){
                    Toast.makeText(this, "El perfil ha sido actualizado", Toast.LENGTH_SHORT).show();
                    onSupportNavigateUp();
                }
                else
                    Toast.makeText(this, "El perfil no se pudo actualizar", Toast.LENGTH_SHORT).show();
                db.closeDB();

            }
        }catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}