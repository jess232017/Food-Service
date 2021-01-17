package com.kopodermo.proyecto_final;

import android.content.Intent;
import com.google.android.material.textfield.TextInputEditText;
import com.kopodermo.proyecto_final.model.Perfil;
import com.kopodermo.proyecto_final.sqlite.BDAdapter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class Login_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
    }

    public void goHome(View view){
        TextInputEditText txtUser = findViewById(R.id.txtEdit_User);
        TextInputEditText txtPass = findViewById(R.id.txtEdit_Pass);
        String strUsuario = Objects.requireNonNull(txtUser.getText()).toString().trim();
        String strPassword = Objects.requireNonNull(txtPass.getText()).toString().trim();

        BDAdapter db = new BDAdapter(this);
        db.openDB();

        try
        {
            if (strUsuario.isEmpty() || strPassword.isEmpty()) {
                Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            }
            else{
                Perfil perfil = db.obtenerUsuario();
                if(strUsuario.equals(perfil.getCorreo()) && strPassword.equals(perfil.getContra())) {
                    Intent intent = new Intent(this, Main_Activity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Credenciales Incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e) {
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);  //exit application
    }
}
